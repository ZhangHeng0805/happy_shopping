$("#accountFrom").submit(function () {
 var pwd = $("#password").val();
 var name = $("#name").val();
 var email = $("#email").val();
 var store_name = $("#store_name").val();
 var store_introduce = $("#store_introduce").val();
 var address = $("#address").val();
 var image = $("#image").val();
 var account = $("#account").val();
if (pwd.length>=6){
    if (pwd.length<=18){
        if (name.length>=1 && name.length<=15){
            if (store_name.length>=1&&store_name.length<=20){
                if (store_introduce.length>=1&&store_introduce.length<=150) {
                    if (address.length>=2&&address.length<=30){
                        if (account.length>=1&&account.length<=18) {
                            $(".submit").attr('disabled',"true");
                            $("#password").val(zh_md5(pwd));
                            return true;
                        }else {
                            alert("用户名长度范围：1~18字");
                        }
                    } else {
                        alert("店铺地址长度范围：2~30字");
                    }
                }else {
                    alert("店铺介绍长度范围：1~150字");
                }
            } else {
                alert("店铺名称长度范围：1~20字");
            }
        } else {
            alert("商户姓名长度范围：1~15字");
        }
    } else {
        alert("密码长度过长！");
    }
} else {
    alert("密码长度过短！");
}
 return false;
});
function ver_email() {
    $('#email_model').modal('show');
}
//邮箱设置弹窗显示时
$("#email_model").on('show.bs.modal',function () {
    $(".get_code").attr("disabled",false);
});
//邮箱设置弹窗关闭时
$("#email_model").on('hide.bs.modal',function () {
    $("#email_code").val('');
});
//获取验证码
$(".get_code").click(function () {
    var mail=$("#my_email").val();
    if (mail!=null&&mail.length>5&&mail.indexOf("@")>0){
        if (mail!=$('#email').val()){
            get_EmailCoed(mail);
        } else {
            alert("请输入新的的邮箱地址，新旧邮箱不能相同");
        }
    }else {
        alert("请输入正确的邮箱地址");
    }
});
$(".ver_code").click(function () {
    var code=$("#email_code").val();
    var mail=$("#my_email").val();
    if (mail!=null&&mail.length>5&&mail.indexOf("@")>0){
        if (code!=null&&code.length>0){
            ver_EmailCoed(mail,code);
        } else {
            alert("验证码不能为空");
        }
    } else {
        alert("邮箱格式错误");
    }
});
//获取验证码
function get_EmailCoed(mail) {
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
}
//进行邮箱验证
function ver_EmailCoed(mail,code) {
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
                alert("邮箱修改成功！");
            }else {
                alert(data.message);
            }
        },
        error:function (e) {
            alert('验证码验证错误：'+e);
        }
    })

}