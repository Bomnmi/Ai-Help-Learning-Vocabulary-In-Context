<template>
<div :class="animationClass" class="fontType" ref="root">
  <div class="indexPageDiv">
    <div class="loginDiv">
      <Tag tagName="Login"/>
      <Input inputName="Email" type="text" @checkEmailFormat="checkEmailFormat">
        <template #inputTips>
          <div class="inputTips" :class="inputTipsAnimationClass" v-show="showInputTips">{{inputTips}}</div>
        </template>
        
      </Input>

      <Input inputName="Password" type="password" @getPassword="getPassword"/>
      <Button btnName="Login" fontSize="2.5" @click="login" fontType="fontType" ref="loginBtn"/>

      <div class="registerBtnDiv">
        <Line length="15"/>
        <div class="registerBtnFrame" @mouseover="registerLinkClass = 'registerLinkOnForcus'" @mouseleave="registerLinkClass = 'registerLink'">
          <div :class="registerLinkClass" @click="changeToRegister">Not have a account? Register here</div>
        </div>
        <Line length="15"/>
      </div>
    </div>
  </div>
  <Image/>
  <img class="img" style="width: 40.0rem; height: 35.980rem; left: 0rem; top: 20.510rem; position: absolute" src="@/assets/svg/login/svg1.svg" />
  <img class="img" style="width: 27.1rem; height: 32.256rem; left: 41.9rem; top: 32.344rem; position: absolute" src="@/assets/svg/login/svg3.svg" />
  <img class="img" style="width: 36.0rem; height: 33.202rem; left: 25.0rem; top: -2rem; position: absolute" src="@/assets/svg/login/bulb.svg" />
</div>

</template>

<script>
import Input from "@/components/login/Input.vue"
import Tag from "@/components/login/Tag.vue"
import Button from "@/components/login/Button.vue"
import Image from "@/components/login/Image.vue"
import Line from "@/components/common/Line.vue"

import { onMounted, onUpdated, reactive, ref, unref, watch } from 'vue'
import { useRouter } from 'vue-router'
import {verifyEmail, encryptPassword, verifyPassword} from '@/assets/utils/util-method.js';
import {doPost} from '@/assets/utils/axios-util.js';
export default {
    name : "LoginView",
    components:{Input, Tag, Button, Image, Line},
    setup(){
      let animationClass = ref("fadeIn")
      const root = ref(null)
      const router = useRouter()
      let registerLinkClass = ref("registerLink")
      let inputTipsAnimationClass = ref("")
      let inputTips = ref("");
      const loginBtn = ref(null);

      let loginParams = {
        loginEmail: "",
        loginPassword: ""
      };

      let showInputTips = ref(false);

      function changeToRegister(){
        router.push("/register")
      }

      function registerButtonOnFocus(){
        registerLinkClass.value = "registerLinkOnForcus"
      }

      function registerButtonLostFocus(){
        registerLinkClass.value = "registerLink"
      }


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
        if(!verificationResult.valid){
          inputTips.value = verificationResult.message;
        }else{
          inputTips.value = "";
        }
        loginParams.loginEmail = email;
      }

      function getPassword(password){
        const verificationResult = verifyPassword(password);
        if(!verificationResult.valid){
          inputTips.value = verificationResult.message;
        }else{
          inputTips.value = "";
        }
        loginParams.loginPassword = password;
        
      }

      function login(){
        let verificationEmailResult = verifyEmail(loginParams.loginEmail);
        let verificationPasswordResult = verifyPassword(loginParams.loginPassword);
        if(!verificationEmailResult.valid){
          inputTips.value = verificationEmailResult.message;
          return;
        }
        if(!verificationPasswordResult.valid){
          inputTips.value = verificationPasswordResult.message;
          return;
        }
        
        //send post request
        doPost("/v1/user/login", {
          loginEmail: loginParams.loginEmail,
          //encrpt password using md5
          loginPassword: encryptPassword(loginParams.loginPassword)
        }).then((response) => {
          const responseData = response.data;
          console.log(responseData);
          if(responseData.code === "1000"){
            //store the userinfo into local storage
            window.localStorage.setItem("userInfo", JSON.stringify(responseData.data));
            //store the token into local storage
            window.localStorage.setItem("token", responseData.accessToken);
            router.push("/article/0/0");
          }else{
            inputTips.value = responseData.message;
          }
        })
      }

      onMounted(()=>{
        window.addEventListener("keydown", (e) => {
          if(e.key === "Enter"){
            login();
          }
        });
      })
      return {
        changeToRegister,
        animationClass, 
        root, 
        registerButtonOnFocus, 
        registerLinkClass, 
        registerButtonLostFocus,
        checkEmailFormat,
        inputTips,
        getPassword,
        inputTipsAnimationClass,
        showInputTips,
        login,
        loginBtn
        }
    }
  
}
</script>

<style scoped>
  @import "@/assets/css/animation.css";
  @import "@/assets/css/font.css";
  .img{
    max-width: 100%;
    top: 10rem;
    position:absolute
  }
  .loginDiv{
    top: 14rem;
    position: absolute;
    width: 63.1rem;
    height: 44rem;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-around;
  }

  .registerBtnDiv{
    width: 58.9rem; 
    height: 2.133rem;
    display: flex;
    align-items: center;
    justify-content: space-around;
  }

  .registerBtnFrame{
    width: 28.2rem; height: 2.13rem; justify-content: center; align-items: center; gap: 10px; display: inline-flex;
    cursor:pointer
  }
  .registerLink{
    color: black; font-size: 1.6rem; word-wrap: break-wor
  }

  .registerLinkOnForcus{
    color: rgb(41, 152, 227); font-size: 1.6rem;  word-wrap: break-wor;
    text-decoration: underline;
  }

  .inputTips{
    color: red; font-size: 2rem;  word-wrap: break-word;
  }
</style>

  
