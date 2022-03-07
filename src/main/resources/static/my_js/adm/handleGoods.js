var GoodsData;
function getData() {
    $.ajax({
        url:'/get_sta2Goods',
        method:'post',
        dataType:'json',
        success:function (d) {
            GoodsData=d;
            var html='';
            $("#goods_num").text(d.length);
            $(".goods_tbody").html(html);
            html+='<section id="no-more-tables">' +
                '<table class="table table-bordered table-striped table-condensed cf" id="editable-sample">' +
                '<thead class="cf">' +
                '<tr>' +
                '<th style="text-align: center">图片</th>' +
                '<th style="text-align: center">时间</th>' +
                '<th style="text-align: center">商品id</th>' +
                '<th style="text-align: center">商品名称</th>' +
                '<th style="text-align: center">店铺id</th>' +
                '<th style="text-align: center">店铺名称</th>' +
                '<th style="text-align: center">商品类型</th>' +
                '<th style="text-align: center">商品简介</th>' +
                '<th style="text-align: center">商品销量(件)</th>' +
                '<th style="text-align: center">库存数量(件)</th>' +
                '<th style="text-align: center">商品单价(元)</th>' +
                '<th style="text-align: center">通过操作</th>' +
                '<th style="text-align: center">封禁操作</th>' +
                '</tr>' +
                '</thead>' +
                '<tbody>';
            for (var i=0;i<d.length;i++) {
                html += "<tr>" +
                    "<td data-title='图片' class='t"+i+"-img' style='text-align: center'><img style='height: 50px;' src='/fileload/show/"+d[i].goods_image+"' alt=''></td>" +
                    "<td data-title='时间' class='col-sm-1 t"+i+"-time' style='text-align: center'><span><span></td>" +
                    "<td data-title='商品id' class='col-sm-1 t"+i+"-goods_id' style='text-align: center'><label class='badge badge-primary'></label></td>" +
                    "<td data-title='商品名称' class='col-sm-1 t"+i+"-goods_name' style='text-align: center'><span><span></td>" +
                    "<td data-title='店铺id' class='col-sm-1 t"+i+"-store_id' style='text-align: center'><label class='badge badge-info'></label></td>" +
                    "<td data-title='店铺名称' class='col-sm-1 t"+i+"-store_name' style='text-align: center'><span><span></td>" +
                    "<td data-title='商品类型' class='col-sm-1 t"+i+"-goods_type' style='text-align: center'></td>" +
                    "<td data-title='商品简介' class='col-sm-2 t"+i+"-goods_introduction' style='text-align: center'></td>" +
                    "<td data-title='商品销量(件)' class='col-sm-1 t"+i+"-goods_sales' style='text-align: center'></td>" +
                    "<td data-title='库存数量(件)' class='col-sm-1 t"+i+"-goods_num' style='text-align: center'></td>" +
                    "<td data-title='商品单价(元)' class='col-sm-1 t"+i+"-goods_price' style='text-align: center'></td>" +
                    "<td data-title='通过操作'  class='col-sm-1 t"+i+"-OK' style='text-align: center'><button class='btn btn-success' onclick='Yes("+i+")'>通过</button></td>" +
                    "<td data-title='封禁操作'  class='col-sm-1 t"+i+"' style='text-align: center'><button class='btn btn-danger' onclick='No("+i+")'>不通过</button></td>" +
                    "</tr>";
            }
            html += "</tbody></table></section>";
            $(".goods_tbody").html(html);
            inText(d);
            EditableTable.init();
        },
        error:function (e) {
            alert('表格数据错误：'+e);
            window.location.href="/exit_adm";
            window.close();
        }
    })
}
//表格填充数据
function inText(data){
    for (var i=0;i<data.length;i++) {
        $(".t"+i+"-img").children("img").attr('alt',data[i].goods_name);
        $(".t"+i+"-goods_id").children("label").text(data[i].goods_id);
        // $(".t"+i+"-time").children("span").text(data[i].time.substring(0,data[i].time.lastIndexOf(':')));
        $(".t"+i+"-time").children("span").text(data[i].time);
        $(".t"+i+"-goods_name").children("span").text(data[i].goods_name);
        $(".t"+i+"-store_id").children("label").text(data[i].store_id);
        $(".t"+i+"-store_name").children("span").text(data[i].store_name);
        var type=data[i].goods_type.indexOf('暂无分类')<0?data[i].goods_type:"<lable class='label label-danger' title='请及时修改该商品的类型'>暂无分类</lable>";
        $(".t" + i + "-goods_type").html(type);
        $(".t"+i+"-goods_introduction").text(data[i].goods_introduction);
        $(".t"+i+"-goods_sales").text(data[i].goods_sales);
        $(".t"+i+"-goods_num").text(data[i].goods_num);
        $(".t"+i+"-goods_price").text(data[i].goods_price);
        $(".t"+i).attr("title",data[i].goods_name);
        if (data[i].goods_type=="暂无分类") {
            $(".t"+i+"-OK").children("button").prop("disabled", true);
            $(".t"+i+"-OK").prop("title", "商品类型暂未分类，无法通过审核！");
        }
    }
}
//刷新数据
function ref() {
    getData();
}
function Yes(i) {
    console.log(i);
    var b = confirm("是否通过审核此商品："+GoodsData[i].goods_name+"？");
    if (b){
        update_state(GoodsData[i].goods_id,0);
    }
}
function No(i) {
    console.log(i);
    var b = confirm("是否不通过审核此商品："+GoodsData[i].goods_name+"？");
    if (b){
        update_state(GoodsData[i].goods_id,3);
    }
}
function update_state(goods_id,state) {
    $.ajax({
        url:'/setState_sta2Goods',
        method:'post',
        data:{
            goods_id:goods_id,
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
            alert('修改商品状态错误：'+e);
            window.location.href="/exit_adm";
            window.close();
        }
    })
}