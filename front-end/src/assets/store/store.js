import { createStore } from "vuex";
import todayWord from "./modules/today_word";
import modalParam from "./modules/modal_param";
import registerModule from "./modules/register_module";
import articleParam from "./modules/article_param";
import wordModal from "./modules/word_modal";
import headerButton from "./modules/header_button";
import setting from "./modules/setting";
import quiz from "./modules/quiz";
const store = createStore({
    modules:{todayWord, modalParam, registerModule,articleParam, wordModal,headerButton, setting, quiz}
    
})

export default store;