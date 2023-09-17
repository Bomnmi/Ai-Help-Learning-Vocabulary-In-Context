import md5 from "js-md5";
import { computed } from "vue"
import { mapState, useStore } from "vuex"

function useMapState(module, state) {
    // 1. 获取实例 $store
    const store = useStore()
    // 2. 遍历状态数据
    const storeStateFns = mapState(module, state)
    // 3. 存放处理好的数据对象
    const storeState = {}
    // 4. 对每个函数进行computed
    Object.keys(storeStateFns).forEach(fnKey => {
        const fn = storeStateFns[fnKey].bind({ $store: store })
        // 遍历生成这种数据结构 => {name: ref(), age: ref()}
        storeState[fnKey] = computed(fn)
    })
    return storeState
}

function remToPx(rem){
    let n = getRootFontSize();
    return rem * n;
}

function getRootFontSize(){
    let w = document.documentElement.clientWidth; /*获取设备宽度*/
    //1 rem = root fontsize
    let n = (10 * (w/1440)) > 40 ? 40 : ((10 * (w/1440))); //This is the root fontsize
    return n;
}
function pxToRem(px){
    let n = getRootFontSize();
    return px / n;
}

function verifyEmail(email){
    var re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    let returnValue = {
        valid: re.test(email),
        message: ""
    };
    if(!returnValue.valid){
        if(email === ""){
            returnValue.message = "Email is required";
        }else{
            returnValue.message = "Invalid email";
        }
    }
    return returnValue;
}

function verifyPassword(password){
    const reg = /^(?=.*[A-Z]).+$/
    let returnValue = {
        valid: false,
        message: ""
    };
    if(password.length < 6){
        returnValue.message = "Password must be at least 6 characters";
    }else if(password.length > 20){
        returnValue.message = "Password must be less than 20 characters";
    }else if(!reg.test(password)){
        returnValue.message = "Password must contain at least one uppercase letter";
    }else{
        returnValue.valid = true;
    }
    return returnValue;
}

function encryptPassword(password){
    return md5(password);
}

function getDateDiff(date1, date2){
    let timeDiff = Math.abs(date1 - date2);
    return Math.floor(timeDiff/ (1000*60*60*24));
}
function compareDate(date1, date2){
	let timeDiff = date1 - date2;
    return Math.floor(timeDiff/ (1000*60*60*24));

	
}

function compareMonth(date1, date2){
	return date1.getFullYear() == date2.getFullYear() && date1.getMonth() == date2.getMonth();
}


function parsePageNameToUrlForm(name){
	let result = "";
	for(let i = 0; i < name.length; i++){
		if(/^[A-Z]$/.test(name.charAt(i))){
			result += "-";
			result += name.charAt(i).toLowerCase();
		}else{
			result += name.charAt(i);
		}
	}
	return result;
}

function parseUrlFormToVueName(name){
	let result = name.charAt(0).toUpperCase();
	for(let i = 1; i < name.length; i++){
		if(name.charAt(i) == "-"){
			i++;
			result += name.charAt(i).toUpperCase();
		}else{
			result += name.charAt(i);
		}
	}
	return result;
}

function parseDate(date){
    // 获取年、月和日
    const year = date.getFullYear();
    const month = date.getMonth() + 1; // 月份从0开始，所以要加1
    const day = date.getDate();

    // 格式化为字符串，保留年、月和日
    const formattedDate = `${year}-${month < 10 ? '0' : ''}${month}-${day < 10 ? '0' : ''}${day}`;
    return formattedDate;
}
function parseDateWithoutYear(date){
    // 获取年、月和日
    const month = date.getMonth() + 1; // 月份从0开始，所以要加1
    const day = date.getDate();

    // 格式化为字符串，保留年、月和日
    const formattedDate = `${month < 10 ? '0' : ''}${month}-${day < 10 ? '0' : ''}${day}`;
    return formattedDate;
}

function parseMonth(date){
	// 获取年、月和日
    const year = date.getFullYear();
    const month = date.getMonth() + 1; // 月份从0开始，所以要加1

    // 格式化为字符串，保留年、月和日
    const formattedDate = `${year}-${month < 10 ? '0' : ''}${month}`;
    return formattedDate;
}

function getWeekday(year, month, day){
	var d=  new Date();
	d.setFullYear(year, month - 1, day);
	return d.getDay();
}

function getNumberOfDays(year, month){
	var d= new Date();
	d.setFullYear(year, month, 0);
	return d.getDate();
}


/**
 * This function is used to locale the modal window
 * @param {*} event The event we operate
 * @param {*} modalRef The proxy object of modal
 * @param {*} convert  Pass a function in, to give the x and y that modal window want to locate.
 */
function locateModal(event, modalRef, convert){
    const rect = event.target.getBoundingClientRect();
    let element = modalRef.value.$el;
    let corrdinate = convert(rect, element)
    element.style.left = corrdinate.x + 'rem';
    element.style.top = corrdinate.y + 'rem';
}

function closeOutSide(showModal, closeModal, selector){
    return (event)=>{
        if(showModal.value && !event.target.closest(selector)){
            closeModal(event);
        }
    }
}

function changeSvgColor(svgElement, color){
    let pathElements = svgElement.querySelectorAll('path')
    pathElements.forEach((item)=>{
        item.style.fill = color
    })
}

export {
    remToPx, 
    pxToRem,
    locateModal, 
    closeOutSide,
    changeSvgColor, 
    verifyEmail,
    encryptPassword, 
    verifyPassword,
    useMapState,
    getDateDiff,
    parseDate,
	parsePageNameToUrlForm,
	parseUrlFormToVueName,
	getNumberOfDays,
	getWeekday,
	compareDate,
	compareMonth,
	parseMonth,
	parseDateWithoutYear
}