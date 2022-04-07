function titleInput(input,h_title,max) {
    // alert($("#title").val().length);
    var i=$("#"+input).val().length;
    var h_t=document.getElementById(h_title);
    h_t.innerText="字数限制"+max+"字,已输入："+i+"字";
    if (i>max){
        $("#"+input).parent().removeClass("has-success").addClass("has-error");
        h_t.style.color="red";
    }else {
        $("#"+input).parent().removeClass("has-error").addClass("has-success");
        h_t.style.color="#4e4f6b";
    }
    var v=$("#"+input).val()
}
function set_select_checked(selectId, checkValue){
    var select = document.getElementById(selectId);
    for (var i = 0; i < select.options.length; i++){
        if (select.options[i].value == checkValue){
            select.options[i].selected = true;
            break;
        }
    }
}
//获取时间戳
function getTime() {
    var date = new Date();
    var year = date.getFullYear();    //获取当前年份
    var mon = date.getMonth()+1;      //获取当前月份   js从0开始取
    var da = date.getDate();          //获取当前日
    var h = date.getHours();          //获取小时
    var m = date.getMinutes();        //获取分钟
    var s = date.getSeconds();        //获取秒
    monthText = mon < 10 ? "0" + mon : mon;
    daText = da < 10 ? "0" + da : da;
    hoursText = h < 10 ? "0" + h : h;
    minutesText = m < 10 ? "0" + m : m;
    secondText = s < 10 ? "0" + s : s;
    return year+monthText+daText+hoursText+minutesText+secondText;
}