<template>

    <div class="todayWordListDiv fontType">
        <div class="todayWordListTitleDiv">
            <span>Today's Words</span>
            <Line length="22"/>
        </div>
        <div class="todayWordListListDiv">
            <el-scrollbar height="40rem">
                <ul class="wordList" >
                    <li class="wordItem" v-for="(word, index) in $store.state.todayWord.wordList" :key="index">
						<el-popover
							placement="left"
							:title="word.word"
							width="20rem"
							trigger="hover"
							>
							<template #reference>
								<p>{{word}}</p>
							</template>
							<slot v-if="!lemmatizationWordSet.has(word)" content><p v-for="(translation,index) in $store.state.todayWord.wordMap.get(word).translation.split('\\')" :key="index">
								{{translation}}</p>
							</slot>
						</el-popover>
                        <!-- <div>I am definition</div> -->
                    </li>
                </ul>
            </el-scrollbar>
        </div>
    </div>
  
</template>
<script>
import { computed, onMounted, ref, watch } from 'vue'
import Line from '../common/Line.vue'
import {doPost, doGet} from '@/assets/utils/axios-util.js';
import {useStore} from 'vuex'
export default {
    name : "TodayWordList",
    props:["word"],
    components : {Line},
    setup(){
        //userInfo
        let userInfo = JSON.parse(window.localStorage.getItem("userInfo"));
        const store = useStore();
		let lemmatizationWordSet = computed(()=>{
			return store.state.articleParam.lemmatizationAddedWordSet;
		})
        onMounted(()=>{
            doGet("/v1/article/get-today-words", {
                userId : userInfo.id
            }).then((response) => {
                //get today's word
                let responseData = response.data;
                store.commit("todayWord/addWordsIntoWordList", responseData.data)
				console.log(responseData.data);
            }).catch((err) => {
                
            });
        })
		return{
			lemmatizationWordSet
		}
    }
}
</script>

<style scoped>
	@import "@/assets/css/font.css";
    .todayWordListDiv{   
        flex:1 1 0;
        display: flex;
        flex-direction: column;
        
    }
    .todayWordListTitleDiv{
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }
    .todayWordListTitleDiv span{
        color: black;
        font-size: 2.3rem; 
        font-weight: 400; 
    }
    .wordList{
        list-style-type: none;
        padding-left: 2rem;
        padding-right: 2rem;
		
    }
    li{
        padding-top: 1rem;
        color: black;
        font-size: 1.8rem; 
        word-break: break-all;
        display:flex;
        flex-direction: column;
        border-radius: 4px;
		text-align: center;
    }
    li div{
        flex:1 1 0;
        white-space: normal;
    }
	li:hover{
		cursor: pointer;
		color:  #f59a9a;
	}
</style>