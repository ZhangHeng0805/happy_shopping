function data1() {
    $.ajax({
        url:'/findGoodsType',
        method:'get',
        dataType:'json',
        success:function (data) {
            var html='<option value="0">全部</option>';
            for (var i=0;i<data.length;i++){
                html+="<option value='"+data[i].id+"'>"+data[i].type+"</option>";
            }
            $("#goods_type").html(html);
        },
        error:function (e) {
            alert('data1错误：'+e);
            window.close();
        }
    })
}
var typeState=0;
function data2(id) {
    $.ajax({
        url:'/findGoodsByType',
        method:'post',
        data:{
          id:id
        },
        dataType:'json',
        success:function (data) {
            // console.log(data);
            var html='';
            $("#type_num").html(data.length);
            $(".goods_tbody").html(html);
            html+='<section id="no-more-tables">' +
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
                '</tr>' +
                '</thead>' +
                '<tbody>';
            for (var i=0;i<data.length;i++) {
                var staClass = "label-success";
                var staStr = "已上线";
                switch (data[i].state) {
                    case 0:
                        staClass = "label-success";
                        staStr = "已上线";
                        break;
                    case 1:
                        staClass = "label-danger";
                        staStr = "已下线";
                        break;
                    case 2:
                        staClass = "label-warning";
                        staStr = "审核中";
                        break;
                }
                html += "<tr>" +
                    "<td data-title='商品图片' class='col-sm-1' style='text-align: center'><img style='height: 45px;' src='/fileload/show/"+data[i].goods_image+"' alt='"+data[i].goods_name+"'></td>" +
                    "<td data-title='商品id' class='col-sm-1' style='text-align: center'><label class='badge badge-primary'>" + data[i].id + "</label></td>" +
                    "<td data-title='商品名称' class='col-sm-1' style='text-align: center'>" + data[i].goods_name + "</td>" +
                    "<td data-title='商品类型' class='col-sm-1' style='text-align: center'>" + data[i].goods_type + "</td>" +
                    "<td data-title='商品简介' class='col-sm-2' style='text-align: center'>" + data[i].goods_introduction + "</td>" +
                    "<td data-title='商品销量(件)' class='col-sm-1' style='text-align: center'>" + data[i].goods_sales + "</td>" +
                    "<td data-title='库存数量(件)' class='col-sm-1' style='text-align: center'>" + data[i].goods_num + "</td>" +
                    "<td data-title='商品单价(元)' class='col-sm-1' style='text-align: center'>" + data[i].goods_price + "</td>" +
                    "<td data-title='商品状态' title='只有[已上线]的商品可以上架商场' class='col-sm-1' style='text-align: center'><label class='label " + staClass + "'>" + staStr + "</label></td>" +
                    "</tr>";
            }
            html+="</tbody></table></section>";
            $(".goods_tbody").html(html);
            EditableTable.init();
        },
        error:function (e) {
            alert('data2错误：'+e);
            window.close();
        }
    })
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