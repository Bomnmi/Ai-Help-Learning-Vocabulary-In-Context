import axios from "axios";
import qs from "qs";
import { ElNotification, ElMessage } from 'element-plus'

axios.defaults.baseURL = "http://localhost:8001/ai/";
axios.defaults.timeout = 50000;

//get request
function doGet(url,params){
    return axios({
        url,
        method:"get",
        params
    })
}

function doPost(url,data){
    let requestData = qs.stringify(data);
    return axios.post(url,requestData)
}

function doPostJson(url, params){
	return axios({
		url,
		method:"post",
		data : params
	})
}

//request interceptor
axios.interceptors.request.use(config => {
    //when access the page that need after user login, add token in the request
    console.log(config.url);
    if(config.url.indexOf("user") == -1){
        let token = localStorage.getItem("token");
        let userInfo = localStorage.getItem("userInfo");
        if(token && userInfo){
            //add token and uid in header
            config.headers["Authorization"] = "Bearer " + token;
            config.headers["uid"] = JSON.parse(userInfo).id;
        }
    }
    return config;
}, error=>{
    console.log("request interceptor error" + error);
})

//response interceptor
axios.interceptors.response.use(response => {
    if(response && response.data.code >= 2000){
        let code = response.data.code;
        if(code == 5001 || code == 5002){
            //token invalid, login again
            window.location.href = "/login"
        }
    }
    return response;
},error => {
    console.log("response interceptor error" + error);
    //back to the error page
    //window.location.href = "/error";
})

export {
    doGet,
    doPost,
	doPostJson
}