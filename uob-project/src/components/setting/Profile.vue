<template>
  <div class="settingPage">
    <div class="profileTopDiv">
        <div class="userProfileInfoDiv modalColor fadeIn">
            <div class="userProfileInfoHeaderDiv">  
                <div class="userAvatarAndInfoDiv">
                    <div class="avatarDiv">123</div>
                    <div class="userInfoDiv">
                        <div v-show="!changedUserNameFlag" class="fontType profileInfoFont">
                            {{userInfo.userName}}
                        </div>
                        <input class="fontType profileInfoFont userNameInput" 
                        v-show="changedUserNameFlag" type="text" 
                        v-model="userInfo.userName" ref="userNameInputRef" @focus="userNameInputRef.select();"
                        @blur="changedUserNameFlag = !changedUserNameFlag"
                        >
                        <div class="fontType profileInfoFont">{{userInfo.email}}</div>
                    </div>
                    <Modify class="modifySvg" @click="changedUserNameFlag = !changedUserNameFlag;"/>
                </div>
                <Line length="44"/>
            </div>

            <div class="userProfileInfoFooterDiv fadeIn">
                <div v-for="(item, index) in profileButton" :key="index" 
                class="fontType selectedColor profileButton">
                    {{item.buttonName}}
                </div>
                <div class="fontType">You have  joined us {{profileDetail.userJoinTime}} days! Keep going!</div>
            </div>
            
        </div>

        <div class="userLearningInfoDiv fadeIn">
            <div class="userLearningDiv modalColor">
                <div class="userLearningDetailDiv">
                    <div class="fontType">Today</div>
                    <div class="numberType">{{profileDetail.todayLearnedWords}}</div>
                    <div class="fontType">words</div>
                </div>
                <div class="userLearningDetailDiv">
                    <div class="fontType">Overview</div>
                    <div class="numberType">{{profileDetail.totalWords}}</div>
                    <div class="fontType">words</div>
                </div>
            </div>  
            <div class="userLearningDiv modalColor">
                <div class="userLearningDetailDiv">
                    <div class="fontType">Today</div>
                    <div class="numberType">{{profileDetail.todayLearningTime}}</div>
                    <div class="fontType">minutes</div>
                </div>
                <div class="userLearningDetailDiv">
                    <div class="fontType">Overview</div>
                    <div class="numberType">{{profileDetail.overviewLearningTime}}</div>
                    <div class="fontType">minutes</div>
                </div>
            </div>
        </div>
    </div>
  </div>
</template>

<script>
import { onMounted, reactive, ref, toRef, toRefs, watch } from 'vue'
import Line from '../common/Line.vue'
import Modify from '../svg/Modify.vue'
import {doGet, doPost} from '@/assets/utils/axios-util.js';
export default {
    name:'Profile',
    components:{Line, Modify},
    setup(){
        let profileButton = reactive([
            {buttonName : "Change Password"},
            {buttonName : "Privacy Settings"},
            {buttonName : "My Friends"},
        ])
        let userInfo = reactive(JSON.parse(window.localStorage.getItem("userInfo")));
        
        let changedUserNameFlag = ref(false);
        const userNameInputRef = ref(null);
        
        let profileInfo = reactive({
            profileDetail: {},
        })
        //Query the total number of learned words and learning time
        onMounted(()=>{
            doGet("/v1/setting/get-profile-detail", {
                userId : userInfo.id
            }).then((response) => {
                console.log(response.data.data);
                if(response.data.code == 1000){
                    profileInfo.profileDetail = {...response.data.data};
                    console.log(profileInfo);
                }
            }).catch((err) => {
                
            });
        })
        
        watch(changedUserNameFlag, (newValue)=>{
            if(newValue == false){
                //update user name in local storage and database
                doPost("/v1/user/update-username", {
                    userId : userInfo.id,
                    newUserName: userInfo.userName
                }).then((response) => {
                    if(response.data.code == 1000){
                        window.localStorage.setItem("userInfo", JSON.stringify(userInfo));
                    }
                }).catch((err) => {
                    
                });
            }
        })
        
        
        return {
            profileButton,
            userInfo,
            changedUserNameFlag,
            userNameInputRef,
            ...toRefs(profileInfo),
        }
    }

}
</script>

<style scoped>
    @import "@/assets/css/button.css";
    @import "@/assets/css/main_page.css";
    @import "@/assets/css/font.css";
    @import "@/assets/css/modal.css";
    @import "@/assets/css/animation.css";
    
    .userNameInput{
        background-color: transparent;
    }

    .profileTopDiv{
        top:10rem;
        position: relative;
        height: 40rem;
        display: flex;
        justify-content: space-around;
    }

    .userProfileInfoDiv{
        width: 40%;
        height: 100%;
        border-radius: 6rem;
        box-shadow: 0px 12px 4px rgba(0, 0, 0, 0.25); 
        display: flex;
        flex-direction: column;
        justify-content: space-around;
    }
    .userProfileInfoHeaderDiv{
        width: 100%;
        height: 35%;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        align-items: center;

    }

    .userAvatarAndInfoDiv{
        width: 100%;
        height: 90%;
        display: flex;
        justify-content: space-around;
    }

    .avatarDiv{
        width: 15%;
        height: 100%;
    }

    .userInfoDiv {
        width: 50%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
    }
    .modifySvg{
        top: 1.9rem;
        right: 3rem;
        position: relative;
        width: 3rem;
        height: 3rem;
    }
    .profileInfoFont{
        font-size: 2.2rem;
    }

    .userProfileInfoFooterDiv{
        width: 100%;
        height: 55%;
        display: flex;
        flex-direction: column;
    }

    .userProfileInfoFooterDiv :last-child{
        align-self: center;
        margin-top: auto;
        color: rgba(0, 0, 0, 0.53);
        font-size: 2rem;
        
    }

    .profileButton{
        height: 22%;
        padding-left: 2rem;
        font-size: 2.5rem;
        border-radius: 1rem;
        color: black;
        display: flex;
        align-items: center;
    }

    .userLearningInfoDiv{
        width: 40%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }

    .userLearningDiv{
        width: 100%;
        height: 45%;

        box-shadow: 0px 12px 4px rgba(0, 0, 0, 0.25); 
        border-radius: 4rem;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
    }

    .userLearningDetailDiv{
        height: 50%;
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: space-around;
    }
    .userLearningDetailDiv :first-child{
        width: 30%;
        font-size: 3rem;
        color: black;
    }

    .userLearningDetailDiv :nth-child(2){
        width: 14%;
        font-size: 3rem;
        color: black;
    }

    .userLearningDetailDiv :nth-child(3){
        font-size: 2rem;
        color: rgba(0, 0, 0, 0.53);
    }

    
</style>