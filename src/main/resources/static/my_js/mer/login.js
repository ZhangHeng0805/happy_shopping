/*商家登录页*/
$("#LoginFroms").submit(function () {
   var phone=$("#phonenum").val();
   var pwd=$("#password").val();
    if (phone.length===11){
        if (pwd.length>5) {
            if (pwd.length<19) {
                $(".submit").attr('disabled',"true");
                $("#password").val(zh_md5(pwd));
                return true;
            }else {
                alert("密码长度过长");
            }
        }else {
            alert("密码长度过短");
        }
    } else {
        alert("请输入11位手机号");
    }
    return false;
});
$("#phonenum").keyup(function () {
    this.value=this.value.replace(/\D/g,'');
    this.style.fontWeight="Bold";
    this.style.letterSpacing="1px";
    if (this.value.length!=11){
        this.style.color="rgba(166,0,9,0.93)";
    }else{
        this.style.color = "rgba(0,146,26,0.91)";
    }
});
$("#phonenum").change(function () {
    this.value=this.value.replace(/\D/g,'');
    this.style.fontWeight="Bold";
    this.style.letterSpacing="1px";
    if (this.value.length!=11){
        this.style.color="rgba(166,0,9,0.93)";
    }else {
        this.style.color="rgba(0,146,26,0.91)";
    }
});
$("#password").keyup(function () {
    this.value=this.value.replace(/[^\w\.\/]/ig,'');
    this.style.fontWeight="Bold";
    this.style.letterSpacing="1px";
    if (this.value.length<6 || this.value.length>18&&this.value.length!=32){
        this.style.color="rgba(166,0,9,0.93)";
    }else {
        this.style.color="rgba(0,146,26,0.91)";
    }
});
$("#password").change(function () {
    this.value=this.value.replace(/[^\w\.\/]/ig,'');
    this.style.fontWeight="Bold";
    this.style.letterSpacing="1px";
    if (this.value.length<6 || this.value.length>18&&this.value.length!=32){
        this.style.color="rgba(166,0,9,0.93)";
    }else {
        this.style.color="rgba(0,146,26,0.91)";
    }
});