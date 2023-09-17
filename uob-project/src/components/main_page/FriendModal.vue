<template>
  <div class="friendModalDiv modalColor" :style="{width: friendModalWidth + 'rem'}" >
    <div class="friendModalHeaderDiv">
        <div>Friend</div>
        <div>
            <input type="text">
        </div>
        <div>
            <img src="@/assets/svg/main_page/search.svg" alt="">
            <img src="@/assets/svg/main_page/add_friend.svg" alt="">
        </div>
    </div>
    <Line :length="friendModalWidth-2"/>
    <div class="friendListDiv">
        <el-scrollbar height="25rem">
            <div v-for="(item, index) in 20" :key="index">
                {{item}}
            </div>
        </el-scrollbar>
    </div>
</div>

</template>

<script>
import { computed, ref } from 'vue'
import { mapState, useStore } from 'vuex'
import Line from '../common/Line.vue'
    export default {
        name :"FriendModal",
        emits:["getWidth"],
        components:{Line},
        setup(){
            let store = useStore()
            let friendModalWidth = computed(()=> store.state.modalParam.friendModalWidth);
            return {
                friendModalWidth,
            }
        }
    }
</script>

<style scoped>
@import "@/assets/css/modal.css";
@import "@/assets/css/animation.css";
    .friendModalDiv{
        height: 30rem; 
        position: absolute;
        box-shadow: 0px 17px 8px 10px rgba(0, 0, 0, 0.25); 
        border-radius: 1rem;
        z-index: 10;
        display: flex;
        flex-direction: column;
        align-items: center;
    }
    .friendModalHeaderDiv{
        height: 3.4rem;
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: space-around;
    }

    .friendModalHeaderDiv :nth-child(1){
        color: black;
        font-size: 1.6rem; 
        font-family: Fondamento; 
        word-break: break-all;
    }
    .friendModalHeaderDiv :nth-child(2){
        width: 50%;
        height: 100%;
        display: flex;
        align-items: center;
    }

    .friendModalHeaderDiv :nth-child(2) input{
        width: 100%;
        height: 50%;
        /* display: none; */
        background-color: #FFFFFF;
        border-radius: 0.5rem;
        border: 0.5rem solid #FFFFFF;
        padding-left: 0.5rem;
    }

    .friendModalHeaderDiv :nth-child(2) input:focus{
        background-color: #FFFFFF;
        border: 0.2rem solid #FFCBE4;
        outline: none;
    }

    .friendModalHeaderDiv :nth-child(3){ 
        width: 20%;
        display: flex;
        align-items: center;
        justify-content: space-between; 
    }
    .friendModalHeaderDiv :nth-child(3) img{
        width:2rem;
        height:2rem;
    }

    .friendListDiv{
        width: 100%;
        flex:1 1 0;
    }
</style>