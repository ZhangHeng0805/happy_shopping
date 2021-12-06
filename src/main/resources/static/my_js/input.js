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