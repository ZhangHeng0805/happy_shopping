
var typeState=0;
var GoodsData;
function data2(id) {
    $.ajax({
        url:'/findGoodsByType',
        method:'post',
        data:{
          id:id
        },
        dataType:'json',
        success:function (data) {
            GoodsData=data;
            // console.log(data);
            var html='';
            $("#type_num").text(data.length);
            $(".goods_tbody").html(html);
            var h='<section id="no-more-tables">' +
                '<table class="table table-bordered table-striped table-condensed cf" id="editable-sample">' +
                '<thead class="cf">' +
                '<tr>' +
                '<th style="text-align: center">商品图片</th>' +
                '<th style="text-align: center">商品id</th>' +
                '<th style="text-align: center">商品名称</th>' +
                '<th style="text-align: center">商品类型</th>' +
                '<th style="text-align: center">商品简介</th>' +
                '<th style="text-align: center">商品销量(件)</th>' +
                '<th style="text-align: center">库存数量(件)</th>' +
                '<th style="text-align: center">商品单价(元)</th>' +
                '<th style="text-align: center">商品状态</th>' +
                '<th style="text-align: center">操作</th>' +
                '<th style="text-align: center">删除操作</th>' +
                '<th style="text-align: center">编辑操作</th>' +
                '</tr>' +
                '</thead>' +
                '<tbody>';
            for (var i=0;i<data.length;i++) {
                var staClass = "label-success";
                var btnClass = "btn-warning";
                var staStr = "已上线";
                var btnStr = "下架商品";
                var fun;
                switch (data[i].state) {
                    case 0:
                        staClass = "label-success";
                        staStr = "已上线";
                        btnClass = "btn-warning";
                        btnStr = "下架商品";
                        fun="sta1("+data[i].id+",\""+data[i].goods_name+"\")";
                        break;
                    case 1:
                        staClass = "label-default";
                        staStr = "已下线";
                        btnClass = "btn-success";
                        btnStr = "上架商品";
                        fun="sta0("+data[i].id+",\""+data[i].goods_name+"\")";
                        break;
                    case 2:
                        staClass = "label-warning";
                        staStr = "审核中";
                        btnClass = "btn-info";
                        btnStr = "催促审核";
                        fun="sta2("+data[i].id+",\""+data[i].goods_name+"\")";
                        break;
                    case 3:
                        staClass = "label-danger";
                        staStr = "商品违规";
                        btnClass = "btn-default";
                        btnStr = "修改重审";
                        fun="sta3("+i+",\""+data[i].goods_name+"\")";
                        break;
                }
                var b = "<tr>" +
                    "<td data-title='商品图片' class='t"+i+"-img' style='text-align: center'><img style='height: 50px;' src='/fileload/show/"+data[i].goods_image+"' alt=''></td>" +
                    "<td data-title='商品id' class='col-sm-1 t"+i+"-id' style='text-align: center'><label class='badge badge-primary'></label></td>" +
                    "<td data-title='商品名称' class='col-sm-1 t"+i+"-name' style='text-align: center'><span><span></td>" +
                    "<td data-title='商品类型' class='col-sm-1 t"+i+"-type' style='text-align: center'></td>" +
                    "<td data-title='商品简介' class='col-sm-2 t"+i+"-introduction' style='text-align: center'></td>" +
                    "<td data-title='商品销量(件)' class='col-sm-1 t"+i+"-sales' style='text-align: center'></td>" +
                    "<td data-title='库存数量(件)' class='col-sm-1 t"+i+"-num' style='text-align: center'></td>" +
                    "<td data-title='商品单价(元)' class='col-sm-1 t"+i+"-price' style='text-align: center'></td>" +
                    "<td data-title='商品状态' title='只有[已上线]的商品可以上架商场' class='col-sm-1' style='text-align: center'><label class='label " + staClass + "'>" + staStr + "</label></td>" +
                    "<td data-title='操作'  class='col-sm-1 t"+i+"' style='text-align: center'><label class='btn " + btnClass + "' onclick='"+fun+"'>" + btnStr + "</label></td>" +
                    "<td data-title='删除操作'  class='col-sm-1 t"+i+"' style='text-align: center'><button class='btn btn-danger' onclick='del_goods("+data[i].id+",\""+data[i].goods_name+"\")'>删除商品</button></td>" +
                    "<td data-title='编辑操作'  class='col-sm-1 t"+i+"' style='text-align: center'><button class='btn btn-primary' onclick='openmodel("+i+")'>编辑商品</button></td>" +
                    "</tr>";
                h+=b;
            }
            var f="</tbody></table></section>";
            $(".goods_tbody").html(h+f);
            inText(data);
            EditableTable.init();

        },
        error:function (e) {
            alert('data2错误：'+e);
            window.location.href="/exit_mer";
            window.close();
        }
    })
}
//表格填充数据
function inText(data){
    for (var i=0;i<data.length;i++) {
        $(".t"+i+"-img").children("img").attr('alt',data[i].goods_name);
        $(".t"+i+"-id").children("label").text(data[i].id);
        $(".t"+i+"-name").children("span").text(data[i].goods_name);
        $(".t"+i+"-type").text(data[i].goods_type);
        $(".t"+i+"-introduction").text(data[i].goods_introduction);
        $(".t"+i+"-sales").text(data[i].goods_sales);
        $(".t"+i+"-num").text(data[i].goods_num);
        $(".t"+i+"-price").text(data[i].goods_price);
        $(".t"+i).attr("title",data[i].goods_name)
    }
}
$("#goods_type").change(function () {
    var value=$("#goods_type option:selected");
    $("#type").html(value.text());
    typeState=value.val();
    data2(typeState);
});
//刷新数据
function ref() {
    data2(typeState);
}

function sta0(id,name) {
    var b = confirm("是否上架该商品：<"+name+">?");
    if (b){
        sub(id,0);
    }
}
function sta1(id,name) {
    var b = confirm("是否下架该商品：<"+name+">?");
    if (b){
        sub(id,1);
    }
}
function sta2(id,name) {
    var b = confirm("是否催促该商品：<"+name+">?");
    if (b){
        alert("已催促商品：<"+name+">");
    }
}
function sta3(i,name) {
    var b = confirm("是否重新编辑该商品：<"+name+">?");
    if (b){
        openmodel(i);
    }
}
function sub(id,state) {
    // console.log("id:"+id+",state:"+state);
    $.ajax({
        url:"/updata_GoodsState",
        method:'post',
        data:{
            id:id,
            state:state
        },
        dataType:'json',
        success:function (d) {
            alert(d.message);
            if (d.code===200){
                ref();
            }
        },
        error:function (e) {
            alert("修改错误:"+e);
            window.location.href="/exit_mer";
            window.close();
        }
    })
}
function del_goods(id,name) {
    console.log(id);
    var b = confirm("确认删除此商品：<"+name+">？");
    if (b){
        $.ajax({
            url:'/delete_Goods',
            method:'post',
            data:{
                id:id
            },
            dataType:'json',
            success:function (d) {
                alert(d.message);
                if (d.code===200){
                    ref();
                }
            },
            error:function (e) {
                alert("删除错误:"+e);
                window.location.href="/exit_mer";
                window.close();
            }
        })
    }
}
function getGoodsType() {
    $.ajax({
        url:'/findGoodsType?t='+Date.now(),
        method:'get',
        dataType:'json',
        success:function (data) {
            var html1,html2;
            html2="<option value='0'>全部</option>";
            for (var i=0;i<data.length;i++){
                html1+="<option value='"+data[i].type+"'>"+data[i].type+"</option>";
                html2+="<option value='"+data[i].id+"'>"+data[i].type+"</option>";
            }
            $("#model_goods_type").html(html1);
            $("#goods_type").html(html2);
        },
        error:function (e) {
            alert('商品类型错误：'+e);
            window.location.href="/exit_mer";
            window.close();
        }
    })
}
//当裁剪图片提示框打开时
$("#img_cut").on('show.bs.modal',function () {
    $("#updateGoods").modal('hide')
});
var imgdata=null;

//当裁剪图片提示框关闭时
$('#img_cut').on('hide.bs.modal', function () {
    $("#updateGoods").modal('show')
    if (imgdata!=null) {
        //图片预览
        $("#show").attr("src", imgdata);
        //input赋值
        $("#image").val(imgdata);
    }else {
        alert("请点击截取按钮裁剪图片")
    }
});

//最初库存
var data_goods_num;
//打开修改商品信息的面板
function openmodel(i) {
    $("#model_title").text(GoodsData[i].goods_name);
    $("#model_goods_id").val(GoodsData[i].id);
    $("#model_goods_name").val(GoodsData[i].goods_name);
    set_select_checked('model_goods_type',GoodsData[i].goods_type);
    $("#model_goods_introduction").text(GoodsData[i].goods_introduction);
    $("#show").attr("src","/fileload/show/"+GoodsData[i].goods_image);
    $("#model_goods_price").val(GoodsData[i].goods_price);
    $("#model_goods_num").val(GoodsData[i].goods_num);
    $("#model_goods_num").attr('min',GoodsData[i].goods_num);
    $("#data_num").text(GoodsData[i].goods_num);
    data_goods_num=GoodsData[i].goods_num;
    $("#updateGoods").modal('show');
}

function sub_goods() {
    var goods_name = $("#model_goods_name").val();
    var goods_id = $("#model_goods_id").val();
    var goods_type = $("#model_goods_type option:selected").val();
    var goods_introduction = $("#model_goods_introduction").text();
    var image = $("#image").val();
    var goods_price = $("#model_goods_price").val();
    var goods_num = $("#model_goods_num").val();
    if (goods_name.length<=15){
        if (goods_introduction.length<=50){
            if (goods_num>=data_goods_num){
                var b = confirm("确定提交商品修改？（修改的商品需要等待审核才能上线）");
                if (b){
                    update_goods(goods_id,goods_name,goods_type,goods_introduction,image, goods_price,goods_num);
                }
            } else {
                alert("商品库存不能低于原始库存:"+data_goods_num+"件");
            }
        } else {
            alert("商品简介过长")
        }
    } else {
        alert("商品名称过长")
    }

}
function update_goods(goods_id,goods_name,goods_type,goods_introduction,image,
                      goods_price,goods_num) {
    $.ajax({
        url:'/update_goods',
        method:'post',
        data:{
            goods_id:goods_id,
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
                ref();
            }
        },
        error:function (e) {
            alert("修改商品错误："+e);
            window.location.href="/exit_mer";
        }
    })
}