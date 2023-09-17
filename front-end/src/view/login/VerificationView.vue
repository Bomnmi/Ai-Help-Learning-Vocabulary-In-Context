<template>
  <div :class="animationClass" class="fontType">
    <!-- The External Div -->
    <div class="indexPageDiv">
        <div class="verificationDiv">
            <Tag tagName="Verify Your Email Address"/>
            <Line length="53.1"/>
            <div id="verficationCodeTitleDiv">
                A verification code has been sent to {{registerEmail}}
            </div>
            <div id="verficationCodeDescriptionDiv">
                Please check your inbox and the verfication code below to verify youar email address. 
                {{codeExpirationTips}} <span v-if="countdown > 0">{{minute}}:{{second}}</span>.
            </div>
            <div id="verificationCodeDiv" @keydown="autoChange">
                <input type="text" class="verificationCodeInput" maxlength="1" ref="input1" @focus="inputFocus(1)">
                <input type="text" class="verificationCodeInput" maxlength="1" ref="input2" @focus="inputFocus(2)">
                <input type="text" class="verificationCodeInput" maxlength="1" ref="input3" @focus="inputFocus(3)">
                <input type="text" class="verificationCodeInput" maxlength="1" ref="input4" @focus="inputFocus(4)">                     
            </div>
            <Button fontSize="2.5" btnName="Verify" @click="verifyCode"/>
            <div id="verificationCodeManipulateDiv">
                <div class="verificationCodeManipulateFont" @click="resendCode">Resend code</div>
                <div class="verificationCodeManipulateFont" @click="changeEmail">Change email</div>
            </div>
        </div>
    </div>

    <Image/>
    <img style="width: 30.0rem; height: 32.0rem; left: 10.4rem; top: 4rem; position: absolute" src="@/assets/svg/login/star_eyes.svg" />
    <img style="width: 32.0rem; height: 29.0rem; left: 40.8rem; top: 2.4rem; position: absolute" src="@/assets/svg/login/ye.svg" />
    <img style="width: 26.8rem; height: 29.0rem; left: 5.0rem; top: 37.8rem; position: absolute" src="@/assets/svg/login/blow.svg" />
    <img style="width: 35.0rem; height: 37.0rem; left: 41.8rem; top: 30.2rem; position: absolute" src="@/assets/svg/login/show.svg" />
    
  </div>
</template>

<script>
import Tag from '@/components/login/Tag.vue'
import Line from '@/components/common/Line.vue'
import Button from '@/components/login/Button.vue'
import { computed, onMounted, ref, watch } from 'vue'
import Image from '@/components/login/Image.vue'
import {doPost} from '@/assets/utils/axios-util.js';
import { useRouter } from 'vue-router'
import { ElNotification, ElMessage } from 'element-plus'
export default {
    name :"VerificationView",
    components: {Tag, Line, Button, Image},
    setup(){
        let animationClass = "fadeIn"
        const input1 = ref(null);
        const input2 = ref(null);
        const input3 = ref(null);
        const input4 = ref(null);
        const inputs = [input1, input2, input3, input4];
        const router = useRouter();
        let countdownInterval;
        let index = 1;
        let registerEmail = ref(window.localStorage.getItem("registerEmail"));
        let countdown =ref(Math.floor((window.localStorage.getItem('codeExpiration') - Date.now())/1000));
        let codeExpirationTips = ref("The code will expire in");
        onMounted(()=>{
            countdownInterval = setInterval(() => {
                countdown.value--;
            }, 1000);
            input1.value.focus();
                       
        })

        watch(countdown, (newValue)=>{
            if(newValue <= 0){
                codeExpirationTips.value = "Press resend code to resend a new verification code";
                clearInterval(countdownInterval);
            }
        })

        let minute = computed(()=>{
            if(countdown.value <= 0){
                return "0" + 0;
            }
            return Math.floor(countdown.value / 60);
        })
        let second = computed(()=>{
            if(countdown.value <= 0){
                return "0" + 0;
            }
            if(countdown.value % 60 < 10){
                return "0" + countdown.value % 60;
            }
            return countdown.value % 60;
        })

        function autoChange(event){
            if(/^[a-zA-Z0-9]$/.test(event.key) || event.key === "Backspace" || event.key === "Enter"){
                if(event.key === "Backspace"){
                    event.preventDefault();
                    console.log(index-1);
                    inputs[index - 1].value.value = "";
                    index = index - 1 < 1 ? 1 : index - 1; 
                    inputs[index - 1].value.focus();
                }else if(event.key !== "Enter"){
                    setTimeout(()=>{
                        index = index + 1 > 4 ? 4 : index + 1; 
                        inputs[index - 1].value.focus();
                    }, 30);
                }else{
                    verifyCode();
                }
            }
        }
        function inputFocus(i){
            index = i;
        }

        function verifyCode(){
            let verficationCode = input1.value.value + input2.value.value + input3.value.value + input4.value.value;
            if(verficationCode.length < 4){
                ElMessage({
					type: 'warning',
					message: 'Please enter verification code',
					grouping:true,
				})
                return;
            }
            doPost("/v1/user/register",{
                registerEmail : registerEmail.value,
                registerPassword : window.localStorage.getItem("registerPassword"),
                verificationCode: verficationCode
            }).then((response) => {
                let responseData = response.data;
                if(responseData.code == 1000){
                    //cleare local storage
                    window.localStorage.removeItem("registerEmail");
                    window.localStorage.removeItem("registerPassword");
                    window.localStorage.removeItem("codeExpiration");
					window.localStorage.removeItem("token");
                    router.push("/article/0/0");
                }
            }).catch((err) => {
                
            });
        }
        
        function resendCode(){
            doPost("v1/user/send-verification-code",{
                registerEmail : registerEmail.value
            }).then((response) => {
                console.log(response);
            }).catch((err) => {
                
            });
        }

        function changeEmail(){
            router.go(-1);
        }
        return {
            animationClass, 
            input1, 
            autoChange,
            registerEmail,
            countdown,
            minute,
            second,
            input2,
            input3,
            input4,
            verifyCode,
            codeExpirationTips,
            inputFocus,
            resendCode,
            changeEmail
        }
    }

}
</script>

<style>
@import "@/assets/css/animation.css";
@import "@/assets/css/font.css";
    .verificationDiv{
        width: 62.1rem;
        top:3rem;
        position:absolute;
        height: 57rem;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
    }

    #verficationCodeTitleDiv{
        color: black; font-size: 2.8rem; font-weight: 400; word-wrap: break-word;
        text-align: center;
    }

    #verficationCodeDescriptionDiv{
        color: rgba(0, 0, 0, 0.61); font-size: 2.4rem; font-weight: 400; word-wrap: break-word;
        text-align: center;
    }

    #verificationCodeDiv{
        height: 10rem;
        width: 62.1rem;
        display: flex;
        justify-content: space-around;
        align-items: center;
    }

    .verificationCodeInput{
        height: 8rem;
        width: 8rem;
        
        font-size: 4rem;
        text-align: center;
        caret-color: #ffffff;
    }

    #verificationCodeManipulateDiv{
        height: 3rem;
        width: 62.1rem;
        display: flex;
        justify-content: space-around;
    }

    .verificationCodeManipulateFont{
        color: #FF6161; font-size: 2.4rem; font-family: Fondamento; font-weight: 400; word-wrap: break-word;
    }

    .verificationCodeManipulateFont:hover{
        cursor: pointer;
        color: rgb(41, 152, 227);
        text-decoration: underline;
    
    }
</style>