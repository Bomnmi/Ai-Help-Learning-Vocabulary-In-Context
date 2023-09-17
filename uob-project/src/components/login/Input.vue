<template>
    <div class="inputDiv fontType">
        <div class="inputFrame">
            <div class="inputNameDiv">{{inputName}}</div>
            <slot name="inputTips"></slot>
        </div>
        <input class="inputType" :type="type" v-model="inputValue" @blur="checkFomat" @keydown.enter="checkFomat"/>
    </div>
    
</template>

<script>
import { onMounted, reactive, ref } from 'vue';

export default {
    name : "Input",
    emits : ["checkEmailFormat", "getPassword","getConfirmedPassword"],
    props : ["inputName", "type"],
    setup(props, context){
        
        let inputValue = ref("");
        let inputTips = ref("");

        function checkFomat(){
            if(props.inputName === "Email"){
                context.emit("checkEmailFormat", inputValue.value);
            }else if(props.inputName === "Password"){
                context.emit("getPassword", inputValue.value);
            }else if(props.inputName==="Confirm your password"){
                context.emit("getConfirmedPassword", inputValue.value);
            }
        }

        return {checkFomat, inputValue, inputTips}
    }
}
</script>

<style>
@import "@/assets/css/animation.css";
@import "@/assets/css/font.css";
.inputDiv{
    width: 54rem;  
    height: 8rem;
    display: flex;
    flex-direction: column;
}
.inputDiv div:nth-child(1){
    flex:1 1 0;
}
.inputType{
    flex:2 1 0;
    border-radius: 8px; border: 0.50px black solid;
    color: black; font-size: 2rem; word-wrap: break-word; padding-left: 1rem;
}

.inputFrame{
    display: flex;
    justify-content: space-between;
}

.inputNameDiv{
  color: black; font-size: 2rem; word-wrap: break-word
}



</style>