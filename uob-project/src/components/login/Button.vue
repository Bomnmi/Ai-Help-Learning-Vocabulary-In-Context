<template>
    <div class="btnDiv" :style="{width: width, height : height}" @mouseover="onMouseOver" 
    @mouseleave="onMouseLeave">
      <div :class="fontType" :style="{fontSize: size + 'rem'}" ref="button">{{btnName}}</div>
    </div>
</template>

<script>
import { ref } from 'vue'
export default {
  name : "Button",
  props:["btnName", "width", "height", "fontSize", "fontType"],
  setup(props){
    const button = ref(null);
    let size = ref(props.fontSize);
    const onMouseOver = (event)=>{
      size.value = parseInt(props.fontSize) + 1;
    }
    const onMouseLeave = (event)=>{
      size.value = parseInt(props.fontSize);
    }
    
    return {onMouseOver, size, onMouseLeave,button}
  }
}
</script>

<style>
@import "@/assets/css/font.css";
.btnDiv{
  width: 59rem; 
  height: 5.5rem; 
  background: #F0CECE; 
  border-radius: 1.8rem; 
  border-left: 0.50px black solid; 
  border-top: 0.50px black solid; 
  border-right: 0.50px black solid; 
  border-bottom: 0.50px black solid;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}
.btnDiv:hover{
  background-color: #f59a9a;
  color: #ffffff;
}

</style>