<template>
  	<div class="settingPage fontType">
		<div class="chartsDiv">
			<div class="chartDiv chartSize">
				<Button class="weeklyBtn buttonDiv" width="7rem" height="3rem" btnName="weekly"
				@click="getWeeklyData(new Date(weeklyUserRecord[0][0].loginTime), weeklyUserRecord[0], 'vocabulary', vocabularyChartOptions);"/>
				<Button class="monthlyBtn buttonDiv" width="7rem" height="3rem" btnName="monthly"
				@click="getMonthlyData(new Date(), 'vocabulary', vocabularyChartOptions);"/>
				<div id="vocabularyChart" class="chartSize"></div>
			</div>

			<div class="chartDiv chartSize">
				<Button class="weeklyBtn buttonDiv" width="7rem" height="3rem" btnName="weekly"
				@click="getWeeklyData(new Date(weeklyUserRecord[0][0].loginTime), weeklyUserRecord[0], 'learningTime', learningTimeChartOptions);"/>
				<Button class="monthlyBtn buttonDiv" width="7rem" height="3rem" btnName="monthly"
				@click="getMonthlyData(new Date(), 'learningTime', learningTimeChartOptions);"/>
				<div id="learningTimeChart" class="chartSize">
					
				</div>
			</div>
			
			
		</div>
		
 	 </div>
  
</template>

<script>
import * as echarts from "echarts";
import { computed, onMounted, onUnmounted, reactive, ref, watch } from 'vue';
import {doGet} from '@/assets/utils/axios-util.js';
import { useStore } from 'vuex';
import {parseDateWithoutYear, parseMonth} from '@/assets/utils/util-method.js';
import Button from '../login/Button.vue';
export default {
	components:{
		Button
	},
	setup(){
		/// define echart
		let echart = echarts;
		const userInfo = JSON.parse(localStorage.getItem("userInfo"));
		const store = useStore();
		const weeklyUserRecord = computed(()=>{
			return store.state.setting.userRecordWeekly;
		});
		const monthlyUserRecord = computed(()=>{
			return store.state.setting.userRecordMonthly;
		});
		
		//two chart instances
		let vocabularyChart;
		let learningTimeChart;
		
		let xAxisData = ref();
		let seriesData = ref();

		onMounted(() => {
			//get the data of user, initial is base on week
			doGet("/v1/setting/get-record",{
				userId : userInfo.id
			}).then((response) => {
				if(response.data.code == 1000){
					store.commit("setting/UPDATE_USER_RECORD", response.data.data);
					store.commit("setting/UPDATE_USER_RECORD_BY_TIME", "weekly");
					store.commit("setting/UPDATE_USER_RECORD_BY_TIME", "monthly");

					//set init option
					vocabularyChart = echart.init(document.getElementById("vocabularyChart"), "vintage");
					learningTimeChart = echart.init(document.getElementById("learningTimeChart"));
					getWeeklyData(new Date(weeklyUserRecord.value[0][0].loginTime), weeklyUserRecord.value[0], "vocabulary", vocabularyChartOptions);
					//getMonthlyData(new Date(), "vocabulary", vocabularyChartOptions);
					getWeeklyData(new Date(weeklyUserRecord.value[0][0].loginTime), weeklyUserRecord.value[0], "learningTime", learningTimeChartOptions);
					//getMonthlyData(new Date(), "learningTime", learningTimeChartOptions);
					initChart();
				}
			}).catch((err) => {
				
			});
			
		});

		onUnmounted(() => {
			vocabularyChart.dispose();
		});

		//pass a initial date as the start date of this chart
		//pass data arr in it to be the seriesData
		//pass type in it to generate vocabulary or learning time
		function getWeeklyData(date, data, type, chartOptions){
			let xAxisData = [];
			let seriesData = [];

			for(var i = 0; i < 7; i++){
				let tempDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() - i);
				xAxisData.push(parseDateWithoutYear(tempDate));
			}
			var indexData = 0;
			for(var i = 0; i < xAxisData.length; i++){
				var curDate = new Date(data[indexData].loginTime);
				if(parseDateWithoutYear(curDate) == xAxisData[i]){
					if(type == "vocabulary"){
						seriesData[i] = data[indexData].wordNumber;
					}else if(type == "learningTime"){
						seriesData[i] = data[indexData].learningTime;
					}
					indexData++;
					if(indexData == data.length){
						break;
					}
				}else{
					seriesData[i] = 0;
				}
			}
			chartOptions.xAxis.data = xAxisData.reverse();
			chartOptions.series[0].data = seriesData.reverse();
		}


		//pass date in it to be the start date of this chart.
		function getMonthlyData(date, type, chartOptions){
			let xAxisData = [];
			let seriesData = [];
			let monthlyRecord = monthlyUserRecord.value;
			for(var i = 0; i < 7; i++){
				xAxisData.push(parseMonth(new Date(date.getFullYear(), date.getMonth() - i)));
			}
			for(var i = 0; i < xAxisData.length; i++){
				if(monthlyRecord[xAxisData[i]] == undefined){
					seriesData.push(0);
					continue;
				}
				if(type == "vocabulary"){
					seriesData.push(monthlyRecord[xAxisData[i]].vocabulary);
				}else if(type == "learningTime"){
					seriesData.push(monthlyRecord[xAxisData[i]].learningTime);
				}
				
			}
			chartOptions.xAxis.data = xAxisData.reverse();
			chartOptions.series[0].data = seriesData.reverse();
		}

		let vocabularyChartOptions = reactive({
			xAxis : {
				type : "category",
				data: [],
			},
			title:{
				text: "Vocabulary",
				left: "center"
			},
			tooltip:{
				trigger:"axis"
			},
			yAxis:{
				type : "value"
			},
			series: [
				{
					data: [],
					type: "bar",
				}
			]
		})

		let learningTimeChartOptions = reactive({
			xAxis : {
				type : "category",
				data: [],
			},
			title:{
				text: "Learning Time",
				left: "center"
			},
			tooltip:{
				trigger:"axis"
			},
			yAxis:{
				type : "value"
			},
			series: [
				{
					data: [],
					type: "bar",
				}
			]
		})

		watch(vocabularyChartOptions, (newValue)=>{
			vocabularyChart.setOption(newValue, true);
		})

		watch(learningTimeChartOptions, (newValue)=>{
			learningTimeChart.setOption(newValue, true);
		})
		// configuration of Echarts
		function initChart() {
			// configuration and data	
			vocabularyChart.setOption(vocabularyChartOptions, true);
			learningTimeChart.setOption(learningTimeChartOptions, true)
			window.onresize = function() {
				vocabularyChart.resize();
				learningTimeChart.resize();
			};
		}
		return { 
			initChart,
			vocabularyChartOptions,
			learningTimeChartOptions,
			getMonthlyData,
			getWeeklyData,
			weeklyUserRecord
		};
	}
}
</script>

<style scoped>
  	@import "@/assets/css/main_page.css";
	@import "@/assets/css/font.css";
	
	.chartsDiv{
		width: 100%;
		height: 88%;
		position: relative;
		top: 7.3rem;
		display: flex;
		flex-direction:column;
		justify-content: space-around;
		align-items: center;
	}

	.chartSize{
		width: 100rem;
		height: 26rem;
	}

	.chartDiv{
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
		font-size: 1.7rem;
	}
	.weeklyBtn{
		left: 18rem;
		
	}


	.monthlyBtn{
		left: 28rem;
		
	}

	.buttonDiv{
		position: absolute;
		z-index: 1;
		margin-top: 2rem;
	}
</style>
