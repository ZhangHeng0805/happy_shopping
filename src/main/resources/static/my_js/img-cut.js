var imgdata=null;
var pc;
window.onload=function () {
    pc = new PhotoClip('#clipArea');
    pc.size(200, 200);
    pc.outputType('png');
    $('#clipBtn').hide();
    $("#view").hide();
};

$("#file").change(function() {
    $("#view").hide();
    var file = this.files[0];
    var size = file.size;
    // console.log('图片大小：'+size);
    var path = $(this).val();
    extStart = path.lastIndexOf('.');
    ext = path.substring(extStart,path.length).toUpperCase();
//判断图片格式
    if(ext !== '.PNG' && ext !== '.JPG' && ext !== '.JPEG' && ext !== '.GIF'){
        alert('请上传正确格式的图片(PNG,JPG,JPEG,GIF)');
        return false;
    }else{
        // console.log('图片格式为：' + ext);
    }
//判断图片大小
    if (file) {
        var fileSize = 0;
        //大于1Mb时转换单位
        fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
        console.log('图片大小:'+fileSize+"Mb");
        if(fileSize > 2){
            alert("图片大小超过2M,请重新上传图片!");
            return false;
        }else {
            pc.load(this.files[0]);
            $('#clipBtn').show();
        }
    }
});
//截取图片按钮点击事件
$('#clipBtn').click(function () {
    var base64=pc.clip();
    // console.log(base64);
    imgdata=base64;
    $("#view").show();
    document.getElementById("view").style.backgroundImage='url("'+imgdata+'")';
});
//当弹出框关闭时
$('#img_cut').on('hide.bs.modal', function () {
    $("#image").val(imgdata);
    document.getElementById("show").style.backgroundImage='url("'+imgdata+'")';
});
//当弹出框打开时
$('#img_cut').on('show.bs.modal', function () {

});
