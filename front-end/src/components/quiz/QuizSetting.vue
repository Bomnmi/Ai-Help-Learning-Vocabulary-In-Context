<template>
	<div class="quizTitleDiv"><Title title="Quiz"/></div>
	<div class="quizSettingSelectorDiv fontType">
		<div>How many words want to test:</div>&nbsp;
		<el-tooltip
			:visible='maxNumberOfWordsCouldSelected < 10'
			class="box-item"
			effect="dark"
			:content="numberOfWordsHintContent"
			placement="top-start"
		>
			<el-input-number 
			:disabled="maxNumberOfWordsCouldSelected < 10"
			placeholder="0"
			:min="maxNumberOfWordsCouldSelected < 10 ? -1 : 10" 
			v-model="numberOfWordsSelected"
			:max="maxNumberOfWordsCouldSelected > 100 ? 100 : maxNumberOfWordsCouldSelected"/>
		</el-tooltip>
	</div>
	<div class="quizSettingSelectorDiv fontType">
		<div>What word set want to test:</div>&nbsp;
		<el-select v-model="datasetOption" placeholder="Select" size="large">
			<el-option v-for="item in datasetOptions" :key="item.value" :value="item.value" :label="item.label"/>
		</el-select>
	</div>
	<div class="quizSettingSelectorDiv fontType">
		<div>Which pattern want to use:</div>&nbsp;
		<el-select v-model="patternOption" placeholder="Select" size="large">
			<el-option :disabled="item.disabled" v-for="item in patternOptions" :key="item.value" :value="item.value" :label="item.label"/>
		</el-select>
	</div>
	<Button @click="clickQuizStartButton" btnName="Start" fontType="fontType" fontSize="3" width="20rem"/>
</template>

<script>
import { computed, onMounted, reactive, ref, toRef, toRefs, watch } from 'vue';
import Button from '../../components/login/Button.vue'
import {doGet} from '@/assets/utils/axios-util.js';
import Title from '../main_page/Title.vue';
import {ElMessage } from 'element-plus'
import {doPostJson} from '@/assets/utils/axios-util.js';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
export default {
	name : 'QuizSetting',
	components:{Button,Title},
	setup(){
		const store = useStore();
		onMounted(()=>{
			doGet("/v1/setting/get-all-words", {
				userId: userInfo.id
			}).then((response) => {
				if(response.data.code == 1000){
					quizSettingInfo.totalVocabularyList = [...response.data.data];
				}
			}).catch((err) => {
				
			});

			doGet("/v1/article/get-today-words", {
                userId : userInfo.id
			}).then((response) => {
				//get today's word
				if(response.data.code == 1000){
					quizSettingInfo.todayVocabularyList = [...response.data.data];
				}
			}).catch((err) => {
				
			});

			doGet("/v1/quiz/get-history-quiz", {
				userId : userInfo.id
			}).then((response) => {
				if(response.data.code == 1000){
					store.commit("quiz/UPDATE_HISTORY_QUIZ", response.data.data);
				}
			}).catch((err) => {
				
			});
		});
		const userInfo = JSON.parse(localStorage.getItem('userInfo'));
		const router = useRouter();
		let quizSettingInfo = reactive({
			totalVocabularyList : new Array(11),
			todayVocabularyList : new Array(11),
		});
		let numberOfWordsSelected = ref(0);
		let datasetOption = ref('');
		let datasetOptions = reactive([
			{
				value : 0,
				label : 'Today words',
			},
			{
				value : 1,
				label : 'All words'
			}
		]);
		let patternOption = ref('');
		let patternOptions = reactive([
			{
				value : 0,
				label : 'Randomly'
			},
			{
				value : 1,
				label : 'Based on add time',
			},
			{
				value : 2,
				label : 'Based on review times',
			},
			{
				value : 3,
				label : 'Based on difficulty level',
			},
		]);

		let maxNumberOfWordsCouldSelected = computed(()=>{
			if(datasetOption.value == 0){
				return quizSettingInfo.todayVocabularyList.length;
			}else if(datasetOption.value == 1){
				return quizSettingInfo.totalVocabularyList.length;
			}
		})

		let numberOfWordsHintContent = computed(()=>{
			console.log(datasetOption.value === 0 || datasetOption.value === '');
			if(datasetOption.value === ''){
				return "Please select a word set."
			}else if(maxNumberOfWordsCouldSelected.value < 10){
				return "The number of words is not enough to generate a quiz."
			}
		})

		watch(datasetOption, (newValue)=>{
			if(newValue === 0 || newValue === ''){
				patternOptions.forEach(pattern => {
					if(pattern.value != 0){
						pattern.disabled = true;
					}
				})
				patternOption.value = 0;
				console.log(patternOptions);
			}else if(newValue === 1){
				patternOptions.forEach(pattern => {
					if(pattern.value != 0){
						pattern.disabled = false;
					}
				})
			}
		})

		
		
		function clickQuizStartButton(){
			//send request to generate a quiz and store the quiz in session
			if(maxNumberOfWordsCouldSelected.value < 10){
				ElMessage({
					type: 'warning',
					message: 'The word number is less than 10',
					grouping:true,
				});
				return;
			}
			if(datasetOption.value === ''){
				ElMessage({
					type: 'warning',
					message: 'Need to choose a dataset',
					grouping:true,
				});
				return;
			}
			console.log(patternOption.value);
			if(patternOption.value === ''){
				ElMessage({
					type: 'warning',
					message: 'Need to choose a pattern',
					grouping:true,
				});
				return;
			}
			//package quizSetting
			let quizSetting = {
				datasetSetting : datasetOption.value,
				patternSetting : patternOption.value,
				numberOfWords : numberOfWordsSelected.value
			};
			
			doPostJson("/v1/quiz/set-quiz?userId=" + userInfo.id, quizSetting).then((response) => {
				if(response.data.code == 1000){
					//store the quiz problems into local storage
					localStorage.setItem("quizProblems", JSON.stringify(response.data.data));
					//jump to the quizProblem page
					router.push("/quiz/quiz-problem/0");
				}
			}).catch((err) => {
				
			});
		}

		
		return{
			datasetOption,
			datasetOptions,
			patternOptions,
			patternOption,
			...toRefs(quizSettingInfo),
			numberOfWordsSelected,
			maxNumberOfWordsCouldSelected,
			numberOfWordsHintContent,
			clickQuizStartButton
		}
	}
}
</script>

<style scoped>
	@import "@/assets/css/font.css";
	.fontType{
		font-size: 2rem;
	}
	.quizTitleDiv{
		width: 100rem;
	}
	.quizSettingSelectorDiv{
		width: 70rem;
		height: 10rem;
		display: flex;
		align-items: center;
		justify-content: space-around;
	
	}
	.quizSettingSelectorDiv div{
		width: 27rem;
	}	

</style>