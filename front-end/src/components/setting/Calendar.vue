<template>
	<div class="settingPage fontType">
		<div class="calandarDiv">
			<div class="calandarContentDiv">
				<div class="calandarTitle">
					<div class="arrowDiv">
						<LeftArrow2 @click="chooseMonth(new Date(curDate.getFullYear(), curDate.getMonth() - 1, 1))"/>
					</div>
					<div class="calandarTitleFontDiv">
						{{curMonthEng}}&nbsp;{{curYear}}
					</div>
					<div class="arrowDiv">
						<RightArrow2 v-if="curDate.getMonth() != initialDate.getMonth()" @click="chooseMonth(new Date(curDate.getFullYear(), curDate.getMonth() + 1, 1))"/>
					</div>
				</div>
				<div class="calandarWeekday">
					<span class="calandarWeekdaySpan">Mon</span>
					<span class="calandarWeekdaySpan">Tue</span>
					<span class="calandarWeekdaySpan">Wed</span>
					<span class="calandarWeekdaySpan">Thu</span>
					<span class="calandarWeekdaySpan">Fri</span>
					<span class="calandarWeekdaySpan">Sat</span>
					<span class="calandarWeekdaySpan">Sun</span>
				</div>
				<div class="calandarDate">
					<span class="calandarDateSpan" 
					:class="setItemClass(item, index)"
					v-for="(item, index) in date" :key="index">
						{{item}}
					</span>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import {getNumberOfDays, getWeekday, parseDate} from "@/assets/utils/util-method.js"
import { computed, onMounted, reactive, ref } from 'vue';
import {doPost, doGet} from '@/assets/utils/axios-util.js';
import LeftArrow2 from '../svg/LeftArrow2.vue';
import RightArrow2 from '../svg/RightArrow2.vue';
export default {
	name : "Calandar",
	components:{LeftArrow2, RightArrow2},
	setup(){
		//get the record of user login
		onMounted(()=>{
			doGet("/v1/setting/get-record", {
				userId : userInfo.id
			}).then((response) => {
				if(response.data.code == 1000){
					response.data.data.forEach((record)=>{
						loginDate.add(parseDate(new Date(record.loginTime)));
					});
				}
			}).catch((err) => {
				console.log(err);
			});
		});

		const userInfo = JSON.parse(localStorage.getItem("userInfo"));
		let monthEnglish = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
		var curDate = ref(new Date());
		let initialDate = new Date(curDate.value);
		let curMonthEng = computed(()=>{
			return monthEnglish[curDate.value.getMonth()]
		});
		let curMonth = computed(()=>{
			return curDate.value.getMonth() + 1;
		});
		let curYear = computed(()=>{
			return curDate.value.getFullYear();
		});
		let date = computed(()=>{
			var tempArr = new Array();
			var firstWeekDay = getWeekday(curYear.value,  curMonth.value, 1);
			firstWeekDay = firstWeekDay == 0 ? 7 : firstWeekDay;
			var lastMonthDays = getNumberOfDays(curYear.value, curMonth.value - 1);
			var thisMonthDays = getNumberOfDays(curYear.value, curMonth.value);
			for(var i = 0; i < firstWeekDay - 1; i++){
				tempArr.unshift(lastMonthDays - i);
			}
			for(var i = 1; i <= thisMonthDays; i++){
				tempArr.push(i);
			}
			for(var i = 1; tempArr.length % 7 != 0; i++){
				tempArr.push(i);
			} 
			return tempArr;
		});
		let loginDate = reactive(new Set());

		function setItemClass(item, index){
			let returnClassName = "";
			let date = new Date();
			if((index - item) < -1){
				date.setFullYear(curYear.value, curMonth.value - 2, item);
				returnClassName += "calandarDateTransparent "
				if(loginDate.has(parseDate(date))){
					returnClassName += "calandarDateHighLight"
				}
			}else if((index - item) > 7){
				date.setFullYear(curYear.value, curMonth.value, item);
				returnClassName += "calandarDateTransparent "
				if(loginDate.has(parseDate(date))){
					returnClassName += "calandarDateHighLight"
				}
			}else{
				date.setFullYear(curYear.value, curMonth.value - 1, item);
				if(loginDate.has(parseDate(date))){
					returnClassName += "calandarDateHighLight"
				}
			}
			return returnClassName;		
		}
		function chooseMonth(date){
			curDate.value = date;
		}

		return{
			curMonthEng,
			curYear,
			date,
			curMonth,
			setItemClass,
			curDate,
			chooseMonth,
			initialDate
		}
	}

}
</script>

<style scoped>
  	@import "@/assets/css/main_page.css";
	@import "@/assets/css/font.css";
	.settingPage{
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.calandarDiv{
		width: 100rem;
		height:50rem;
		background: linear-gradient(268deg, rgba(253.52, 142.28, 142.28, 0.50) 9%, #FFB4B4 41%, rgba(252.01, 104.37, 104.37, 0.10) 100%);
		position: relative;
		top: 3rem;
		border-radius: 4rem;
	}
	.calandarContentDiv{
		width: 100%;
		height: 100%;
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
	}
	.calandarTitle{
		display: flex;
		justify-content: center;
		align-items: center;
		width: 80rem;
		height: 8rem;
		font-size: 4rem;	
	}
	.calandarTitleFontDiv{
		width: 20rem;
		text-align: center;
	}
	.arrowDiv{
		width: 3rem;
		height: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-left : 2rem;
		margin-right : 2rem;
		
	}
	.calandarWeekday{
		width: 80rem;
		height: 3rem;
		display: flex;
		justify-content: space-between;
		align-items: center;
		color: rgba(60, 60, 67, 0.60);
		
	}
	.calandarWeekdaySpan{
		width: 14.28%;
		display: flex;
		justify-content: space-around;
		align-items: center;
		font-size: 1.9rem;
	}
	.calandarDateSpan{
		width: 14.28%;
		display: flex;
		justify-content: space-around;
		align-items: center;
		font-size: 1.9rem;
	}

	.calandarDateTransparent{
		color: rgba(60, 60, 67, 0.60);
	}

	.calandarDateHighLight{
		color: yellow;
	}

	.calandarDate{
		width: 80rem;
		height: 33rem;
		display: flex;
		flex-wrap: wrap;
		
	}
</style>