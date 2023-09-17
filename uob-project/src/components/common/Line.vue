<template>
  <div class="line" :style="{width: length + 'rem'}"></div>
</template>

<script>
export default {
    name :"Line",
    props:["length"]
}
</script>

<style scoped>
.line{ 
    height: 0rem;
    border: 0.50px rgba(0, 0, 0, 0.24) solid;
}
</style>