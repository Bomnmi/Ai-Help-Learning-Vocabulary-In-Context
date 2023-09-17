<template>
	<div class="quizPannelTitle fontType">
		<span>{{pannelName[$route.params.quizPageName]}}</span>
		<Line length="22"/>
		<el-scrollbar height="46rem">
			<ul class="questionList" v-if="$route.params.quizPageName == 'quiz-problem'">
				<li class="questions" @click="$router.push('/quiz/quiz-problem/' + index)" 
				:class="$route.params.quizIndex == index ? 'highLight' : 
				(item.answer != '' ? (item.answer == item.correctWord ? 'correct':'wrong') 
				:'') " v-for="(item, index) in quizProblems" :key="index">
					{{'Question ' + (index + 1)}}
				</li>
			</ul>

			<ul class="questionList" v-if="$route.params.quizPageName == 'quiz-setting'">
				<li class="historyQuiz" v-for="(item, index) in historyQuiz" :key="index">
					<span>
						{{parseDate(new Date(item.createTime))}}
					</span>
					<span>
						{{item.score + '%'}}
					</span>
					<Line length="20"/>
				</li>
			</ul>
		</el-scrollbar>
    </div>
</template>

<script>
import { computed, onMounted, ref } from 'vue'
import Line from '../common/Line.vue'
import { useStore } from 'vuex'
import {doGet} from '@/assets/utils/axios-util.js';
import {parseDate} from "@/assets/utils/util-method.js"
export default {
	name : "QuizPannel",
	components: {Line},
	setup(){
		const store = useStore();
	
		let pannelName = ref({
			"quiz-problem" : 'Questions',
			"quiz-setting" : 'History Quiz'
		});
		let quizProblems = computed(()=>{
			return store.state.quiz.quizProblems;
		});

		let historyQuiz = computed(()=>{
			return store.state.quiz.historyQuiz;
		});

		return{
			pannelName,
			quizProblems,
			historyQuiz,
			parseDate
		}
	}
}
</script>

<style scoped>
	@import "@/assets/css/font.css";
	.quizPannelTitle{
		text-align: center;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.highLight{
		background-color : #f59a9a;
		border-radius: 1.2rem;
	}
	.correct{
		background-color : rgba(86, 222, 86, 0.781);
		border-radius: 1.2rem;
	}
	.wrong{
		background-color : red;
		border-radius: 1.2rem;
	}

	.questionList{
		list-style-type: none;
		padding-left : 2rem;
		padding-right : 2rem;
		font-size: 1.7rem;
	}
	.questions{
		margin-top: 1rem;
		width: 20rem;
		height: 4rem;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.questions:hover{
		cursor: pointer;
	}

	.historyQuiz{
		margin-top: 1rem;
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
	}

</style>