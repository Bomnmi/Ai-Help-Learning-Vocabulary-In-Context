import {doPost, doGet} from '@/assets/utils/axios-util.js';
import { useRouter } from 'vue-router';
export default{
    namespaced: true,
    actions:{
        getParagraph(context, userId){
            doGet("/v1/article/get-paragraphs", {
                userId
            }).then((response) => {
                let responseData = response.data;
                context.commit('UPDATE_ARTICLE', responseData)
            }
            ).catch((err) => {
                console.log(err);
            });
        },
        getLemmatizationWordList(context, paragraphId){
            doGet("v1/article/get-lemmatization-word-list",{
                paragraphId : paragraphId,
            }).then((response) => {
                let responseData = response.data;
                context.commit('UPDATE_LEMMATIZATION_SET', responseData);
            }).catch((err) => {
                console.log(err);
            });
        },

        getLemmatizationWord(context, word){
            return new Promise(resolve=>{
                doGet("/v1/article/get-lemmatization-word",{
                    word
                }).then((response) => {
                    context.commit('UPDATE_LEMMATIZATION_WORD', response.data);
                    resolve();
                }).catch((err) => {
                    console.log(err);
                });
            })
        },
        getTranslationContent(context, paragraphId){
            doGet("/v1/article/get-translation-article",{
                paragraphId
            }).then((response) => {
                context.commit('UPDATE_TRANSLATED_ARTICLE', response.data)
            }).catch((err) => {
                
            });
        },
        finishReading(context, params){
            doPost("/v1/article/finish-reading",{
                userId : params.userId,
                paragraphId: params.paragraphId
            }).then((response) => {
                console.log(response);
                if(response.data.code == 1000){
                    context.dispatch("getParagraph", params.userId)
                }
            }).catch((err) => {
                
            });
        }
    },
    mutations:{
        UPDATE_ARTICLE(state, responseData){
            state.articleInfo.articles = responseData.paragraphs;
            state.articleInfo.wordList = responseData.wordList;
            state.articleInfo.booksNumber = responseData.paragraphs.length;
        },

        UPDATE_LEMMATIZATION_SET(state, responseData){
            //Reset lemmatizationAddedWordSet
            state.lemmatizationAddedWordSet = new Set();
            responseData.data.forEach((word) => {
                state.lemmatizationAddedWordSet.add(word);
            })
        },

        UPDATE_LEMMATIZATION_WORD(state, responseData){
            state.lemmatizationWord = responseData.data;
        },
        UPDATE_TRANSLATED_ARTICLE(state, responseData){
            state.translatedContent = responseData.data;
        },
        CLEAR_TRANSLATED_ARTICLE(state, value){
            state.translatedContent = value
        },
        
        DELETE_WORD_FROM_LEMMATIZATION_SET(state, value){
            state.lemmatizationAddedWordSet.delete(value);
        },
		DELETE_LEMMATIZATION_SET(state){
			state.lemmatizationAddedWordSet = new Set();
		}
        
    },
    state:{
        articleInfo:{
            articles:[],
            wordList:[],
            booksNumber:0,  
        },
        lemmatizationAddedWordSet : new Set(),
        lemmatizationWord: "",
        translatedContent: "",

    },
}