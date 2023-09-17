<template>
  <div class="buttonDiv selectedColor">
    <div class="fontType">
        {{buttonName}}
    </div>
  </div>
</template>

<script>
import { useRoute } from 'vue-router'
export default {
    name : "SettingButton",
    props:["buttonName"],
    setup(){
    
    }
}
</script>

<style scoped>
@import "@/assets/css/font.css";
@import "@/assets/css/button.css";
    .buttonDiv{
        height: 10%;
        border-radius: 1.5rem;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .fontType{
        font-size: 2.5rem;
        color: black;
        text-align: center;
    }

    
</style>