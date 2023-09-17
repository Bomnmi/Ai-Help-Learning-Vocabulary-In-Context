<template>
  <div class="tagDiv">
    <div class="tagType fontType" >{{tagName}}</div>
  </div>

</template>

<script>
export default {
    name : "Tag",
    props:["tagName"]
}
</script>

<style scoped>
	@import "@/assets/css/font.css";
    .tagDiv{
        justify-content: center; align-items: center; gap: 10px; display: inline-flex
    }
    .tagType{
        color: #F25D5D; font-size: 5rem; word-wrap: break-word
    }


</style>