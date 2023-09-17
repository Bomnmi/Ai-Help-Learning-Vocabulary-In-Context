<template>
    <div class="headerDiv mainPageColor" ref="headerDivRef">
        <CheckIn class="headerButtonDiv"/>
        <Reading class="readingButtonDiv headerButtonDiv" @click="router.push('/article/0/0')"/>
		<Quiz class="headerButtonDiv quizButtonDiv" @click="pushToQuizPage"/>
        <div id="headerBlockDiv"></div>
        <Rank class="headerButtonDiv"/>
        <ShowFriend ref="showFriendRef" class="headerButtonDiv" @click="clickFriendButton" id="showFriendId"/>
        <Profile ref="showProfileRef" class="headerButtonDiv" @click="clickProfileButton" id="profileId"/>
        <FriendModal id="friendModal" ref="friendModal" v-show="showFriendModal"
        :class="friendAnimationClass"
        />
        <ProfileModalVue 
        id="profileModal" 
        ref="profileModal" 
        v-show="showProfileModal"
        class="profileModalDiv" 
        :class="profileAnimationClass"/>
    </div>
    
</template>

<script>
import { onMounted, onUnmounted, reactive, ref } from 'vue';
import FriendModal from './FriendModal.vue';
import {locateModal, pxToRem, closeOutSide,changeSvgColor} from '@/assets/utils/util-method.js'
import { useStore } from 'vuex';
import CheckIn from '../svg/CheckIn.vue';
import Reading from '../svg/Reading.vue';
import Rank from '../svg/Rank.vue';
import ShowFriend from '../svg/ShowFriend.vue';
import Profile from '../svg/Profile.vue';
import ProfileModalVue from './ProfileModal.vue';
import Quiz from '../svg/Quiz.vue';
import { useRouter } from 'vue-router';

export default {
    name : 'PageHeader',
    components:{
        FriendModal,CheckIn,Reading, Rank, ShowFriend, Profile,ProfileModalVue, Quiz},
    setup(){
        //Animation Controle
        let friendAnimationClass = ref("fadeInUp");
        let profileAnimationClass = ref("fadeInUp");

        let friendModal = ref(null);
        let store = useStore();
        let checkInComponent = ref(null);
        let headerDivRef = ref(null);
        let profileModal = ref(null);
        let showFriendRef = ref(null);
        let showProfileRef = ref(null);
        
        //Controle the Modal Window
        let showFriendModal = ref(false);
        let showProfileModal = ref(false);

        //router
        let router = useRouter();

        //Some time click svg would choose the inner path element of it.
        //So add closest() method to find the svg tag.
        function closeFriendModal(event){
            let element = event.target.closest("#showFriendId");
            if(element && element.id === "showFriendId" ){
                return;
            }
            friendAnimationClass.value = "fadeOutDown";
            window.setTimeout(()=>{
                showFriendModal.value = false;
            },300);
        }

        function closeProfileModal(event){
            let element = event.target.closest("#profileId");
            if(element && element.id === "profileId"){
                return;
            }
            profileAnimationClass.value = "fadeOutDown";
            window.setTimeout(()=>{
                showProfileModal.value = false;
            },300);
        }

        onMounted(()=>{
            document.body.addEventListener('click', closeOutSide(showFriendModal, closeFriendModal, "#friendModal"), true);
            document.body.addEventListener('click', closeOutSide(showProfileModal, closeProfileModal, "#profileModal"), true);
        })

        function clickFriendButton(e){
            friendAnimationClass.value = "fadeInUp";
            let rect = showFriendRef.value.$el.getBoundingClientRect();
            locateModal(e, friendModal, ()=>{
                let width = store.state.modalParam.friendModalWidth;
                let x = pxToRem(rect.x) - width / 2.5;
                let y = pxToRem(rect.y + rect.height);
                return {x,y}
            })
            showFriendModal.value = true;
        }

        function clickProfileButton(e){
            profileAnimationClass.value = "fadeInUp";
            let rect = showProfileRef.value.$el.getBoundingClientRect();
            locateModal(e, profileModal, ()=>{
                let width = store.state.modalParam.profileModalWidth;
                let x = pxToRem(rect.x) - width / 4;
                let y = pxToRem(rect.y + rect.height) + 0.3;
                return {x,y}
            })
            showProfileModal.value = true;
        }

		function pushToQuizPage(){
			if(localStorage.getItem("quizProblems") != undefined){
				router.push('/quiz/quiz-problem/0');
			}else{
				router.push('/quiz/quiz-setting/-1');
			}
			
		}
        return {
            friendModal,
            showFriendModal,
            checkInComponent,
            headerDivRef,
            clickFriendButton,
            profileModal,
            showProfileModal,
            clickProfileButton,
            friendAnimationClass,
            profileAnimationClass,
            showFriendRef,
            showProfileRef,
            router,
			pushToQuizPage
        }
    }
}
</script>

<style scoped>
    @import "@/assets/css/main_page.css";
    .headerDiv{
        height: 6rem;
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: space-around;
    }

    .headerButtonImg{
        height: 4.3rem;
        width: 4.3rem;
    }

    .headerButtonDiv{
        height: 4.3rem;
        width: 4.3rem;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
    }
	.readingButtonDiv{
		height: 5.0rem;
        width: 5.0rem;
	}
	.quizButtonDiv{
		height: 3.7rem;
        width: 3.7rem;
	}
    #headerBlockDiv{
        width: 60rem;
    }
    </style>