<template>
	<div class="quizTitleDiv"><Title title="Quiz Problem"/></div>
	<div class="quizProblemDifinitionDiv">
		<div class="arrowDiv"
			@click="problemDefinitionIndex > 0 ? problemDefinitionIndex-- : ''" >
			<LeftArrow v-show="problemDefinitionIndex > 0"/> 
		</div>
		{{problemDefinition[problemDefinitionIndex]}}
		<div class="arrowDiv" 
			@click="problemDefinitionIndex < problemDefinition.length - 1 ? problemDefinitionIndex++ : ''">
			<RightArrow v-show="problemDefinitionIndex < problemDefinition.length - 1
			"/>
		</div>
	</div>
	<div class="quizOptionsDiv">
		<div class="quizOptionDiv" v-for="(word,index) in quizProblem.vocabularyList" :key="word.word">
			<div :ref="el => setRefs(el, index)" @click="clickOneWord(word.word, index)" class="quizOptionButtonDiv">
				{{word.word}}
			</div>
			<div class="quizOptionHint">
				<p v-show="quizOptionShowHint" :class="quizProblem.correctWord == word.word ? 'correct' : 'wrong'"
				>{{quizProblem.correctWord == word.word ? 'correct' : (quizProblem.answer == word.word ? 'choosed' : 'wrong')}}</p>
			</div>
		</div>
	</div>
	<Button v-if="answeredProblem < quizProblems.length" @click="nextWord" btnName='Next' fontSize="3" fontType="fontType" />
	<Button v-else @click="submitQuiz" btnName='Submit' fontSize="3" fontType="fontType" />
	
</template>

<script>
import { computed, nextTick, onMounted, onUpdated, reactive, ref, watch } from 'vue';
import Button from '../login/Button.vue'
import Title from '../main_page/Title.vue'
import RightArrow from '../svg/RightArrow.vue';
import LeftArrow from '../svg/LeftArrow.vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import {doPost,doPostJson} from '@/assets/utils/axios-util.js';
import { useStore } from 'vuex';


export default {
	name : "QuizProblem",
	components: {Button, Title,RightArrow,LeftArrow},
	setup(){
		const store = useStore();
		onMounted(()=>{
			console.log("onMounted");
			store.commit("quiz/UPDATE_QUIZ_PROBLMES",JSON.parse(localStorage.getItem("quizProblems")));
			mountedRouteChangeFlag.value = !mountedRouteChangeFlag.value;
		})
		let route = useRoute();
		
		let quizProblems = computed(()=>{
			//return store.state.quiz.quizProblems;
			return JSON.parse(localStorage.getItem("quizProblems"));
		})
		let quizProblem = computed(()=>{
			return quizProblems.value[route.params.quizIndex];
		});
		const router = useRouter();

		let problemDefinition = computed(()=>{
		
			return quizProblem.value.problemDefinition.split("\\");
		})
		let problemDefinitionIndex = ref(0);

		const itemRefs = ref([]);

		const setRefs = (el, index)=>{
			itemRefs.value[index] = el;
		}
		let chooseWord = ref('');
		let chooseWordIndex = ref(-1);

		let answeredProblem = ref(0);

		let mountedRouteChangeFlag = ref(false);

		watch(mountedRouteChangeFlag, ()=>{
			if(route.name == "quiz" && route.path.indexOf("/quiz-problem") != -1){
				chooseWord.value = '';
				chooseWordIndex.value = -1;
				quizOptionShowHint.value = false;
				problemDefinitionIndex.value = 0;
				answeredProblem.value = 0;
				quizProblems.value.forEach(problem => {
					if(problem.answer != ''){
						answeredProblem.value++;
					}
				})
				if(quizProblem.value.answer != ''){
					nextTick(()=>{
						//show the correct answer and hint
						quizOptionShowHint.value = true;
						itemRefs.value.forEach((el,index)=>{
							if(el.innerText == quizProblem.value.answer){
								el.style.backgroundColor = "#f59a9a";
								chooseWordIndex.value = index;
							}else if(el.innerText == quizProblem.value.correctWord){
								el.style.backgroundColor = "#78EB76";
							}else{
								el.style.backgroundColor = "";
							}
						});
					})
				}
			}
		})
		watch(route, (newValue)=>{
			mountedRouteChangeFlag.value = !mountedRouteChangeFlag.value;
		})
		

		const userInfo = JSON.parse(localStorage.getItem("userInfo"));
		let quizOptionShowHint = ref(false);

		watch(chooseWordIndex, (newValue, oldValue)=>{
			if(oldValue != -1){
				itemRefs.value[oldValue].style.backgroundColor = "";
			}
			if(newValue != -1){
				itemRefs.value[newValue].style.backgroundColor = "#f59a9a";
			}
		})

		function clickOneWord(word, index){
			if(quizProblem.value.answer != ''){
				return;
			}
			chooseWord.value = word;
			chooseWordIndex.value = index;
		}

		function nextWord(){
			var quizIndex = Number(route.params.quizIndex) == quizProblems.value.length - 1 ? Number(route.params.quizIndex) : Number(route.params.quizIndex) + 1;
			if(quizProblem.value.answer != ''){
				router.push("/quiz/quiz-problem/" + quizIndex);
				return;
			}
			if(chooseWord.value == ""){
				ElMessage({
					type: 'warning',
					message: 'Please select a word',
					grouping:true,
				});
				return;
			}
			if(quizIndex == quizProblems.value.length - 1){
				answeredProblem.value++;
			}
			//update the 'answer' in quizProblem
			quizProblem.value.answer = chooseWord.value;
			if(chooseWord.value == quizProblem.value.correctWord){
				//to the next problem, and update 
				router.push("/quiz/quiz-problem/" + quizIndex);
			}else{
				//also need to reset this word
				itemRefs.value.forEach((el,index)=>{
					if(el.innerText == quizProblem.value.answer){
						el.style.backgroundColor = "#f59a9a";
						chooseWordIndex.value = index;
					}else if(el.innerText == quizProblem.value.correctWord){
						el.style.backgroundColor = "#78EB76";
					}else{
						el.style.backgroundColor = "";
					}
				});
				doPost("/v1/article/update-next-review-time", {
					userId : userInfo.id,
					word : quizProblem.value.correctWord,
					quality : 0,
				}).then((result) => {
					console.log(result);
				}).catch((err) => {
					
				});
			}
			quizOptionShowHint.value = true;
			quizProblems.value[route.params.quizIndex] = quizProblem.value;
			store.commit("quiz/UPDATE_QUIZ_PROBLMES", quizProblems.value);
			localStorage.setItem("quizProblems", JSON.stringify(quizProblems.value));
		}

		function submitQuiz(){
			//upload the quiz into database
			let wrongAnswer = 0;
			quizProblems.value.forEach((problem)=>{
				if(problem.answer != problem.correctWord){
					wrongAnswer++;
				}
			})
			console.log(wrongAnswer);
			let score = (1 - (wrongAnswer / quizProblems.value.length)) * 100;
			score = score.toFixed(4);
			console.log(score);
			doPost("/v1/quiz/add-quiz", {
				userId : userInfo.id,
				score : score
			}).then((result) => {
				if(result.data.code == 1000){
					localStorage.removeItem("quizProblems");
					router.push("/quiz/quiz-setting/-1")		
				}
			}).catch((err) => {
				
			});
			//localStorage.removeItem("quizProblems");
		}

	
		return {
			quizProblem,
			problemDefinition,
			problemDefinitionIndex,
			clickOneWord,
			setRefs,
			chooseWord,
			nextWord,
			quizOptionShowHint,
			quizProblems,
			answeredProblem,
			submitQuiz
		}
	}
}
</script>

<style scoped>
	.correct{
		color : green;
	}
	.wrong{
		color : red;
	}
	.quizTitleDiv{
		width: 100rem;
	}
	.quizProblemDifinitionDiv{
		width: 90rem;
		height: 10rem;
		border-radius: 2rem;
		box-shadow: 0rem 0rem 2rem rgba(0, 0, 0, 0.54);;
		display: flex;
		justify-content: space-between;
		align-items: center;
		text-align: center;
		padding-left: 2rem;
		padding-right: 2rem;
	}
	.arrowDiv{
		display: flex;
		align-content: center;
		width: 5rem;
	}

	.quizOptionsDiv{
		width: 90rem;
		height: 15rem;
		display: flex;
		flex-wrap : wrap;
	}
	.quizOptionDiv{
		width: 50%;
		height: 50%;
		display: flex;
		align-items: center;
		justify-content: center;

	}
	.quizOptionButtonDiv{
		width: 20rem;
		height: 5rem;
		background-color: #F0CECE; 
		border-radius: 2rem;
		display: flex;
		border: 0.50px solid black;
		justify-content: center;
		align-items: center;
	}

	.quizOptionButtonDiv:hover{
		background-color: #f59a9a;
		cursor: pointer;
	}

	.quizOptionHint{
		position: relative;
		left: 4rem;
		width: 10rem;
	}

	
</style>