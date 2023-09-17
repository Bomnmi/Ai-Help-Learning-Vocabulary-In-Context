<template>
    <div class="settingPage">
        <Search class="searchWordDiv"/>
        <div class="wordListDiv">
              <WordList/>
        </div>
    </div>
</template>

<script>
import Search from './Search.vue'
import WordList from './WordList.vue'
import { onMounted } from 'vue'
import { useStore } from 'vuex'
export default {
    name:'RecentWords',
    components:{Search,WordList},
    setup(){
		let userInfo = JSON.parse(localStorage.getItem("userInfo"));
        const store = useStore();
        onMounted(()=>{
			store.dispatch("setting/getRecentWords", userInfo.id);
        })
    }
}
</script>

<style scoped>
    @import "@/assets/css/main_page.css";
    .searchWordDiv{
        width: 100%;
        height: 5rem;
        display: flex;
        position: relative;
        top: 1rem;
        justify-content: space-around;
        align-items: center;
    }
    .wordListDiv{
        width: 100%;
        /* background-color: red; */
        height: 80%;
        top:3rem;
        position:relative;
    }
</style>