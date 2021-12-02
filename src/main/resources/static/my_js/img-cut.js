var imgdata=null;
$("#clipArea").photoClip({
    width: 200,
    height: 200,
    file: "#file",
    view: "#view",
    ok: "#clipBtn",
    outputType: "png",
    strictSize: true,
    loadStart: function(file) {
        console.log("照片读取中");
    },
    loadComplete: function(src) {
        console.log("照片读取完成!");
    },
    loadError: function(event) {
        console.log("错误："+event);
    },
    clipFinish: function(dataURL) {
        // console.log(dataURL);
        imgdata=dataURL;
    }
});
//当提示框关闭时
$('#img_cut').on('hide.bs.modal', function () {
    if (imgdata!=null) {
        //图片预览
        $("#show").attr("src", imgdata);
        //input赋值
        $("#image").val(imgdata);
    }else {
        alert("请点击截取按钮裁剪图片")
    }
});