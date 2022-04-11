
var LoginLog;//登录日志
var AppData;//App数据
var sta=1;//当前界面

function init(){
    $(".tbody_t1").html('');
    $(".tbody_t2").html('');
}

$('#select_log').change(function () {
   getLoginLog($('#select_log').val()) ;
});
function getLoginLog(type) {
    $.ajax({
        url:'/getLogByType',
        method:'post',
        data:{
          type:type
        },
        dataType:'json',
        success:function (d) {
            LoginLog=d;
            var html='';
            $("#t1_title").text(d.length);
            html+='<section id="no-more-tables">' +
                '<table class="table table-bordered table-striped table-condensed cf" id="editable-sample">' +
                '<thead class="cf">' +
                '<tr>' +
                '<th style="text-align: center">id</th>' +
                '<th style="text-align: center">账号</th>' +
                '<th style="text-align: center">访问信息</th>' +
                '<th style="text-align: center">重复操作</th>' +
                '<th style="text-align: center">操作时间</th>' +
                '<th style="text-align: center">操作信息</th>' +
                '</tr>' +
                '</thead>' +
                '<tbody>';
            for (var i=0;i<d.length;i++) {
                html += "<tr>" +
                    "<td data-title='id' class='t" + i + "-id' style='text-align: center'><label class='badge badge-primary'></label></td>" +
                    "<td data-title='手机号' class='col-sm-1 t" + i + "-tel' ondblclick='IP("+i+")' style='text-align: center'><label class='label label-success'></label></td>" +
                    "<td data-title='访问信息' class='col-sm-6 t" + i + "-request'  style='text-align: center' ></td>" +
                    "<td data-title='重复操作' class='col-sm-1 t" + i + "-count' style='text-align: center' ><label class='badge badge-inverse'></label></td>" +
                    "<td data-title='操作时间' class='col-sm-2 t" + i + "-time' style='text-align: center' ></td>" +
                    "<td data-title='操作信息' class='col-sm-2 t" + i + "-info' style='text-align: center'></td>" +
                    "</tr>";
            }
            html += "</tbody></table></section>";
            $(".tbody_t1").html(html);
            inText1(d);
            EditableTable.init();
        },
        error:function (e) {
            alert('LoginLog表格数据错误：'+e);
            window.location.href="/exit_adm";
            window.close();
        }
    })
}
//loginLog数据初始化
function inText1(d) {
    for (var i=0;i<d.length;i++) {
        $(".t" + i + "-id").children("label").text(d[i].id);
        $(".t" + i + "-tel").children("label").text(d[i].tel);
        $(".t" + i + "-request").text(d[i].request);
        $(".t" + i + "-count").children("label").text(d[i].count);
        $(".t" + i + "-time").text(d[i].time);
        $(".t" + i + "-info").text(d[i].info);
    }
}
function getAppData() {
    $.ajax({
        url:'/getPhoneInfoList',
        method:'post',
        dataType:'json',
        success:function (d) {
            AppData=d;
            var html='';
            $("#t2_title").text(d.length);
            html+='<section id="no-more-tables">' +
                '<table class="table table-bordered table-striped table-condensed cf" id="editable-sample">' +
                '<thead class="cf">' +
                '<tr>' +
                '<th style="text-align: center">设备ID</th>' +
                '<th style="text-align: center">IP地址</th>' +
                '<th style="text-align: center">设备型号</th>' +
                '<th style="text-align: center">手机号</th>' +
                '<th style="text-align: center">运营商</th>' +
                '<th style="text-align: center">sdk版本</th>' +
                '<th style="text-align: center">android版本</th>' +
                '<th style="text-align: center">app型号</th>' +
                '<th style="text-align: center">最近访问</th>' +
                '</tr>' +
                '</thead>' +
                '<tbody>';
            for (var i=0;i<d.length;i++) {
                html += "<tr>" +
                    "<td data-title='设备ID' class='p" + i + "-id' style='text-align: center'><label class='badge badge-primary'></label></td>" +
                    "<td data-title='IP地址' class='col-sm-1 p" + i + "-ip' ondblclick='IP("+i+")' style='text-align: center'><label class='label label-success'></label></td>" +
                    "<td data-title='设备型号' class='col-sm-1 p" + i + "-model'  style='text-align: center' ></td>" +
                    "<td data-title='手机号' class='col-sm-1 p" + i + "-tel' style='text-align: center' ><label class='badge badge-inverse'></label></td>" +
                    "<td data-title='运营商' class='col-sm-1 p" + i + "-tel_type' style='text-align: center' ></td>" +
                    "<td data-title='sdk版本' class='col-sm-1 p" + i + "-sdk' style='text-align: center' ><label class='badge badge-info'></label></td>" +
                    "<td data-title='android版本' class='col-sm-1 p" + i + "-release' style='text-align: center' ></td>" +
                    "<td data-title='app型号' class='col-sm-2 p" + i + "-app_version' style='text-align: center' ></td>" +
                    "<td data-title='最近访问' class='col-sm-2 p" + i + "-last_time' style='text-align: center'></td>" +
                    "</tr>";
            }
            html += "</tbody></table></section>";
            $(".tbody_t2").html(html);
            inText2(d);
            EditableTable.init();
        },
        error:function (e) {
            alert('AppData表格数据错误：'+e);
            window.location.href="/exit_adm";
            window.close();
        }
    })
}
//AppData数据初始化
function inText2(d) {
    for (var i=0;i<d.length;i++) {
        $(".p" + i + "-id").children("label").text(d[i].id);
        $(".p" + i + "-ip").children("label").text(d[i].ip);
        $(".p" + i + "-sdk").children("label").text(d[i].sdk);
        $(".p" + i + "-model").text(d[i].model);
        $(".p" + i + "-tel").children("label").text(d[i].tel);
        $(".p" + i + "-tel_type").text(d[i].tel_type);
        $(".p" + i + "-release").text(d[i].release);
        $(".p" + i + "-app_version").text(d[i].app_version);
        $(".p" + i + "-last_time").text(d[i].last_time);
    }
}
function ref(i){
    sta=i;
    init();
    switch (i) {
        case 1:
            getLoginLog($('#select_log').val()) ;
            break;
        case 2:
            getAppData();
            break;
    }


}
