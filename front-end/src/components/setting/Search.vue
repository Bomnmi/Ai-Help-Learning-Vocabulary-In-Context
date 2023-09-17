<template>
    <div>
        <img class="searchSvg" src="@/assets/svg/setting/search.svg"/>
        <input class="searchInput" type="text">
		<ShowEye v-show="showEye" @click="$store.commit('setting/UPDATE_SHOW_EYE', !showEye)" class="showEyeSvg"/>
		<HideEye v-show="!showEye" @click="$store.commit('setting/UPDATE_SHOW_EYE', !showEye)" class="hideEyeSvg"/>
		<Edit @click="$store.commit('setting/UPDATE_SHOW_EDIT', !showEdit)" class="editSvg"/>
    </div>
</template>

<script>
import { computed, ref } from 'vue'
import { useStore } from 'vuex';
import ShowEye from '../svg/ShowEye.vue';
import HideEye from '../svg/HideEye.vue';
import Edit from '../svg/Edit.vue';
export default {
    name : 'Search',
	components:{ShowEye, HideEye, Edit},
    setup(){
        const store = useStore();
        let showEye = computed(()=>{
            return store.state.setting.showEye;
        })
		let showEdit = computed(()=>{
			return store.state.setting.showEdit;
		})
        return {
            showEye,
			showEdit
        }
    }
}
</script>

<style scoped>
    .searchSvg{
        height: 3rem;
        width: 3rem;
    }
    .searchInput{
        width: 85rem;
        border-radius: 3rem;
        height: 3rem;
        border: 0;
        padding-left: 1rem;
        font-size: 2rem;
    }
    .showEyeSvg{
        height: 4rem;
        width: 4rem;
    }
    .editSvg{
        height: 3rem;
        width: 3rem;
    }
    .hideEyeSvg{
        height: 4rem;
        width: 4rem;
    }

</style>