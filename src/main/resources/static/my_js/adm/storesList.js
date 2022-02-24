var storeData;
function getData() {
    $.ajax({
        url:'/getstoresList',
        method:'post',
        dataType:'json',
        success:function (d) {
            storeData=d;
            var html='';
            $("#store_num").text(d.length);
            $(".store_tbody").html(html);
            html+='<section id="no-more-tables">' +
                '<table class="table table-bordered table-striped table-condensed cf" id="editable-sample">' +
                '<thead class="cf">' +
                '<tr>' +
                '<th style="text-align: center">店铺头像</th>' +
                '<th style="text-align: center">店铺id</th>' +
                '<th style="text-align: center">店家姓名</th>' +
                '<th style="text-align: center">店铺名称</th>' +
                '<th style="text-align: center">店铺介绍</th>' +
                '<th style="text-align: center">开店时间</th>' +
                '<th style="text-align: center">营业额(元)</th>' +
                '</tr>' +
                '</thead>' +
                '<tbody>';
            for (var i=0;i<d.length;i++) {
                html += "<tr>" +
                    "<td data-title='店铺头像' class='col-sm-1 t" + i + "-img' style='text-align: center'><img style='height: 50px;' src='/fileload/show/" + d[i].store_image + "' alt=''></td>" +
                    "<td data-title='店铺id' class='col-sm-1 t" + i + "-store_id' style='text-align: center'><label class='badge badge-primary'></label></td>" +
                    "<td data-title='店家姓名' class='col-sm-1 t" + i + "-boss_name' style='text-align: center'></td>" +
                    "<td data-title='店铺名称' class='col-sm-2 t" + i + "-store_name' style='text-align: center'></td>" +
                    "<td data-title='店铺介绍' class='col-sm-4 t" + i + "-store_introduce' style='text-align: center'></td>" +
                    "<td data-title='开店时间' class='col-sm-2 t" + i + "-start_time' style='text-align: center'></td>" +
                    "<td data-title='营业额(元)' class='col-sm-1 t" + i + "-turnover' style='text-align: center'></td>" +
                    "</tr>";
            }
        html += "</tbody></table></section>";
        $(".store_tbody").html(html);
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
        // $(".t" + i + "-img").children("img").attr('alt', data[i].icon);
        $(".t" + i + "-store_id").children("label").text(data[i].store_id);
        $(".t" + i + "-boss_name").text(data[i].boss_name);
        $(".t" + i + "-store_name").text(data[i].store_name);
        $(".t" + i + "-store_introduce").text(data[i].store_introduce);
        $(".t" + i + "-start_time").text(data[i].start_time);
        $(".t" + i + "-turnover").text('¥ '+data[i].turnover);
    }
}
//刷新数据
function ref() {
    getData();
}
