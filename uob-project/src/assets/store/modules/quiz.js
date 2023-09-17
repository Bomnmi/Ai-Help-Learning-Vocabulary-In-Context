export default{
    namespaced: true,
    actions:{
        
        
    },
    mutations:{
        UPDATE_QUIZ_PROBLMES(state, value){
			state.quizProblems = value;
		},
		UPDATE_HISTORY_QUIZ(state, value){
			state.historyQuiz = value;
		}
        
    },
    state:{
       quizProblems:[],
	   historyQuiz:[],
    },
} 