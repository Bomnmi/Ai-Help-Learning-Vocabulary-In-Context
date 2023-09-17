<template>
  
  <div>
    <PageHeader/>
    <div class="articleMainPageDiv">
      <div class="articleDiv  mainPageColor">
        <div v-if="!showParagraph">
          <div class="articleHeaderDiv">
            <!-- Article Title -->
            <Title title="Today's Articles"/>
          </div>
          <el-scrollbar>
            <div class="articleBodyDiv" :class="showBookList">
              <!-- Ariticle Body -->
              <AddArticle v-if="booksNumber == 0" @addArticle="addArticle"/>
              <Book @click="chooseOneBook(item)" v-else v-for="item in articleInfo.articles" :key="item.id" 
              class="book" :title="item.title" :content="item.markedContent" :id="item.id"
              :keyWords="articleInfo.wordList[item.id]"/>
            </div>
          </el-scrollbar>
        </div>

        <!-- Paragraph Content -->
        <div class="paragraphDiv" v-if="showParagraph">
          <div class="paragraphTitle">
            {{paragraphTitle}}
          </div>
          <p class="translatedContentP" v-if="showTranslatedContent">{{translatedContent}}</p>
          <div class="paragraphContent">
            <p @click="chooseOneWord($event, index)" 
              @mouseover="selectOneWord" 
              @mouseleave="unSelectOneWord" 
              v-for="(item,index) in contentArr" :key="index"
              class="paragraphWord" :class="item[1] == 1 ? 'highLightWord' : ''">
              {{item[0]}}&nbsp;</p>
          </div>
          <Button class="tranlationButtonClass" width="16rem" fontType="fontType" fontSize="2" :btnName="showTranslatedContent ? 'Origin': 'Translation'" @click="switchTranslateContent">

		  </Button>
          <Button 
            class="finishReadingButtonClass" 
            width="15rem" 
            fontType="fontType" 
            fontSize="2" 
            btnName="Finish" 
            @click="finishReading">
          </Button>
        </div>
      </div>
      <div class="wordDiv  mainPageColor">
        <TodayWordList/>
      </div>

      <WordModal :modalWord="modalWord"
        v-show="showModal"
        id="wordModal" 
        @close="closeModal" ref="wordModal" :isReviewedWord="isReviewedWord"/>
    </div>
  </div>
</template>

<script>
import PageHeader from '../../components/main_page/PageHeader.vue'
import { computed, isReactive, onMounted, onBeforeUnmount, reactive, ref, toRefs, watch, onUnmounted } from 'vue'
import AddArticle from '@/components/main_page/AddArticle.vue'
import Title from '@/components/main_page/Title.vue'
import Book from '@/components/main_page/Book.vue'
import TodayWordList from '@/components/main_page/TodayWordList.vue'
import WordModal from '../../components/main_page/WordModal.vue'
import {remToPx, pxToRem, locateModal, closeOutSide, useMapState} from '@/assets/utils/util-method.js'
import {useStore } from 'vuex'
import { useRoute, useRouter } from 'vue-router'
import {doGet, doPost} from '@/assets/utils/axios-util.js';
import Button from '../../components/login/Button.vue'
import { ElNotification, ElMessage } from 'element-plus'
export default {

  name : 'Article',
  components : {Title, AddArticle,TodayWordList, Book, PageHeader, WordModal,Button},
  setup(){
    
    //use vuex to initialize state
    const store = useStore();
    const state = useMapState('articleParam', ['articleInfo', 'lemmatizationAddedWordSet', 'lemmatizationWord']);

    const router = useRouter();
    const route = useRoute();
    let animationClass = ref("fadeIn")

    let booksNumber = computed(function(){
      return state.articleInfo.value.booksNumber;
    })

    let showBookList = computed(()=>{
      return booksNumber.value != 0 ? "bookList" : "createBook"

    })
     

    let paragraphTitle = ref("");
    let contentArr = ref([]);

    let paragraphWordClass = ref("");
    let showParagraph = ref(false);
    let showTranslatedContent = ref(false);

    //modal setting
    let showModal = ref(false);
    let modalWord = reactive([[]]);
    let wordModal = ref(null);
    let isReviewedWord = ref(true);

    //userInfo
    let userInfo = JSON.parse(window.localStorage.getItem("userInfo"));

    let translatedContent = ref("");
    //Added word(unreviewed)
    let lemmatizationWordsSet = computed(()=>{
     	 return state.lemmatizationAddedWordSet.value;
    });

	//count the user's learning time.
	const startTime = ref(new Date());
	let isUserLearningTimeSent = ref(false);

	const handleBeforeUnload = () =>{
		const endTime = new Date();
		const duration = endTime - startTime.value;
		//Give a flag, if user exit this component by switching, then we can send
		//	request in onUnmounted
		isUserLearningTimeSent.value = true;
		//send to server
		handleUserLearningTime(duration);
	}

	function handleUserLearningTime(learningTime){
		//if store has learning time, add together
		const lastTimeLearningTime = localStorage.getItem("learningTime");
		if(lastTimeLearningTime != null){
			learningTime += Number(lastTimeLearningTime);
		}
		if((learningTime / 60000) > 1){
			//convert learning time to minutes
			learningTime = (learningTime / 60000);
			//send to server
			doPost("/v1/setting/update-learning-time",{
				userId : userInfo.id,
				learningTime
			}).then((result) => {
				console.log(result);
			}).catch((err) => {
				
			});
			localStorage.removeItem("learningTime");
			return;
		}
		//restore the learning time
		localStorage.setItem("learningTime", learningTime);
	}

    //control the modal
    function closeModal(){
		modalWord[0] = [];
		showModal.value = false;
    }

    const onClickOutside = closeOutSide(showModal, closeModal, '#wordModal')
	
    onMounted(()=>{
		store.dispatch('articleParam/getParagraph', userInfo.id);
		if(route.params.id != 0){
			router.push('/article/'+route.params.bookNum+'/0');
		}
		//When mount coumponet, add onClickOutside into event listner
		document.addEventListener('click', onClickOutside, true);
		//add beforeunload listner
		window.addEventListener("beforeunload", handleBeforeUnload);
    })

    onBeforeUnmount(()=>{
		//count the time if user switch the component.
		const endTime = new Date();
		const duration = endTime - startTime.value;
		if(!isUserLearningTimeSent.value){
			handleUserLearningTime(duration);
		}
		

		document.removeEventListener('click', onClickOutside, true);
		window.removeEventListener("beforeunload", handleBeforeUnload);
    })
    

    function switchTranslateContent(){
		if(lemmatizationWordsSet.value.size != 0){
			return;
		}
      	showTranslatedContent.value = !showTranslatedContent.value;
    }

    function addArticle(){
		//send request to server, get articles
		doGet("/v1/article/generate-articles", {
			userId : userInfo.id
		}).then((response) => {
			let responseData = response.data;
			if(responseData.code == 1000){
			//get all article
			store.dispatch('articleParam/getParagraph', userInfo.id)
			router.push("/article/" + booksNumber.value + "/0");
			}
		}).catch((err) => { 
		});   
    }

    function finishReading(){
      //send request to server, update article, set correponding vocabulary to be unused
      if(lemmatizationWordsSet.value.size != 0){
        //If there are some words need to be reviewed, can not finish reading
          ElMessage({
            type: 'warning',
            message: 'There are some words need to be reviewed',
            grouping:true,
          })
          return;
      }
      store.dispatch("articleParam/finishReading", {
        userId : userInfo.id,
        paragraphId : route.params.bookId
      });
    }
  

    //control the word
    function chooseOneWord(event, index){
      isReviewedWord.value = false;
      //Check if this word is added word or not
      store.dispatch('articleParam/getLemmatizationWord', contentArr.value[index][0]).then(res=>{
        if(lemmatizationWordsSet.value.has(state.lemmatizationWord.value)){
          isReviewedWord.value = true;
        }
        modalWord[0] = [state.lemmatizationWord.value, index];
        //Get the abosolute coordinate of selected word
        locateModal(event, wordModal, rect=>{
          let height = store.state.modalParam.wordModalHeight;
          let x = pxToRem(rect.x + rect.width);
          let y = pxToRem(rect.y) - height;
          y = y < 0 ? 0 : y;
          return {x, y} 
        });
        //open modal
        //showModal.value = true;
      })
    }

    function selectOneWord(event){
      event.target.classList.add('paragraphWordForcus');
    }
    function unSelectOneWord(event){
      event.target.classList.remove('paragraphWordForcus');
    }


    //set paragraph title and content
    function chooseOneBook(bookInfo){
      //get lemmatization of added words
      store.dispatch('articleParam/getLemmatizationWordList', bookInfo.id);
      paragraphTitle.value = bookInfo.title;
      showParagraph.value = true;
      let content = bookInfo.markedContent;
      content.split(" ").forEach((word, index)=>{
        if(word.charAt(word.length - 1) == '#'){
          word = word.substring(0, word.length - 1);
          contentArr.value.push([word,1]);
        }else{
          contentArr.value.push([word, 0]);
        }
      })
      let newUrl = route.fullPath.substring(0, route.fullPath.length - 1) + bookInfo.id;
      translatedContent.value = bookInfo.translationContent;
      router.push(newUrl);
    }

    let trigger = computed(()=>{
      return store.state.wordModal.trigger;
    })

    watch(trigger,()=>{
      showModal.value = true
    })

    // watch(modalWord,()=>{
    //   showModal.value = true
    // })

    watch(booksNumber, (newValue, oldValue)=>{
      router.push("/article/" + booksNumber.value + "/0");
    })

    watch(route, (newValue)=>{
      if(newValue.params.bookId == 0){
        contentArr.value = [];
        showParagraph.value = false;
        showModal.value = false;
        store.commit("articleParam/CLEAR_TRANSLATED_ARTICLE", "");
		store.commit("articleParam/DELETE_LEMMATIZATION_SET");
        showTranslatedContent.value = false;
      }
    })

    return {
      animationClass,
      booksNumber, 
      showBookList, 
      addArticle, 
      contentArr, 
      chooseOneWord, 
      paragraphWordClass, 
      selectOneWord, 
      unSelectOneWord,
      showModal,
      modalWord,
      chooseOneBook,
      paragraphTitle,
      showParagraph,
      closeModal,
      wordModal,
      route,
      lemmatizationWordsSet,
      isReviewedWord,
      ...state,
      switchTranslateContent,
      showTranslatedContent,
      trigger,
      finishReading,
      translatedContent
    }
  }
}
</script>

<style scoped>
  @import "@/assets/css/main_page.css";
  @import "@/assets/css/animation.css";
  
  .paragraphDiv{
    
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
  }
  .tranlationButtonClass{
    align-self: flex-end;
    position: absolute;
    bottom: 2rem;
    left : 2rem;
    margin-right : 2rem;
  }
  .finishReadingButtonClass{
    align-self: flex-end;
    position: absolute;
    bottom: 2rem;
    margin-right : 2rem;
  }

  .paragraphWord{
    height: 10%;
    cursor: pointer;
  }

  .paragraphWordForcus{
    color: rgb(41, 152, 227);
    text-decoration: underline;
  }
  .paragraphTitle{
    display: flex;
    padding-top:1rem;
    justify-content: center;
    align-items: center;
    font-size: 3.4rem; 
    font-family: Georgia, "Times New Roman", Times, serif;; 
    font-weight: 400; 
    word-wrap:break-word
  }

  .paragraphContent{
    flex: 1 1 0;
    padding-left: 3rem;
    padding-right: 3rem;
    margin-top: 2rem;
    display: flex;
    flex-wrap: wrap;
    align-content: flex-start;
  }

  .paragraphContent p{
    font-size: 2.2rem; 
    font-family: Georgia, "Times New Roman", Times, serif;; 
    font-weight: 400; 
    word-wrap: break-word;
  }
  .articleMainPageDiv{
    top: 4rem;
    height: 52rem;
    position: relative;
    display: flex;
    justify-content: space-between; 
  }
  .articleDiv{
      border-radius: 2.0rem; 
	  height: 52rem;
      width: 110rem;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
  }
  .articleBodyDiv{
    display: flex;
	height: 40rem;

  }
  .bookList{
    align-items: center;  
  }
  .createBook{
    flex-direction: column;
    align-items: center;
	margin-top: 5rem;
  }

  .book{
    margin-left:5rem;
    margin-right: 4rem;
  }

  .wordDiv{
    width: 25rem;
    display: flex;
    justify-content: space-around;
	border-radius: 2rem;
  }

  .translatedContentP{
    padding-left: 3rem;
    padding-right: 3rem;
    font-size: 1.8rem; 
    font-family: Arial; 
    font-weight: 400; 
    word-wrap: break-word;
  }

  .highLightWord{
    color: yellow;
  }
</style>