<template>
    <div @click="triggerAddArticle">
        <svg width="27.4rem" height="27.4rem" viewBox="0 0 274 274" fill="none" xmlns="http://www.w3.org/2000/svg" >
            <path :class="isSelected" d="M137 265.438C66.0682 265.438 8.5625 207.932 8.5625 137C8.5625 66.0682 66.0682 8.5625 137 8.5625C207.932 8.5625 265.438 66.0682 265.438 137C265.438 207.932 207.932 265.438 137 265.438ZM137 248.312C198.479 248.312 248.312 198.479 248.312 137C248.312 75.5212 198.479 25.6875 137 25.6875C75.5212 25.6875 25.6875 75.5212 25.6875 137C25.6875 198.479 75.5212 248.312 137 248.312Z" fill="black" fill-opacity="0.65"/>
            <path :class="isSelected" d="M68.5 145.562C66.2291 145.562 64.0512 144.66 62.4454 143.055C60.8396 141.449 59.9375 139.271 59.9375 137C59.9375 134.729 60.8396 132.551 62.4454 130.945C64.0512 129.34 66.2291 128.438 68.5 128.438H205.5C207.771 128.438 209.949 129.34 211.555 130.945C213.16 132.551 214.062 134.729 214.062 137C214.062 139.271 213.16 141.449 211.555 143.055C209.949 144.66 207.771 145.562 205.5 145.562H68.5Z" fill="black" fill-opacity="0.65"/>
            <path :class="isSelected" d="M128.438 68.5C128.438 66.2291 129.34 64.0512 130.945 62.4454C132.551 60.8396 134.729 59.9375 137 59.9375C139.271 59.9375 141.449 60.8396 143.055 62.4454C144.66 64.0512 145.562 66.2291 145.562 68.5V205.5C145.562 207.771 144.66 209.949 143.055 211.555C141.449 213.16 139.271 214.062 137 214.062C134.729 214.062 132.551 213.16 130.945 211.555C129.34 209.949 128.438 207.771 128.438 205.5V68.5Z" fill="black" fill-opacity="0.65"/>
        </svg>
    </div>
    <span>Press to add article</span>

</template>

<script>
import { ref } from 'vue'
export default {
    name : "AddArticle",
    emits:["addArticle"],
    setup(props, context){
        let isSelected = ref("unselected")
        function triggerAddArticle(){
            context.emit("addArticle")
        }
        return{
            isSelected,
            triggerAddArticle
        }
    }
}
</script>

<style scoped>
    span{
      color: black; font-size: 3.4rem; font-family: Fondamento; font-weight: 400; word-wrap: break-word
    }

    path{
        box-shadow: 20px 12px 4px rgba(0, 0, 0, 0.25)
    }
    
    div{
        cursor: pointer;
    }
    svg:hover path{
        fill :#FF9393;
    }  
</style>