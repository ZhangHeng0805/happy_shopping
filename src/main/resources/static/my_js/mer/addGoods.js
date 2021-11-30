
function data1() {
    $.ajax({
        url:'/findGoodsType',
        method:'get',
        dataType:'json',
        success:function (data) {
            var html='';
            for (var i=0;i<data.length;i++){
                html+="<option value='"+data[i].type+"'>"+data[i].type+"</option>";
            }
            $("#goods_type").html(html);
        },
        error:function (e) {
            alert('data1错误：'+e);
            window.close();
        }
    })
}

$("#GoodsForm").submit(function () {
    var store_image=$("#goods_image").val();
    var goods_name=$("#goods_name").val();
    var goods_introduction=$("#goods_introduction").val();
    var extStart = store_image.lastIndexOf('.');
    if (goods_name.length<=15){
        if (goods_introduction.length<=50){
            if(ext === '.PNG' || ext === '.JPG' || ext === '.JPEG' || ext === '.GIF') {
                var file = $("#goods_image").get(0).files[0];
                if (file.size < 1024 * 1024 * 1) {
                    return true;
                } else {
                    alert("图片大小超过1M,请重新上传图片!");
                }
            }else {
                alert('请上传正确格式的图片');
            }
        } else {
            alert("商品简介过长")
        }
    } else {
        alert("商品名称过长")
    }
    return false;
});
$("#goods_image").change(function () {
    var file = this.files[0];
    var size = file.size;
    console.log('图片大小：'+size);
    var path = $(this).val();
    extStart = path.lastIndexOf('.');
    ext = path.substring(extStart,path.length).toUpperCase();
    //判断图片格式
    if(ext !== '.PNG' && ext !== '.JPG' && ext !== '.JPEG' && ext !== '.GIF'){
        alert('请上传正确格式的图片(PNG,JPG,JPEG,GIF)');
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
        if(fileSize > 1){
            alert("图片大小超过1M,请重新上传图片!");
            return false;
        }

    }
});