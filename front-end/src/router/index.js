import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path : '/login',
        name : 'login',
        meta:{
            keepAlive: true
        },
        component: ()=>import("@/view/login/LoginView.vue")
    },
    {
        path : '/register',
        name : 'register',
        meta:{
            keepAlive: true
        },
        component: ()=>import("@/view/login/RegisterView.vue")
    },
    {
        path : '/verification',
        name : 'verification',
        meta:{
            keepAlive: true
        },
        component: ()=>import("@/view/login/VerificationView.vue")
    },
    {
        path : '/article/:bookNum/:bookId',
        name : 'article',
        component: ()=>import("@/view/main_page/ArticleView.vue")
    },
    {
        path: '/setting/:settingPage',
        name : "setting",
        component: ()=>import("@/view/setting/SettingView.vue")
    },
	{
		path: '/quiz/:quizPageName/:quizIndex',
		name : "quiz",
		component: ()=>import("@/view/quiz/QuizView.vue"),
		beforeEnter:(to, from, next)=>{
			console.log(to);
			if(to.path.indexOf("/quiz-problem") != -1){
				if(localStorage.getItem("quizProblems")!= null){
					next();
				}else{
					next("/quiz/quiz-setting/-1")
				}
			}else{
				next();
			}
		}
	}
]

const router = createRouter({
    history : createWebHistory(),
    routes
});

export default router;