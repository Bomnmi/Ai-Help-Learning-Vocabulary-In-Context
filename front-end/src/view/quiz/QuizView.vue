<template>
	<div>
		<PageHeader/>
		<div class="quizViewDiv">
			<div class="quizMainDiv mainPageColor fontType">
				<component :is='currentMainVue'/>
			</div>
			<div class="quizPannelDiv mainPageColor">
				<QuizPannel/>
			</div>
		</div>
	</div>
</template>

<script>
import { computed, reactive, ref } from 'vue'
import Button from '../../components/login/Button.vue'
import PageHeader from '../../components/main_page/PageHeader.vue'
import QuizSetting from '../../components/quiz/QuizSetting.vue'
import Title from '../../components/main_page/Title.vue'
import QuizPannel from '../../components/quiz/QuizPannel.vue'
import QuizProblem from '../../components/quiz/QuizProblem.vue'
import { useRoute } from 'vue-router'
import {parseUrlFormToVueName} from "@/assets/utils/util-method.js"


export default {
	name : "QuizView",
	components: {PageHeader,Button, QuizSetting,Title, QuizPannel, QuizProblem},
	setup(){
		let route = useRoute();
		
		let currentMainVue = computed(()=>{
			let quizPageName = route.params.quizPageName;
			return parseUrlFormToVueName(quizPageName)
		});
		return{
			currentMainVue
		}
	}
}
</script>

<style scoped>
	@import "@/assets/css/font.css";
	@import "@/assets/css/main_page.css";

	.quizTitleDiv{
		width: 100rem;
	}
	.quizViewDiv{
		width: 100%;
		height: 90%;
		position: absolute;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	.fontType{
		font-size: 2rem;
	}	

	.quizPannelDiv{
		width: 25rem;
		height: 52rem;
		display: flex;
		flex-direction: column;
		align-items: center;
		border-radius: 2rem;
	}
	.quizMainDiv{
		width: 110rem;
		height: 52rem;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-around;
		border-radius: 2rem;
	}


	
</style>