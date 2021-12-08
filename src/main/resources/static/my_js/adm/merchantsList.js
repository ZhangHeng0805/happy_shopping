
function getData() {
    $.ajax({
        url:'/getMerchantsList',
        method:'post',
        dataType:'json',
        success:function (d) {
            var html='';
            $("#mer_num").text(d.length);
            $(".mer_tbody").html(html);
            html+='<section id="no-more-tables">' +
                '<table class="table table-bordered table-striped table-condensed cf" id="editable-sample">' +
                '<thead class="cf">' +
                '<tr>' +
                '<th style="text-align: center">店铺图片</th>' +
                '<th style="text-align: center">店铺id</th>' +
                '<th style="text-align: center">店铺名称</th>' +
                '<th style="text-align: center">商家姓名</th>' +
                '<th style="text-align: center">商家电话</th>' +
                '<th style="text-align: center">商家邮箱</th>' +
                '<th style="text-align: center">店铺地址</th>' +
                '<th style="text-align: center">店铺简介</th>' +
                '<th style="text-align: center">注册时长(天)</th>' +
                '<th style="text-align: center">账号状态</th>' +
                '</tr>' +
                '</thead>' +
                '<tbody>';
            for (var i=0;i<d.length;i++) {
                var staClass;
                var staText;
                switch (d[i].state) {
                    case 0:
                        staClass='label-success';
                        staText='正常';
                        break;
                    case 1:
                        staClass='label-danger';
                        staText='违规封禁';
                        break;
                    case 2:
                        staClass='label-warning';
                        staText='账号异常';
                        break;
                }
                html += "<tr>" +
                    "<td data-title='店铺图片' class='t" + i + "-img' style='text-align: center'><img style='height: 50px;' src='/fileload/show/" + d[i].store_image + "' alt=''></td>" +
                    "<td data-title='店铺id' class='col-sm-1 t" + i + "-store_id' style='text-align: center'><label class='badge badge-primary'></label></td>" +
                    "<td data-title='店铺名称' class='col-sm-1 t" + i + "-store_name' style='text-align: center'></td>" +
                    "<td data-title='商家姓名' class='col-sm-1 t" + i + "-mer_name' style='text-align: center'></td>" +
                    "<td data-title='商家电话' class='col-sm-2 t" + i + "-tel' style='text-align: center'></td>" +
                    "<td data-title='商家邮箱' class='col-sm-2 t" + i + "-email' style='text-align: center'></td>" +
                    "<td data-title='店铺地址' class='col-sm-2 t" + i + "-store_address' style='text-align: center'></td>" +
                    "<td data-title='店铺简介' class='col-sm-2 t" + i + "-store_introduce' style='text-align: center'></td>" +
                    "<td data-title='注册时长(天)' class='col-sm-1 t" + i + "-time' style='text-align: center'></td>" +
                    "<td data-title='账号状态' class='col-sm-1 t" + i + "-state' style='text-align: center'><label class='label "+staClass+"'>"+staText+"</label></td>" +
                "</tr>";

            }
            html += "</tbody></table></section>";
            $(".mer_tbody").html(html);
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
function inText(data) {
    for (var i=0;i<data.length;i++) {
        $(".t" + i + "-img").children("img").attr('alt', data[i].store_name);
        $(".t" + i + "-store_id").children("label").text(data[i].store_id);
        $(".t" + i + "-store_name").text(data[i].store_name);
        $(".t" + i + "-mer_name").text(data[i].mer_name);
        $(".t" + i + "-tel").text(data[i].tel);
        $(".t" + i + "-email").text(data[i].email);
        $(".t" + i + "-store_address").text(data[i].store_address);
        $(".t" + i + "-store_introduce").text(data[i].store_introduce);
        $(".t" + i + "-time").text(data[i].time);
    }
}
//刷新数据
function ref() {
    getData();
}