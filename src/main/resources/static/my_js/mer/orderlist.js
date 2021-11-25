function init() {
    $(".goods_tbody_t1").html('');
    $(".goods_tbody_t2").html('');
    $(".goods_tbody_t3").html('');
    $(".goods_tbody_t4").html('');
    $(".goods_tbody_t5").html('');
    $(".goods_tbody_t6").html('');
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
                  '<th style="text-align: center">单价</th>' +
                  '<th style="text-align: center">数量</th>' +
                  '<th style="text-align: center">总价</th>' +
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
                    "<td data-title='订单号' class='col-sm-1' style='text-align: center'>"+data[i].order_id.substring(data[i].order_id.indexOf('_')+1)+"</td>" +
                    "<td data-title='收货人' class='col-sm-1' style='text-align: center'>"+data[i].username+"</td>" +
                    "<td data-title='订单时间' class='col-sm-1' style='text-align: center'>"+data[i].time+"</td>" +
                    "<td data-title='联系电话' class='col-sm-1' style='text-align: center'>"+data[i].tel+"</td>" +
                    "<td data-title='收货地址' class='col-sm-2' style='text-align: center'>"+data[i].address+"</td>" +
                    "<td data-title='商品id' class='col-sm-1' style='text-align: center'><label class='label label-default'>"+data[i].goods_id+"</label></td>" +
                    "<td data-title='商品名' class='col-sm-1' style='text-align: center'>"+data[i].goods_name+"</td>" +
                    "<td data-title='单价' class='col-sm-1' style='text-align: center'>"+data[i].price+"</td>" +
                    "<td data-title='数量' class='col-sm-1' style='text-align: center'>"+data[i].num+"</td>" +
                    "<td data-title='总价' class='col-sm-1' style='text-align: center'>"+data[i].all_price+"</td>" +
                    "<td data-title='状态' class='col-sm-1' style='text-align: center'><label class='label "+staClass+"'>"+staStr+"</label></td>" +
                    "</tr>";
            }
            html+="</tbody></table></section>";
            var body = $(".goods_tbody_t"+id);
            body.html(html);
            EditableTable.init();
        },
        error:function (e) {
            alert('data5错误：'+e);
            window.close();
        }
    })
}