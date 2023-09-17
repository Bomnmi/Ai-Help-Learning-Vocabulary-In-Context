<template>
  <div>
    <RouterView v-slot="{Component}">
      <KeepAlive include="login,register,verification">
        <component :is="Component"></component>
      </KeepAlive>
    </RouterView>
   
  </div>
</template>

<script>

  import {useRoute, useRouter} from 'vue-router'

  export default {
      name:"App",
      components:{},
      setup(){
        const route = useRoute();
        // onMounted(()=>{
        //   router.push('/login')
        // })
        return {
                  route
                }
      }
  }
</script>

<style>

</style>