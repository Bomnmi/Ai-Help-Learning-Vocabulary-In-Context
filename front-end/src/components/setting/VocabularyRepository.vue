<template>
    <div class="settingPage">
        <Search class="searchWordDiv"/>
        <div class="wordListDiv">
            <WordList />
        </div>
		
    </div>
</template>

<script>
import { onMounted } from 'vue'
import Search from './Search.vue'
import WordList from './WordList.vue'
import { useStore } from 'vuex'
export default {
    name : "VocabularyRepository",
    components:{Search, WordList},
    setup(){
        const store = useStore();
        let userInfo = JSON.parse(localStorage.getItem("userInfo"));
        onMounted(()=>{
            store.dispatch("setting/getAllWords", userInfo.id);
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