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
$(".submit").click(function () {
    $("#accountFrom").submit();
});