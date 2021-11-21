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
    var store_introduce=$("#store_introduce").val();
    var store_image=$("#store_image").val();
    var extStart = store_image.lastIndexOf('.');
    var ext = store_image.substring(extStart,store_image.length).toUpperCase();
    if (name.length>1){
        if (email.indexOf('@')>1){
            if (account.length>1){
                if (phonenum.length===11){
                    if (password1.length>5){
                        if (password1.length<19){
                            if (password===password1){
                                if (store_introduce.length<=150){
                                    if(ext === '.PNG' || ext === '.JPG' || ext === '.JPEG' || ext === '.GIF') {
                                        var file = $("#store_image").get(0).files[0];
                                        if (file.size < 1024 * 1024 * 2) {
                                            return true;
                                        } else {
                                            alert("图片大小超过2M,请重新上传图片!");
                                        }
                                    }else {
                                        alert('请上传正确格式的图片');
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

$("#store_image").change(function () {
    var file = this.files[0];
    var size = file.size;
    console.log('大小：'+size);
    var path = $(this).val();
    extStart = path.lastIndexOf('.');
    ext = path.substring(extStart,path.length).toUpperCase();
    //判断图片格式
    if(ext !== '.PNG' && ext !== '.JPG' && ext !== '.JPEG' && ext !== '.GIF'){
        alert('请上传正确格式的图片');
        return false;
    }else{
        console.log('图片格式为：' + ext);
    }
    //判断图片大小
    if (file) {
        var fileSize = 0;
        //大于1Mb时转换单位
        if (file.size > 1024 * 1024)
            fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
            console.log('fileSize:'+fileSize);
        if(fileSize > 2){
            alert("图片大小超过2M,请重新上传图片!");
            return false;
        }

    }
});