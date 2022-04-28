//获取商品类型
function data1() {
    $.ajax({
        url:'/findGoodsType?t='+Date.now(),
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
            window.location.href="/exit_mer";
            window.close();
        }
    })
}

function btn_submit() {
    var img=$("#image").val();
    var goods_name=$("#goods_name").val();
    var goods_introduction=$("#goods_introduction").val();
    var goods_type = $("#goods_type option:selected").val();
    var goods_price=$("#goods_price").val();
    var goods_num=$("#goods_num").val();
    if (goods_name.length>0&&goods_name.length<=15){
        if (goods_introduction.length>0&&goods_introduction.length<=50){
            if (goods_price.length>0&&goods_price>0&&goods_price<=999999) {
                if (goods_num.length>0&&goods_num>0&&goods_num<=99999999) {
                    if (img.length > 0) {
                        var b = confirm("确定添加新商品？(新商品需要等待审核才能上线)");
                        if (b) {
                            submit_goods(goods_name,goods_type,goods_introduction,img,goods_price,goods_num);
                        }
                    } else {
                        alert("请选择商品图片");
                    }
                }else {
                    alert("商品库存范围：0~99999999件");
                }
            }else {
                alert("商品价格范围：0.01~999999元");
            }
        } else {
            alert("商品简介长度：1~50字");
        }
    } else {
        alert("商品名称长度：1~15字");
    }
}

function submit_goods(goods_name,goods_type,goods_introduction,image,goods_price,goods_num){
    $(".submit").attr("disabled",true);
    $(".submit").text("商品添加中...");
    $.ajax({
        url:'/addGoods',
        method:'post',
        data:{
            goods_name:goods_name,
            goods_type:goods_type,
            goods_introduction:goods_introduction,
            image:image,
            goods_price:goods_price,
            goods_num:goods_num
        },
        dataType:'json',
        success:function (d) {
            alert(d.message);
            if (d.code===200){
                form_reset();
            }
            $(".submit").attr("disabled",false);
            $(".submit").text("添加商品");
        },
        error:function (e) {
            $(".submit").attr("disabled",false);
            $(".submit").text("添加商品");
            console.log(e);
            alert("新增商品错误："+e.toString());
            // window.location.href="/exit_mer";
        }
    });
}
//重置表单
function form_reset(){
    //表单
    $("#GoodsForm")[0].reset();
    pc=pc.clear();
    document.getElementById("view").style.backgroundImage=null;
    document.getElementById("show").style.backgroundImage=null;
    $("#image").val(null);
    imgdata=null;
    $('#clipBtn').hide();
    $("#view").hide();
}

