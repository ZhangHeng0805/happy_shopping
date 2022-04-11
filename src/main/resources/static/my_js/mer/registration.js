/*注册页面*/
$("#registForm").submit(function () {
    var name=$("#name").val();
    var email=$("#email").val();
    var account=$("#account").val();
    var phonenum=$("#phonenum").val();
    var password=$("#password").val();
    var password1=$("#password1").val();
    var store_name=$("#store_name").val();
    var address=$("#address").val();
    var img=$("#image").val();
    var store_introduce=$("#store_introduce").val();
    if (name.length>1){
        if (email.indexOf('@')>1){
            if (account.length>1){
                if (phonenum.length===11){
                    if (password1.length>5){
                        if (password1.length<19){
                            if (password===password1){
                                if (store_introduce.length<=150){
                                    if (img.length>0){
                                        $(".submit").attr('disabled',"true");
                                        $("#password").val(zh_md5(password));
                                        return true;
                                    }else {
                                        alert("请选择店铺图片");
                                    }
                                } else {
                                    alert("店铺介绍字数限制150字");
                                }
                            } else {
                                alert("输入的两次密码密码不一致");
                            }
                        } else {
                            alert("密码过长");
                        }
                    } else {
                        alert("密码过短");
                    }
                } else {
                    alert("请输入11位手机号");
                }
            } else {
                alert("用户名过短");
            }
        } else {
            alert("请输入正确格式的邮箱");
        }
    } else {
        alert("姓名过短");
    }
    return false;
});

function ver_email() {
    $('#email_model').modal('show');
}
//弹窗显示时
$("#email_model").on('show.bs.modal',function () {
    $(".get_code").attr("disabled",false);
});
//弹窗关闭时
$("#email_model").on('hide.bs.modal',function () {
    $("#email_code").val('');
});
//获取验证码
$(".get_code").click(function () {
    var mail=$("#my_email").val();
    if (mail!=null&&mail.length>5&&mail.indexOf("@")>0){
        $(".get_code").attr("disabled",true);
        $(".get_code").val("验证码发送中...");
        $.ajax({
            url:'/get_EmailCode',
            method:'post',
            data:{
                email:mail
            },
            dataType:'json',
            success:function (data) {
                alert(data.message);
                $(".get_code").val("获取验证码");
                if (data.code===200){
                    $(".get_code").attr("disabled",false);
                    $(".ver_code").show();
                }else if (data.code==404){
                    $(".ver_code").show();
                    $(".get_code").val(data.message);
                }
            },
            error:function (e) {
                $(".get_code").attr("disabled",false);
                $(".get_code").val("获取验证码");
                $(".ver_code").show();
                alert('邮箱验证码错误：'+e);
            }
        })
    }else {
        alert("请输入正确的邮箱地址");
    }
});
$(".ver_code").click(function () {
   ver_EmailCoed();
});
//进行验证
function ver_EmailCoed() {
    var code=$("#email_code").val();
    var mail=$("#my_email").val();
    if (mail!=null&&mail.length>5&&mail.indexOf("@")>0){
        if (code!=null&&code.length>0){
            $.ajax({
                url:'/ver_EmailCode',
                method:'post',
                data:{
                    email:mail,
                    code:code
                },
                dataType:'json',
                success:function (data) {
                    if (data.code==200){
                        $("#email").val(mail);
                        $('#email_model').modal('hide');
                    }else {
                        alert(data.message);
                    }
                },
                error:function (e) {
                    alert('验证码验证错误：'+e);
                }
            })
        } else {
            alert("验证码不能为空");
        }
    } else {
        alert("邮箱格式错误");
    }
}