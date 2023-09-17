<template>
  <div class="articleTitleDiv">
        <Line length="38"/>
        <span class="fontType">{{title}}</span>
        <Line length="38"/>
  </div>
</template>

<script>
import Line from '../common/Line.vue'
export default {
    name:"Title",
    components:{Line},
	props:['title'],

}
</script>

<style scoped>
	@import "@/assets/css/font.css";
    .articleTitleDiv{
        height: 100%;
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: space-around;
    }
    span{
      color: black; font-size: 3.4rem; font-weight: 400; word-wrap: break-word
    }
</style>