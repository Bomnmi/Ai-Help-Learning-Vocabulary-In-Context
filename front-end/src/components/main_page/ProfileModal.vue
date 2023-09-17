<template>
  <div class="profileModalDiv modalColor" :style="{width: profileModalWidth + 'rem'}">
        <div>
            {{userName}}
        </div>
        <Line/>
         <div @click="clickProfileButton(item)" v-for="(item, index) in profileButton" :key="index" class="selectedColor">
            {{item}}
        </div>
  </div>
</template>

<script>
import { computed, reactive, ref} from 'vue';
import { useStore } from 'vuex';
import Line from '../common/Line.vue';
import { useRouter } from 'vue-router';
export default {
    name : "ProfileModal",
    components : {Line},
    setup() {
        const userInfo = JSON.parse(window.localStorage.getItem("userInfo"));
        let userName = ref(userInfo.userName);
        let profileButton = reactive(["Profile", "Settings", "Exit"]);
        let store = useStore();
        let router = useRouter();

        let profileModalWidth = computed(()=>store.state.modalParam.profileModalWidth);

        function clickProfileButton(buttonName){
            if(buttonName == "Profile"){
                router.push("/setting/profile")
            }else if(buttonName == "Settings"){

            }else if(buttonName == "Exit"){

            }
        }

        return {
            profileModalWidth,
            profileButton,
            userName,
            clickProfileButton
        }
    }
}
</script>

<style scoped>

    @import "@/assets/css/modal.css";
    @import "@/assets/css/button.css";
    @import "@/assets/css/animation.css";
    .profileModalDiv {
        width: 10rem;
        position:absolute;
        box-shadow: 0px 17px 8px 10px rgba(0, 0, 0, 0.25); 
        z-index: 2;
        display: flex;
        flex-direction: column;
        justify-content: center;
        border-radius: 1.5rem;
    }
    .profileModalDiv div{
        padding-left: 1rem;
        color: black;
        font-size: 1.6rem; 
        font-family: Fondamento; 
        word-break: break-all;
    }
    .selectedColor{
        border-radius: 1.5rem;
    }
</style>