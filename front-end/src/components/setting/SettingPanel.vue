<template>
    <div class="settingPanelDiv mainPageColor">
        <div class="fontType">
            Settings    
        </div>
        <!-- This slot is for Line -->
        <Line class="lineVue"/>
        <SettingButton 
        :buttonName="item.buttonName" 
        v-for="(item, index) in buttonNameArr" 
        :key="index" :class="item.isSelected ? 'buttonSelected' : ''" @click="goToAnotherPage(item.urlName, index)"/>        
    </div>
</template>

<script>
import { onMounted, onUpdated, reactive, watch } from 'vue'
import SettingButton from './SettingButton.vue'
import Line from '../common/Line.vue'
import { useRoute, useRouter } from 'vue-router'
export default {
    components:{SettingButton, Line},
    setup(){
        let buttonNameArr = reactive([
            {buttonName:"Profile",isSelected:false, urlName:'/setting/profile'},
            {buttonName:"Vocabulary Repository",isSelected:false,urlName:'/setting/vocabulary-repository'},
            {buttonName:"Recent Words",isSelected:false,urlName:'/setting/recent-words'},
            {buttonName:"Sentences",isSelected:false,urlName:'/setting/sentences'},
            {buttonName:"Calendar",isSelected:false,urlName:'/setting/calendar'},
            {buttonName:"Statistics",isSelected:false,urlName:'/setting/statistics'},
        ])
        const route = useRoute();
        const router = useRouter();
		let lastSelectedIndex = 0;
        onMounted(()=>{
            buttonNameArr.forEach((item, index) => {
                let lowerCaseButtonName = item.buttonName.split(" ").join("-").toLowerCase();
                let urlName = route.params.settingPage.toLowerCase();
                if(lowerCaseButtonName === urlName){
                    buttonNameArr[index].isSelected = true;
					lastSelectedIndex = index;
                }
            })
        });

        // watch(route, (newValue)=>{
        //     if(newValue.name == 'setting'){
        //         buttonNameArr.forEach((item, index) => {
        //             buttonNameArr[index].isSelected = false;
        //             let lowerCaseButtonName = item.buttonName.split(" ").join("").toLowerCase();
        //             let urlName = newValue.params.settingPage.toLowerCase();
        //             if(lowerCaseButtonName === urlName){
        //                 buttonNameArr[index].isSelected = true;
        //             }
        //         })
        //     }
        // })


        function goToAnotherPage(urlName,index){
			buttonNameArr[lastSelectedIndex].isSelected = false;
			buttonNameArr[index].isSelected = true;
			lastSelectedIndex = index;
            router.push(urlName);
        }
        return {
            buttonNameArr,
            goToAnotherPage
        }
    }
}
</script>

<style scoped>
    @import "@/assets/css/main_page.css";
    @import "@/assets/css/font.css";
    @import "@/assets/css/button.css";

    .settingPanelDiv{
        width: 20%;
    }

    .fontType{
        font-size:4rem;
        color: black;
        text-align: center;
    }

    .lineVue{
        width:500%;
        border: 0.3rem white solid;
        position: relative;
        z-index: 1;
    }
</style>