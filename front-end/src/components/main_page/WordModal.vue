<template>
  <div class="wordModalDiv mainPageColor" :style="{height: wordModalHeight + 'rem'}" :class="animationClass">
    <div class="translationDiv fontType">
        <!-- $store.state.wordModal.wordTranslation -->
        <div class="translationDivTitle" v-if="!isAssess">
            <el-popover
                placement="left-start"
                :title="word"
                width="20rem"
                height="30rem"
                trigger="hover"
                >
                <template #reference>
                    <p>{{word}}</p>
                </template>
                <slot content><p v-for="(item,index) in wordTranslation" :key="index">
					{{!isReviewedWord ? wordTranslation[index] : ''}}
				</p></slot>
            </el-popover>
            <audio ref="audioRef">
                <source :src="audioUrl"/>
            </audio>
            <!-- 'https://dict.youdao.com/dictvoice?type=0&audio=' -->
            <div class="audioButtonDiv">
				<p @click="playAudio(0)">us</p>
            	<p @click="playAudio(1)">uk</p>
			</div>
        </div>
        <div v-else>
            <div v-if="assessArr[assessArr.length - 1] == 1">Is your momery correct?</div>
            <div v-else>Easy to recall?</div>
        </div>
        
    </div>
    <div class="definationDiv">
        
        <div class="partOfSpeechDiv" v-if="(!isReviewedWord && canTranslate) || isAssess">
            <Button :class="partOfSpeechButtonIndex == index ? buttonSelected : ''" fontType="fontType" :width="item.partOfSpeech.length + 'rem'" height="2rem" :btnName="item.partOfSpeech" v-for="(item,index) in wordDetail.meanings" :key="index" @click="changePartOfSpeech(index)">
                {{item.partOfSpeech}}
            </Button>
        </div>
        <span class="fontType" v-if="!isReviewedWord || isAssess">
            {{definitionArr[definitionArrIndex].join(' ')}}
            <div>
                <Button fontType="fontType" width="3.5rem" height="2rem" btnName="pre" v-show="definitionArrIndex > 0" @click="definitionArrIndex--">last</Button>
                <Button fontType="fontType" width="3.5rem" height="2rem" btnName="next" v-show="definitionArr.length > 1 && definitionArrIndex != definitionArr.length - 1" @click="definitionArrIndex++">next</Button>
            </div>
        </span>

        <span class="fontType" v-else-if="isReviewedWord && canTranslate">Do you remember the definiation?</span>
        <div class="difinitionDiv fontType" v-if="(!isReviewedWord && canTranslate) || isAssess">
            definition:
            <Button :class="definitionButtonIndex == index ? buttonSelected : ''" width="3rem" height="2rem" fontType="numberType" :btnName="(index+1)" v-for="(item,index) in wordDetail.meanings[meaningsIndex].definitions.slice(0,3)" :key="index" @click="changeDefinition(index)">
            </Button>
        </div>
    </div>

    <div class="buttonDiv" v-if="!isReviewedWord && canTranslate">
        <Button @click="addWordIntoRepository" width="7rem" height="3rem" btnName="Add" fontSize="1rem" fontType="fontType"/>
        <Button @click="closeModalWindow" width="7rem" height="3rem" btnName="Cancle" fontSize="1rem" fontType="fontType"/>
    </div>
    <div class="buttonDiv" v-else-if="isReviewedWord && canTranslate">
        <Button @click="assessArr.push(1); isAssess=true" width="7rem" height="3rem" btnName="Yes" fontSize="1rem" fontType="fontType"/>
        <Button @click="assessArr.push(0); isAssess=true" width="7rem" height="3rem" btnName="No" fontSize="1rem" fontType="fontType"/>
        <Button v-if="assessArr[assessArr.length - 1] == 0" @click="assessArr.push(-1); isAssess=true" width="7rem" height="3rem" btnName="Forget" fontSize="1rem" fontType="fontType"/>
    </div>

    
  </div>
</template>

<script>
    import { ElMessage } from 'element-plus'
    import Button from '../login/Button.vue'
    import {useStore} from 'vuex'
    import {doPost, doGet} from '@/assets/utils/axios-util.js';
    import { computed, onMounted, reactive, ref, toRef, toRefs, watch } from 'vue'
    import {useMapState} from '@/assets/utils/util-method.js'
    export default {
        name : "WordModal",
        props:["modalWord", "isReviewedWord"],
        emits: ["close"],
        components:{Button},
        setup(props, context){
            let store = useStore()
            let state = useMapState('wordModal', ['wordDefinitionMap', 'wordDefinition', 'wordTranslation'])
            let animationClass = ref("mordalFadeIn")
            let word = computed(()=>{
                return props.modalWord[0][0]
            })
            let definition = ref("");
            let meaningsIndex = ref(0);
            let showDifinition = ref(true);
            let splitNumber = 15;
            let canTranslate = ref(false);
            let isAssess = ref(false);
            let assessArr = reactive([]);
            let startTime;
            let quality = ref(-1);
            let partOfSpeechButtonIndex = ref(0);
            let buttonSelected = ref('buttonSelected');
            let definitionButtonIndex = ref(0);
            //userInfo
            let userInfo = JSON.parse(window.localStorage.getItem("userInfo"));
            let audioUrl = ref("");
            let audioRef = ref(null);
            let info = reactive({
                wordDetail: {
                    meanings:[
                        {
                            antonyms:[],
                            definitions:[
                                {
                                    definition:"",
                                    example: ""
                                }
                            ],
                            partOfSpeech:"",
                            synonyms:[],
                        }
                    ]
                }
            })
            watch(word, (newValue)=>{
                isAssess.value = false;
                //send request to find the defination of word
                assessArr.splice(0);
                partOfSpeechButtonIndex.value = 0;
                definitionButtonIndex.value = 0;
                if(props.isReviewedWord){
                    startTime = new Date();
                }
                if(word.value != undefined){
                    store.dispatch("wordModal/getDefinition",word.value).then(()=>{
                        if(state.wordDefinition.value.length > 0){
                            info.wordDetail = {...state.wordDefinition.value[0]}
                            canTranslate.value = true;
                            //reset definition
                            definition.value = info.wordDetail.meanings[0].definitions[0].definition;
                            //reset mearningsIndex
                            meaningsIndex.value = 0;
							console.log(state.wordDefinitionMap.value);
                        }else{
                            definition.value = "sorry, can't translate this for now"
                            canTranslate.value = false;
                        }       
                    });
                }
            })
            //send quality to backend
            watch(assessArr, (newValue)=>{
                console.log("assessArr");
                quality.value = -1;
                if(newValue.length == 2){
                    if(newValue[0] == 1 && newValue[1] == 1){
                        const timeInterval = Math.floor((new Date() - startTime) / 1000);
                        if(timeInterval <=5){
                            //5 quality
                            quality.value = 5;
                        }else if(timeInterval > 5 && timeInterval < 10){
                            //4 quality
                            quality.value = 4;
                        }else{
                            //3 quality
                            quality.value = 3;
                        }
                    } else if(newValue[0] == 0 && newValue[1] == 0){
                        //1 quality
                        quality.value = 1;
                    } else if(newValue[0] == 0 && newValue[1] == 1){
                        quality.value = 2;
                        //2 quality
                    }else if(newValue[1] == -1){
                        //0 quality
                        quality.value = 0;
                    }
                   
                }
                if(newValue.length == 3){
                    if(newValue[1] == 0 && newValue[2] == 0){
                        //1 quality
                        quality.value = 1;
                    }else if(newValue[1] == 0 && newValue[2] == 1){
                        //2 quality
                        quality.value = 2;

                    }else if(newValue[2] == -1){
                        //0 quality
                        quality.value = 0;
                    }
                }
                console.log(quality.value);
                if(quality.value >= 0){
                    console.log("quality:" + newValue);
                    console.log(props.modalWord[0][0]);
                    assessArr.splice(0);
                    store.dispatch("wordModal/updateNextReviewTime", {
                        userId : userInfo.id,
                        word: word.value,
                        quality : quality.value
                    });
					closeModalWindow();
                }
            })

            let wordTranslation = computed(()=>{
                const arr = store.state.wordModal.wordTranslation.split("\\");
                return arr;
            })


            function addWordIntoRepository(){
                if(!store.state.todayWord.wordMap.has(word.value)){
                    doPost("/v1/article/add-word", {
                        userId : userInfo.id,
                        word : word.value
                    }).then((response) => {
						console.log(response);
                        if(response.data.code == '1000'){
							//add words
                            store.dispatch("wordModal/updateTodayWordListMap", {
								word: word.value,
								translation: state.wordDefinitionMap.value.get(word.value).translation,
								definition: state.wordDefinitionMap.value.get(word.value).definition
							});
                            ElMessage({
                                type: 'success',
                                message: 'Successfully add!',
                                grouping:true,
                            })
                        }else if(response.data.code == '4000'){
							ElMessage({
								type: 'warning',
								message: 'This word has already added!',
								grouping:true,
							})
						}
                    }).catch((err) => {
                        ElMessage({
                            type: 'warning',
                            message: 'This word has already added!',
                            grouping:true,
                        })
                    });
                }else{
                    ElMessage({
                        type: 'warning',
                        message: 'This word has already added!',
                        grouping:true,
                    })
                }
                
            }
        
            let wordModalHeight = computed(()=>store.state.modalParam.wordModalHeight)

            function closeModalWindow(){
                definitionArrIndex.value = 0
                context.emit("close"); 
            }
            
            function changePartOfSpeech(index){
                partOfSpeechButtonIndex.value = index;
                meaningsIndex.value = index;
                definitionButtonIndex.value = 0;
                definition.value = info.wordDetail.meanings[index].definitions[0].definition;
            }

            function changeDefinition(index){
                definitionButtonIndex.value = index;
                definition.value = info.wordDetail.meanings[meaningsIndex.value].definitions[index].definition;
            }

            let definitionArrIndex = ref(0);
            let definitionArr = computed(()=>{
                definitionArrIndex.value = 0;
                let result = [];
                let tempArr = definition.value.split(' ');
                for(let i = 0; i < tempArr.length; i += splitNumber){
                    result.push(tempArr.slice(i, i + splitNumber));
                }
                return result;
            })

            function playAudio(type){
                audioRef.value.src = 'https://dict.youdao.com/dictvoice?type='+type+'&audio='+word.value;
                audioRef.value.play();
            }
        
            return{
                addWordIntoRepository,
                closeModalWindow,
                wordModalHeight,
                animationClass,
                word,
                definition,
                meaningsIndex,
                ...toRefs(info), 
                changePartOfSpeech,
                changeDefinition,
                definitionArr,
                definitionArrIndex,
                showDifinition,
                canTranslate,
                isAssess,
                assessArr,
                partOfSpeechButtonIndex,
                buttonSelected,
                definitionButtonIndex,
                audioUrl,
                audioRef,
                playAudio,
                wordTranslation
            }
        }
    }
</script>

<style scoped>
    @import "@/assets/css/main_page.css";
    @import "@/assets/css/font.css";
	.audioButtonDiv{
		display: flex;
		width: 5rem;
		justify-content: space-between;
	}
    .wordModalDiv{
        width: 30.6rem; 

        box-shadow: 0rem 0.6rem 0.8rem 1.0rem rgba(0, 0, 0, 0.25); 
        border-radius: 2.0rem; 
        border: 0.050rem black solid;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        position: absolute;
        z-index: 10;

    }
    .definationDiv{
        width: 100%;
        height: 50%;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }
    
    .fontType{
        font-size: 1.7rem;
    }
    .translationDiv{
        display: flex;
        flex-direction: column;
        align-items: center;
        font-size: 2rem;
    }

    .partOfSpeechDiv{
        display: flex;
        flex-wrap: wrap;
        justify-content: space-around;
    }
    .buttonDiv{
        display: flex;
        justify-content: space-around;
    }

    .difinitionDiv{
        display: flex;
        justify-content: space-around;
    }

    .definationDiv span{
        padding-left: 0.2rem;
        padding-right: 0.2rem;
        width: 100%;
        height: 60%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        text-align: center;
        word-wrap: normal;
    }
    .definationDiv span div{
        display: flex;
        justify-content: space-around;
        
    }

    .buttonSelected{
        background-color: #f59a9a;
        color: #ffffff;
    }

    .translationDivTitle{
        width: 100%;
        display: flex;
        justify-content: space-around;
    }

    .translationDivTitle p:hover{
        color:  #f59a9a;
        cursor: pointer;
    }
    .el-popper.is-customized {
        /* Set padding to ensure the height is 32px */
        padding: 6px 12px;
        background-color: #FFE1E1;
    }

    .el-popper.is-customized .el-popper__arrow::before {
        background-color: #FFE1E1;
        right: 0;
    }

</style>