<template>
    <div :class="animationClass" style="width : 29rem; cursor:pointer"  @mouseover="isSelected = 'selected'" @mouseleave="isSelected = ''" @click="chooseBook">
        <div class="bookDiv  fontType" :class="isSelected">
            <div class="titleDiv">
                <div class="title" style="">{{bookTitle}}</div>
            </div>
            <div class="keywordDiv">
                <div>KeyWords:</div>
                <div>{{keyWords}}</div>
            </div>
        </div>
    </div>
    
</template>

<script>
import { computed, ref } from 'vue'
export default {
    name : "Book",
    emits:["clickBook"],
    props:["title", "content", "id", "keyWords"],
    setup(props, context){

        let animationClass = ref("fadeIn")
        let isSelected = ref("unSelected")
        let bookTitle = ref(props.title);
        let bookContent = ref(props.content);
        
        let id = ref(props.id);
        function chooseBook(){
            context.emit("clickBook", bookTitle.value, bookContent.value, id.value)
        }
        return {isSelected, bookTitle, bookContent, chooseBook,animationClass}
    }
}
</script>

<style scoped>
	@import "@/assets/css/font.css";
    .bookDiv{
        width: 27rem; 
        height: 35.6rem; 
        background: #F0CECE; 
        display: flex;
        flex-direction: column;
        align-items: center;
        border: 0.50px black solid;
        border-radius: 2.0rem; 
        
    }
    .selected{
        transform: translateX(-2rem);
        box-shadow: 2rem 2rem 0.8rem rgba(0, 0, 0, 0.54);
    }
    
    .titleDiv{
        width: 26.4rem; 
        height: 9.7rem;
        padding-top: 0.8rem;
        background: #F0CECE; 
        justify-content: center; 
        align-items: center; 
        gap: 10px; 
        display: flex;
        border-radius: 2.0rem; 
        
    }
    .title{
        width: 26.7rem; 
        text-align: center; 
        color: black; 
        font-size: 1.6rem; 
        font-style: italic;
        word-wrap: break-word
    }

    .keywordDiv{
        top: 3rem;
        position: relative;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .keywordDiv :first-child{
        width: 26.7rem;
        padding-left: 0.5rem;
        color: black; 
        font-size: 1.6rem; 
        font-style: italic; 
    }

    .keywordDiv :last-child{
        width: 26.4rem; 
        height: 14rem;
        border: 0.50px black solid;
        font-size: 1.7rem;
        color: black; 
    }
</style>