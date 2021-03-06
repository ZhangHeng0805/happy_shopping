function init() {
    $(".goods_tbody_t1").html('');
    $(".goods_tbody_t2").html('');
    $(".goods_tbody_t3").html('');
    $(".goods_tbody_t4").html('');
    $(".goods_tbody_t5").html('');
    $(".goods_tbody_t6").html('');
    for (var i=1;i<=6;i++){
        $("#btn"+i).hide();
        $("#btn"+i).text("导出为Excel文件");
    }
    // $("#btn2").hide();
    // $("#btn3").hide();
    // $("#btn4").hide();
    // $("#btn5").hide();
    // $("#btn6").hide();
}

//查询本店全部订单商品
function data(id,method) {
    $.ajax({
        url:'/OrderGoodsByState',
        method:'get',
        data:{
            id:Date.now(),
            method:method
        },
        dataType:'json',
        success:function (data) {
            init();
            // console.log(data);
            $("#t"+id+"_title").html(data.length);
            var html='';
            html+='<section id="no-more-tables">' +
                '<table class="table table-bordered table-striped table-condensed cf" id="editable-sample">' +
                  '<thead class="cf">' +
                  '<tr>' +
                  '<th style="text-align: center">订单号</th>' +
                  '<th style="text-align: center">收货人</th>' +
                  '<th style="text-align: center">订单时间</th>' +
                  '<th style="text-align: center">联系电话</th>' +
                  '<th style="text-align: center">收货地址</th>' +
                  '<th style="text-align: center">商品id</th>' +
                  '<th style="text-align: center">商品名</th>' +
                  '<th style="text-align: center">单价(元)</th>' +
                  '<th style="text-align: center">数量</th>' +
                  '<th style="text-align: center">总价(元)</th>' +
                  '<th style="text-align: center">状态</th>' +
                  '</tr>' +
                  '</thead>' +
                  '<tbody>';
            for (var i=0;i<data.length;i++){
                var staClass="label-default";
                var staStr="未处理";
                switch (data[i].state) {
                    case 0:
                        staClass="label-default";
                        staStr="未处理";
                        break;
                    case 1:
                        staClass="label-warning";
                        staStr="拒绝发货";
                        break;
                    case 2:
                        staClass="label-info";
                        staStr="待收货";
                        break;
                    case 3:
                        staClass="label-success";
                        staStr="已收货";
                        break;
                    case 4:
                        staClass="label-danger";
                        staStr="退货";
                        break;
                }
                html+="<tr>" +
                    "<td data-title='订单号' class=' col-md-1 t"+i+"-order_id' style='text-align: center'></td>" +
                    "<td data-title='收货人' class='col-md-1 t"+i+"-username' style='text-align: center'></td>" +
                    "<td data-title='订单时间' class='col-md-1 t"+i+"-time' style='text-align: center'></td>" +
                    "<td data-title='联系电话' class='col-md-1 t"+i+"-tel' style='text-align: center'></td>" +
                    "<td data-title='收货地址' class='col-md-2 t"+i+"-address' style='text-align: center'></td>" +
                    "<td data-title='商品id' class='col-md-1 t"+i+"-goods_id' style='text-align: center'><label class='label label-default'></label></td>" +
                    "<td data-title='商品名' class='col-md-1 t"+i+"-goods_name' style='text-align: center'></td>" +
                    "<td data-title='单价' class='col-md-1 t"+i+"-price' style='text-align: center'></td>" +
                    "<td data-title='数量' class='col-md-1 t"+i+"-num' style='text-align: center'></td>" +
                    "<td data-title='总价' class='col-md-1 t"+i+"-all_price' style='text-align: center'></td>" +
                    "<td data-title='状态' class='col-md-1' style='text-align: center'><label class='label "+staClass+"'>"+staStr+"</label></td>" +
                    "</tr>";
            }
            html+="</tbody></table></section>";
            var body = $(".goods_tbody_t"+id);
            body.html(html);
            inText(data,id);
            EditableTable.init();
        },
        error:function (e) {
            alert('data5错误：'+e);
            window.location.href="/exit_mer";
            window.close();
        }
    })
}
function inText(data,id){
    for (var i=0;i<data.length;i++) {
        $(".t"+i+"-order_id").text(data[i].order_id.substring(data[i].order_id.indexOf('_')+1));
        $(".t"+i+"-username").text(data[i].username);
        $(".t"+i+"-time").text(data[i].time);
        $(".t"+i+"-tel").text(data[i].tel);
        $(".t"+i+"-address").text(data[i].address);
        $(".t"+i+"-goods_id").children("label").text(data[i].goods_id);
        $(".t"+i+"-goods_name").text(data[i].goods_name);
        $(".t"+i+"-price").text(data[i].price);
        $(".t"+i+"-num").text(data[i].num);
        $(".t"+i+"-all_price").text(data[i].all_price);
    }
    if (data.length>0){
        $("#btn"+id).show();
    }
}

function excel(name) {
    var store_name=$("#store_name").val();
    var excel = new ExcelGen({
        "src_id": "editable-sample",
        "show_header": true,
        "auto_format": true,
        "author":"乐在购物网_商家店铺:"+store_name,
        "file_name": store_name+"["+name+ "]_"+getTime()+".xlsx"
    });
    excel.generate();
}