import {doPost, doGet} from '@/assets/utils/axios-util.js';

export default{
    namespaced: true,
    actions:{
        getUserLastLoginTime(context, userId){
            doGet("/v1/user/get-user-last-login-time",{
                userId
            }).then((response) => {
                if(response.data.code == 1000){
                    context.commit("UPDATE_LAST_LOGIN_TIME", response.data.data)
                }
            }).catch((err) => {
                console.log(err);   
            });
        }
        
    },
    mutations:{
        UPDATE_LAST_LOGIN_TIME(state, value){
            state.lastLoginTime = new Date(value);
            window.localStorage.setItem("lastLoginTime", state.lastLoginTime);
        },
        UPDATE_IS_CHECKED(state,value){
            state.isChecked = value;
        }
        
    },
    state:{
        checkIn:{
            lastLoginTime: new Date(),
            isChekced : false,
        }
    },
} 