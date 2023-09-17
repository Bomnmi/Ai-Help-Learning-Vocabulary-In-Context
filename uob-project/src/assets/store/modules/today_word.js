export default{
    namespaced: true,
    actions:{
        
    },
    mutations:{
        addWordIntoWordList(state, params){
            if(!state.wordMap.has(params.word)){
                state.wordList.push(params.word);
                state.wordMap.set(params.word,{
					translation: params.translation,
					definition: params.definition,
				});
                return;
            }
        },
        addWordsIntoWordList(state, value){
            //Reset the word list
            state.wordList = new Array();
            state.wordMap = new Map();
            value.forEach((word)=>{
                state.wordMap.set(word.word, {
					translation: word.translation,
					definition: word.definition,
				});
                state.wordList.push(word.word);
            })
			console.log(state.wordMap);
			
        }
    },
    state:{
        wordList : '',
        wordMap : '',

    },
}