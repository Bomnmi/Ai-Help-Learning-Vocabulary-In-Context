<template>
<div :class="animationClass" class="fontType">
  <div class="indexPageDiv">
    <div class="registerDiv">
        <Tag tagName="Register"/>
        <Input inputName="Email" type="text" @checkEmailFormat="checkEmailFormat">
          <template #inputTips>
            <div class="inputTips" :class="inputTipsAnimationClass" v-show="showInputTips">{{inputTips}}</div>
          </template>
        </Input>
        <Input inputName="Password" type="password" @getPassword="getPassword"/>
        <Input inputName="Confirm your password" type="password" @getConfirmedPassword="getConfirmedPassword"/>
        <Button btnName="Register" fontSize="2.5" @click="registerBtnClick"/>
    </div> 
  </div>
  <Image/>
  <img class="img" style="width: 38.0rem; height: 42.980rem; left: 0rem; top: 12.510rem; position: absolute" src="@/assets/svg/login/svg4.svg" />
  <img class="img" style="width: 38.1rem; height: 41.256rem; left: 30.9rem; top: 28.344rem; position: absolute" src="@/assets/svg/login/svg5.svg" />
  <img class="img" style="width: 40.0rem; height: 45.202rem; left: 31.0rem; top: -5rem; position: absolute" src="@/assets/svg/login/svg6.svg" />
</div>
</template>

<script>
import Input from "@/components/login/Input.vue"
import Tag from "@/components/login/Tag.vue"
import Button from "@/components/login/Button.vue"
import Image from "@/components/login/Image.vue"
import {verifyEmail, encryptPassword, verifyPassword} from '@/assets/utils/util-method.js';
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import {doPost} from '@/assets/utils/axios-util.js';
export default {
    name : "RegisterView",
    components:{Input, Tag, Button, Image},
    setup(){
        let animationClass = ref("fadeIn")
        const router = useRouter()

        let inputTipsAnimationClass = ref("")
        let inputTips = ref("");
        let registerParams = {
          registerEmail: "",
          registerPassword: "",
          confirmedPassword:"",
          encryptPassword: "",
        };

        let showInputTips = ref(false);
        watch(inputTips, (newValue, oldValue)=>{
          showInputTips.value = false;
          if(newValue === ""){
            inputTipsAnimationClass.value = ""
          }else if(oldValue === "") {
            inputTipsAnimationClass.value = "bounceIn"
            setTimeout(() => {
              showInputTips.value = true;
            }, 100);
          }else{
            inputTipsAnimationClass.value = "shakeX"
            setTimeout(() => {
              showInputTips.value = true;
            }, 1);
          } 
        })
        function checkEmailFormat(email){
          let verificationResult = verifyEmail(email);
          console.log(verificationResult);
          if(!verificationResult.valid){
            inputTips.value = verificationResult.message;
            return;
          }else{
            doPost("/v1/user/email-check", {
              registerEmail: email
            }).then((response) => {
              const responseData = response.data;
              if(responseData.code != 1000){
                inputTips.value = responseData.message;
              }
            }).catch((err) => {
              
            });
            inputTips.value = "";
            registerParams.registerEmail = email;
          }
        }

        function getPassword(password){
          const verificationResult = verifyPassword(password);
          if(!verificationResult.valid){
            inputTips.value = verificationResult.message;
          }else{
            inputTips.value = ""; 
            registerParams.registerPassword = password;
          }
        }

        function getConfirmedPassword(confirmedPassword){ 
          const verificationResult = verifyPassword(confirmedPassword);
          if(confirmedPassword != registerParams.registerPassword){
            inputTips.value = "Please enter the same password";}
          else if(!verificationResult.valid){
            inputTips.value = verificationResult.message;
          }else{
            inputTips.value = "";
            registerParams.confirmedPassword = confirmedPassword;
          }
        }

        function registerBtnClick(){
            const verificationEmail = verifyEmail(registerParams.registerEmail);
            const verificationPassword = verifyPassword(registerParams.registerPassword);
            const verificationConfirmedPassword = verifyPassword(registerParams.confirmedPassword);
            if(!verificationEmail.valid){
              inputTips.value = verificationEmail.message;
              return;
            }
            if(!verificationPassword.valid){
              inputTips.value = verificationPassword.message;
              return;
            }
            if(!verificationConfirmedPassword.valid){
                inputTips.value = verificationConfirmedPassword.message;
                return;
            }
            //cc4cf51348522fa1879ff6cd960b9799
            //Pass all verification, request a 4 digit code
            //cache user's information into local storage
            window.localStorage.setItem("registerEmail", registerParams.registerEmail);
            window.localStorage.setItem("registerPassword", encryptPassword(registerParams.registerPassword));
            //set a expire time for 15 minutes
            window.localStorage.setItem("codeExpiration", Date.now() + 15 * 60 * 1000);
            doPost("/v1/user/send-verification-code", {
              registerEmail: registerParams.registerEmail,
            }).then((response) => {
              console.log(response);
              const responseData = response.data;
              if(responseData.code!= 1000){
                inputTips.value = responseData.message;
              }else{
                router.push({
                  path : "/verification"
                })
              }
            }).catch((err) => {
              console.log(err); 
            })
        }
        return {animationClass, registerBtnClick,inputTipsAnimationClass
        ,showInputTips, inputTips,checkEmailFormat, getPassword,getConfirmedPassword
        }
    }
}
</script>

<style>
@import "@/assets/css/font.css";
.registerDiv{
    top: 10rem;
    position: absolute;
    width: 63.1rem;
    height: 50rem;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-around;
  }

  .inputTips{
    color: red; font-size: 2rem; font-family: Fondamento; font-weight: 400; word-wrap: break-word;
  }
</style>