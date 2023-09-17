<template>
    <div class="settingDiv">
        <PageHeader/>
        <div class="settingMainDiv">
            <SettingPanel/>
            <component :is="componentName"></component>
        </div>
    </div>
  
  
</template>

<script>
import { onMounted, onUpdated, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import PageHeader from '../../components/main_page/PageHeader.vue'
import SettingPanel from '../../components/setting/SettingPanel.vue'
import Profile from '../../components/setting/Profile.vue'
import VocabularyRepository from '../../components/setting/VocabularyRepository.vue'
import RecentWords from '../../components/setting/RecentWords.vue'
import Sentences from '../../components/setting/Sentences.vue'
import Calendar from '../../components/setting/Calendar.vue'
import Statistics from '../../components/setting/Statistics.vue'
import { useStore } from 'vuex'
import {parseUrlFormToVueName} from "@/assets/utils/util-method.js"
export default {
    components: {
        PageHeader,
        SettingPanel,
        Profile,
        VocabularyRepository,
        RecentWords,
        Sentences,
        Calendar,
        Statistics
    },
    name: 'Setting',
    setup(){
        const route = useRoute();
        let settingPageName = route.params.settingPage;
        let componentName = ref(parseUrlFormToVueName(settingPageName));
		let userInfo = JSON.parse(localStorage.getItem("userInfo"));
		const store = useStore();
        watch(route, (newValue)=>{
            if(newValue.name == 'setting'){
                componentName.value = parseUrlFormToVueName(newValue.params.settingPage);
            }
        })
        onMounted(()=>{
            store.dispatch("setting/getAllWords", userInfo.id);
        })
        return{
            settingPageName,
            componentName
        }
    }

}
</script>

<style scoped>
    @import "@/assets/css/animation.css";
    .settingDiv{
        width: 100%;
        height: 100%;
        position: absolute;
        display: flex;
        flex-direction: column;
    }

    .settingMainDiv{
        width: 100%;
        height: 100%;
        display: flex;
    }
    
</style>