<template>
    <div class="wordListVueDiv">
		<div class="editDetailButtonDiv" v-show="showEdit">
			<div @click="chooseAllOrNot" v-show="!isAllChecked">Select All</div>
			<div @click="chooseAllOrNot" v-show="isAllChecked">Cancel Select All</div>
			<div @click="$store.commit('setting/UPDATE_SHOW_EDIT',false)">Cancel Edit</div>
		</div>	
        <el-scrollbar :height="showEdit?'45rem':'50rem' ">
            <ul class="wordListUl" ref="wordListUlRef">
							
                <li class="wordDetailLi" v-for="(item,index) in wordList" :key="item.id">
                    <input ref="checkBoxRef" @click="clickCheckBox($event, item, index)" type="checkbox" class="checkBoxInput" :class="checkBoxAnimationClass" v-show="showEdit">
					<div class="wordDetailInfoDiv">
						<div ref="itemRef" class="wordDiv item">{{item.word}}</div>
						<audio ref="audioRef">
							<source/>
						</audio>
						<!-- 'https://dict.youdao.com/dictvoice?type=0&audio=' -->
						<div ref="itemRef" class="audioButtonDiv item"> 
							<p @click="playAudio(0, item.word)">us</p>
							<p @click="playAudio(1,item.word)">uk</p>
						</div>
						<el-popover
							placement="top-start"
							:title="item.word"
							width="20rem"
							trigger="hover"
							>
							<template #reference>
								<p ref="itemRef" class="template item" v-show="showEye">{{item.translation.split('\\')[0].split(',')[0].split('；')[0]}}</p>
							</template>
							<slot content><p v-for="(translation,index) in item.translation.split('\\')" :key="index">
								{{translation}}</p></slot>
						</el-popover>
						<el-popover
							placement="top-start"
							:title="item.word"
							width="30rem"
							trigger="hover"
							>
							<template #reference>
								<p ref="itemRef" class="template item" v-show="showEye">definition</p>
							</template>
							<slot content><p class="definitionP" v-for="(definition,index) in item.definition.split('\\')" :key="index">
								[{{definition}}]</p></slot>
						</el-popover>
						<div ref="itemRef" class="item">{{parseDate(item.addTime)}}</div>
					</div>
                    <Line class="lineVue"/>
                </li>
				
            </ul>
        </el-scrollbar>
		<div class="editDetailManipulateDiv" v-show="showEdit">
			<div @click="relearnOrSkilledWord('relearn')" :class="canPress ? 'canPressClass' : 'canNotPressClass'" >Relearn</div>
			<div @click="relearnOrSkilledWord('skilled')" :class="canPress ? 'canPressClass' : 'canNotPressClass'" >Skilled</div>
		</div>
    </div>
</template>

<script>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import Line from '../common/Line.vue'
import { useStore } from 'vuex';
import { useRoute } from 'vue-router';
import {parseDate} from '@/assets/utils/util-method.js'
import {doPostJson, doGet} from '@/assets/utils/axios-util.js';
import {ElMessage } from 'element-plus'
export default {
    name : "WordList",
    components:{Line},
    setup(){
        let route = useRoute();
        const store = useStore();
		let wordListUlRef = ref(null);
		let itemRef = ref(null);
		let audioRef = ref(null);
		let isFadeIn = ref(false);
		let checkBoxRef = ref(null);
		let isAllChecked = ref(false);
		let isSomeChecked = ref(false);
		let checkedNumber = ref(0);
		let canPress = ref(false);
		let userInfo = JSON.parse(localStorage.getItem("userInfo"));

		let wordList = computed(()=>{
            return store.state.setting.wordList;
        })
		let checkBoxAnimationClass = computed(()=>{
			return store.state.setting.checkBoxAnimationClass;
		})

        let showEye = computed(()=>{
            return store.state.setting.showEye;
        })

		let showEdit = computed(()=>{
			return store.state.setting.showEdit;
		})
		watch(checkBoxAnimationClass, (newValue)=>{
			if(newValue == 'fadeInLeft'){
				itemRef.value.forEach(item => {
					item.style.transform = 'translateX(5rem)';
				})
			}else{
				itemRef.value.forEach(item => {
					item.style.transform = 'translateX(0rem)';
				})	
			}
		})
		watch(checkedNumber, (newValue)=>{
			if(newValue == 0){
				//Can not press Relearn button
				canPress.value = false;
			}else{
				canPress.value = true;
			}
		})
		
		function chooseAllOrNot(){
			if(!isAllChecked.value){
				checkBoxRef.value.forEach((item)=>{
					item.checked = true;
					isAllChecked.value = true;
					checkedNumber.value = checkBoxRef.value.length;
				})
			}else{
				checkBoxRef.value.forEach((item)=>{
					item.checked = false;
					isAllChecked.value = false;
					checkedNumber.value = 0;
				})
			}
		}
		function relearnOrSkilledWord(method){
			if(!canPress.value){
				ElMessage({
					type: 'warning',
					message: 'Need to select at lease one word',
					grouping:true,
				})
				return;
			}
			let wordArr= [];
			checkBoxRef.value.forEach((item,index)=>{
				if(item.checked){
					wordArr.push(wordList.value[index]);
				}
			})
			if(method == 'relearn'){
				doPostJson("/v1/setting/relearn-word", wordArr).then((response) => {
					if(response.data.code == 1000){
						ElMessage({
							type: 'success',
							message: 'Successfully relearn',
							grouping:true,
						})
					}
				}).catch((err) => {
					
				});
			}else{
				doPostJson("/v1/setting/skilled-word", wordArr).then((response) => {
					if(response.data.code == 1000){
						ElMessage({
							type: 'success',
							message: 'Successfully skilled',
							grouping:true,
						})
					}
				}).catch((err) => {
					
				});
			}

			
		}
        function playAudio(type, word){
            console.log(audioRef);
            audioRef.value[0].src = 'https://dict.youdao.com/dictvoice?type='+type+'&audio='+word;
            audioRef.value[0].play();
        }

		function clickCheckBox(event, item, index){
			if(checkBoxRef.value[index].checked){
				checkedNumber.value++;
			}else{
				checkedNumber.value--;
			}
		}
        return {
            wordList,
            parseDate,
            audioRef,
            playAudio,
            showEye,
			showEdit,
			wordListUlRef,
			itemRef,
			isFadeIn,
			checkBoxAnimationClass,
			checkBoxRef,
			chooseAllOrNot,
			isAllChecked,
			clickCheckBox,
			isSomeChecked,
			checkedNumber,
			canPress,
			relearnOrSkilledWord
        }
    }
}
</script>

<style scoped>
	@import "@/assets/css/animation.css";

	.wordListVueDiv{
		display: flex;
		flex-direction: column;
		align-items: center;
	}
	.canPressClass{
		color:#f74f4f;
		cursor: pointer;
	}
	.canNotPressClass{
		color: grey;
		cursor: not-allowed;
	}

	.editDetailManipulateDiv{
		width: 102.4rem;
		height: 7rem;
		position: absolute;
		left : 8rem;
		top:45.7rem;
		background-color: #F0CECE;
		z-index: 1;
		display: flex;
		justify-content: space-between;
		padding-left: 10rem;
		padding-right: 10rem;
		align-items: center;
		font-size : 2rem;
	}

    .definitionP{
        font-size: 1.5rem;
    }
    .wordListUl{
        display: flex;
        flex-direction: column;
		align-items: center;
    }
    .wordDetailLi{
        display: flex;
        flex-wrap: wrap;
		align-items: center;
        font-size: 1.8rem;
        margin-top: 2rem;
    }
    .translationDiv{
        width: 20rem
    }
    .definitionDiv{
        width:20rem
    }
    .wordDiv{
        width: 10rem;
    }

    .lineVue{
        width:100%;
        border: 0.1rem white solid;
        margin-top: 0.8rem;
		position: relative;

    }
    .audioButtonDiv{
        width: 5rem;
        display: flex;
        justify-content: space-between;
    }
    .audioButtonDiv p:hover{
        color:  #f59a9a;
        cursor: pointer;
    }

    .template:hover{
        color:  #f59a9a;
        cursor:default;
    }
	.template{
		width:10rem
	}


	.item{
		margin-left: 0rem;
  		transition: transform  0.5s ease; /* 添加过渡效果到 margin 属性 */
	}
	.wordDetailInfoDiv{
		width: 100rem;
		display: flex;
		justify-content: space-around;
	}

	.checkBoxInput{
		position: relative;
		left : 3rem
	}

	.editDetailButtonDiv{
		width: 90%;
		height: 4rem;
		display: flex;
		justify-content: space-between;
		font-size : 2rem;
		color:#f74f4f;
		align-items: center;
		padding-left: 3rem;
		
	}
	.editDetailButtonDiv div:hover{
		cursor:pointer;
	}

	

	
</style>