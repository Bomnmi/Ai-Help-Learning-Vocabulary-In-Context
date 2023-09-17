import {doPost, doGet} from '@/assets/utils/axios-util.js';
import { ElMessage } from 'element-plus'
export default{
    namespaced: true,
    actions:{
        getDefinition(context, word){
            return new Promise((resolve)=>{
                if(context.state.wordDefinitionMap.has(word)){
                    context.commit("UPDATE_DEFINITION", word)
                    resolve();
                }else{
                    doGet("/v1/article/get-translation-word", {
                        word
                    }).then((response) => {
                        let responseData = response.data;
                        context.commit('UPDATE_DEFINITION_MAP',{
                            word,
                            responseData
                        });
                        resolve();
                    }).catch((err) => {
                        console.log(err);
                    });
                }
            })
        },
        updateNextReviewTime(context, params){
            console.log(params);
            doPost("/v1/article/update-next-review-time",{
                userId: params.userId,
                word : params.word,
                quality : params.quality
            }).then((response) => {
                console.log(response.data);
                if(response.data.code == 1000){
                    context.commit("articleParam/DELETE_WORD_FROM_LEMMATIZATION_SET", params.word, {root:true})
					ElMessage({
                        type: 'success',
                        message: 'Successfully review!',
                        grouping:true,
                    })
                }
            }).catch((err) => {
                
            });
        },
		updateTodayWordListMap(context, params){
			console.log(params);
			context.commit("todayWord/addWordIntoWordList", {
				word: params.word,
				translation: params.translation,
				definition: params.definition
			}, {root:true})
		}
    },
    mutations:{
        UPDATE_DEFINITION(state, word){
            state.trigger = !state.trigger;
            state.wordDefinition = state.wordDefinitionMap.get(word).definition;
            state.wordTranslation = state.wordDefinitionMap.get(word).translation;
        },
        UPDATE_DEFINITION_MAP(state, param){
            let responseData = param.responseData;
            console.log(responseData);
            let word = param.word;
            if(responseData.data != null){
                state.wordDefinitionMap.set(word, responseData.data);
                state.wordDefinition = state.wordDefinitionMap.get(word).definition;
                state.wordTranslation = state.wordDefinitionMap.get(word).translation;
            }else{
                state.wordDefinition = []
                state.wordTranslation = '';
            }
            state.trigger = !state.trigger;
        }
    },
    state:{
        wordDefinition: [],
        wordTranslation:'',
        wordDefinitionMap: new Map(),
        trigger: false,
    },
}