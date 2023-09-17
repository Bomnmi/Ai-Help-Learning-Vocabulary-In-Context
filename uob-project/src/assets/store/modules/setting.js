import {doPost, doGet} from '@/assets/utils/axios-util.js';
import { getDateDiff, compareDate, parseMonth, compareMonth} from '../../utils/util-method';
export default{
    namespaced: true,
    actions:{
        getAllWords(context, value){
			doGet("/v1/setting/get-all-words",{
				userId: value
			}).then((response) => {
				if(response.data.code == 1000){
					context.commit("UPDATE_WORD_LIST", response.data.data);
					context.commit("UPDATE_TOTAL_WORD_LIST", response.data.data);
				}
			}).catch((err) => {
				console.log(err);
			});
			
        },
		getRecentWords(context, value){
			if(context.state.recentWordList.length == 0){
				var tempList = [];
				context.state.totalWordList.forEach(word =>{
					if(getDateDiff(word.addTime, new Date()) <= 7){
						tempList.push(word);
					}
				})
				context.commit("UPDATE_WORD_LIST", tempList);
				context.commit("UPDATE_RECENT_WORD_LIST", tempList);
			}else{
				context.commit("UPDATE_WORD_LIST", context.state.recentWordList);
			}
		}
    },
    mutations:{
        UPDATE_WORD_LIST(state, value){
            state.wordList = value;
        },
        UPDATE_SHOW_EYE(state, value){
            state.showEye = value;
        },
		UPDATE_TOTAL_WORD_LIST(state,value){
			state.totalWordList = [];
            value.forEach((word) => {
                word.addTime = new Date(word.addTime);
                state.totalWordList.push(word);
            })
		},
		UPDATE_RECENT_WORD_LIST(state, value){
			state.recentWordList = value;
		},
		UPDATE_SHOW_EDIT(state,value){
			if(value == true){
				state.showEdit = value;
				state.checkBoxAnimationClass = 'fadeInLeft'
			}else{
				state.checkBoxAnimationClass = 'fadeOutLeft'
				window.setTimeout(()=>{
					state.showEdit = value;
				}, 0);
			}
		},
		
		UPDATE_USER_RECORD(state,value){
			state.userRecord = value;
		},

		UPDATE_USER_RECORD_BY_TIME(state, value){
			let result = [];
			let startIndex = 0;
			let userRecord = state.userRecord;
			if(value == "weekly"){
				//split the userRecord by week
				while(startIndex < userRecord.length){
					let temp = [];
					let firstDate = new Date(userRecord[startIndex].loginTime);
					temp.push(userRecord[startIndex]);
					startIndex++;
					let lastDate = new Date(firstDate.getFullYear(), firstDate.getMonth(), firstDate.getDate() - 7);
					for(var i = startIndex; i < userRecord.length; i++){
						let curDate = new Date(userRecord[i].loginTime);
						if(compareDate(curDate, lastDate) >= 0){
							temp.push(userRecord[i]);
						}else{
							startIndex = i;
							break;
						}
					}
					result.push(temp);
				}
				state.userRecordWeekly = result;	
			}else if(value == "monthly"){
				//split the userRecord by month
				result = {};
				let firstDate = new Date(userRecord[startIndex].loginTime);
				let numberOfVocabularies = 0;
				let numberOfLearningTime = 0;
				for(var i = startIndex; i < userRecord.length; i++){
					let curDate = new Date(userRecord[i].loginTime);
					if(compareMonth(curDate, firstDate)){
						numberOfVocabularies += userRecord[i].wordNumber;
						numberOfLearningTime += userRecord[i].learningTime;
						
						if(i == userRecord.length - 1){
							result[parseMonth(firstDate)] = {
								"vocabulary": numberOfVocabularies,
								"learningTime": numberOfLearningTime
							}
						}
						
					}else{
						result[parseMonth(firstDate)] = {
							"vocabulary": numberOfVocabularies,
							"learningTime": numberOfLearningTime
						}
						numberOfLearningTime = 0;
						numberOfVocabularies = 0;
						let monthDiff = firstDate.getMonth() - curDate.getMonth();
						console.log(monthDiff);
						for(var j = 1; j < monthDiff; j++){
							let tempDate = new Date(firstDate.getFullYear(), firstDate.getMonth() - j);
							result[parseMonth(firstDate)] = {
								"vocabulary": numberOfVocabularies,
								"learningTime": numberOfLearningTime
							}
						}
						firstDate = new Date(curDate);
						numberOfVocabularies += userRecord[i].wordNumber;
						numberOfLearningTime += userRecord[i].learningTime;

						if(i == userRecord.length - 1){
							result[parseMonth(firstDate)] = {
								"vocabulary": numberOfVocabularies,
								"learningTime": numberOfLearningTime
							}
						}
					}
				}
				state.userRecordMonthly = result;
				/*[{firstMonth:
					{
						vocabulary : 30, 
						learningTime : 40
					}
				},]
				*/
			}
		},
    },
    state:{
		totalWordList:[],
		recentWordList:[],
        wordList: [],
        showEye: false,
		showEdit: false,
		checkBoxAnimationClass: '',
		userRecord: [],
		userRecordWeekly:[],
		userRecordMonthly:{},
    },
}