import './assets/main.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from "./router/index.js"
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import store from './assets/store/store.js'
import {doGet, doPost} from '@/assets/utils/axios-util.js';
const app = createApp(App);
app.use(router)
app.mount('#app');
app.use(ElementPlus);
app.use(store)

var c = () =>{
    let w = document.documentElement.clientWidth; /*获取设备宽度*/
    let n = (10 * (w/1440)) > 40 ? 40 + "px" : ((10 * (w/1440)) + "px");
    document.documentElement.style.fontSize = n

    
}

window.addEventListener("load", ()=>{
    c();
    //Record how long that user learning
    // let nowTime = new Date().getTime();
    // let openPageCount = window.localStorage.getItem('openPageCount');
    // openPageCount = openPageCount ? parseInt(openPageCount) : 0;
    // openPageCount += 1
    // localStorage.setItem('openPageCount', openPageCount);
    // refreshTime();
    // let isRightTime = compareRefreshTime();
    // if(Number(localStorage.getItem('startFlag')) && isRightTime){
    //     return;
    // }
    // localStorage.setItem('startTime', nowTime);
    // localStorage.setItem("startFlag", 1);
});

window.addEventListener("resize", c);

// window.addEventListener("beforeunload", ()=>{
//     let nowTime = new Date().getTime();
//     let openPageCount = window.localStorage.getItem('openPageCount');
//     openPageCount -= 1;
//     localStorage.setItem('openPageCount', openPageCount);
//     localStorage.setItem('endTime', nowTime);
//     if(window.location.pathname.indexOf('article') == 1){
//         if(openPageCount == 0){
//             //send data to backend
//             localStorage.setItem("startFlag", 0);
//             let learningTime = localStorage.getItem("learningTime");
//             learningTime = learningTime ? Number(learningTime) : 0;
//             learningTime += nowTime - localStorage.getItem("startTime");
//             localStorage.setItem("learningTime", learningTime);
//             if(learningTime < 60 * 1000){
//                 return;
//             }else{
//                 doPost("/v1/setting//update-learning-time", {
//                     userId : JSON.parse(window.localStorage.getItem("userInfo")).id,
//                     learningTime : Number(learningTime/(60 * 1000))   
//                 }).then((response) => {
//                     console.log(response);
//                     if(response.data.code == 1000){
//                         localStorage.setItem("learningTime", 0);
//                     }
//                 }).catch((err) => {
                    
//                 });
//             }
            
//         }
//     }
    
    
// });

// function compareRefreshTime(){
//    let nowTime = new Date().getTime();
//    let refreshTime = localStorage.getItem("refreshTime");
//    return nowTime - refreshTime < 60 * 1000;
// }

// function refreshTime(){
//     setTimeout(refreshTime ,30 * 1000);
//     let nowTime = new Date().getTime();
//     localStorage.setItem("refreshTime",nowTime);
// }


