var ChatConfig;
function getChatConfig() {
    $("#btn_excel").hide();
    $.ajax({
        url:'/getChatConfig',
        method:'post',
        dataType:'json',
        success:function (d) {
            ChatConfig=d;
            var html='';
            $("#chat_tbody").html(html);
            html+='<section id="no-more-tables">' +
                '<table class="table table-bordered table-striped table-condensed cf" id="editable-sample">' +
                '<thead class="cf">' +
                '<tr>' +
                '<th style="text-align: center">编号</th>' +
                '<th style="text-align: center">服务器ip</th>' +
                '<th style="text-align: center">服务器端口</th>' +
                '<th style="text-align: center" title="注意：本地端口号是客户端获取配置依据，可以随便设置，但是不能重复">本地端口</th>' +
                '<th style="text-align: center">说明</th>' +
                '</tr>' +
                '</thead>' +
                '<tbody>';
            for (var i=0;i<d.length;i++) {
                html += "<tr>" +
                    "<td data-title='编号' class='col-sm-1 t" + i + "-id' style='text-align: center'><label class='label label-success'></label></td>" +
                    "<td data-title='服务器ip' class='col-sm-3 t" + i + "-ip' ondblclick='IP("+i+")' style='text-align: center' title='双击修改数据'><label class='badge badge-primary'></label></td>" +
                    "<td data-title='服务器端口' class='col-sm-1 t" + i + "-port' ondblclick='Port("+i+")' style='text-align: center' title='双击修改数据'><label class='badge badge-info'></label></td>" +
                    "<td data-title='本地端口' class='col-sm-1 t" + i + "-localPort' ondblclick='LocalPort("+i+")' style='text-align: center' title='双击修改数据'><label class='label label-warning'></label></td>" +
                    "<td data-title='说明' class='col-sm-5 t" + i + "-explain' ondblclick='Explain("+i+")' style='text-align: center' title='双击修改数据'></td>" +
                    "</tr>";
            }
            html += "</tbody></table></section>";
            $(".chat_tbody").html(html);
            inText1(d);
            EditableTable.init();
        },
        error:function (e) {
            alert('ChatConfig表格数据错误：'+e);
            window.location.href="/exit_adm";
            window.close();
        }
    })
}
//ChatConfig数据初始化
function inText1(d) {
    for (var i=0;i<d.length;i++) {
        $(".t" + i + "-id").children("label").text(d[i].id);
        $(".t" + i + "-ip").children("label").text(d[i].ip);
        $(".t" + i + "-port").children("label").text(d[i].port);
        $(".t" + i + "-localPort").children("label").text(d[i].localPort);
        var explain='';
        explain=d[i].explain;
        $(".t" + i + "-explain").text(explain);
    }
    if (d.length>0){
        $("#btn_excel").show();
    }
}
//刷新数据
function ref(){
    getChatConfig();
}
//修改ip
function IP(i) {
    var ip=prompt("请输入编号["+ChatConfig[i].id+"]聊天服务的服务器ip地址:",ChatConfig[i].ip);
    if (ip!=null){
        if (ip.length>0){
            $.ajax({
                url:'/set_ChatConfig',
                method:'post',
                data:{
                    id:ChatConfig[i].id,
                    ip:ip
                },
                dataType:'json',
                success:function (data) {
                    alert(data.message);
                    if (data.code===200){
                        ref();
                    }
                },
                error:function (e) {
                    alert('ip变更错误：'+e);
                    window.location.href="/exit_adm";
                    window.close();
                }
            })
        } else {
           alert('聊天服务的ip地址不能为空')
        }
    }
}
//修改port
function Port(i) {
    var port=prompt("请输入编号["+ChatConfig[i].id+"]聊天服务的服务器端口号:",ChatConfig[i].port);
    if (port!=null){
        if (port.length>0){
            $.ajax({
                url:'/set_ChatConfig',
                method:'post',
                data:{
                    id:ChatConfig[i].id,
                    port:port
                },
                dataType:'json',
                success:function (data) {
                    alert(data.message);
                    if (data.code===200){
                        ref();
                    }
                },
                error:function (e) {
                    alert('port变更错误：'+e);
                    window.location.href="/exit_adm";
                    window.close();
                }
            })
        } else {
            alert('聊天服务的端口不能为空')
        }
    }
}
//修改localPort
function LocalPort(i) {
    var localPort=prompt("请输入编号["+ChatConfig[i].id+"]聊天服务的本地端口号:",ChatConfig[i].localPort);
    if (localPort!=null){
        if (localPort.length>0){
            var flag=true;
            for (var n=0;n<ChatConfig.length;n++){
                if (localPort==ChatConfig[n].localPort){
                    flag=false;
                    break;
                }
            }
            if (flag) {
                $.ajax({
                    url: '/set_ChatConfig',
                    method: 'post',
                    data: {
                        id: ChatConfig[i].id,
                        localPort: localPort
                    },
                    dataType: 'json',
                    success: function (data) {
                        alert(data.message);
                        if (data.code === 200) {
                            ref();
                        }
                    },
                    error: function (e) {
                        alert('localPort变更错误：' + e);
                        window.location.href = "/exit_adm";
                        window.close();
                    }
                });
            }else {
                alert('聊天服务的本地端口不能重复');
            }
        } else {
            alert('聊天服务的本地端口不能为空');
        }
    }
}
//修改explain
function Explain(i) {
    var explain=prompt("请输入编号["+ChatConfig[i].id+"]聊天服务的说明:",ChatConfig[i].explain);
    if (explain!=null){
        if (explain.length>0){
            $.ajax({
                url:'/set_ChatConfig',
                method:'post',
                data:{
                    id:ChatConfig[i].id,
                    explain:explain
                },
                dataType:'json',
                success:function (data) {
                    alert(data.message);
                    if (data.code===200){
                        ref();
                    }
                },
                error:function (e) {
                    alert('explain变更错误：'+e);
                    window.location.href="/exit_adm";
                    window.close();
                }
            })
        } else {
            alert('聊天服务的说明不能为空')
        }
    }
}
//导出为Excel
$("#btn_excel").click(function () {
    var excel = new ExcelGen({
        "src_id": "editable-sample",
        "show_header": true,
        "auto_format": true,
        "author":"乐在购物网_管理员",
        "file_name": "系统后台管理[聊天服务配置]_"+getTime()+".xlsx"
    });
    excel.generate();
});
$("#btn_new").click(function () {
    var ip=prompt("请输入服务器ip");
    if (ip!=null&&ip.length>0){
        var port=prompt("请输入服务器端口号");
        if (port!=null&&port.length>0){
            var localPort=prompt("请输入本地端口号");
            if (localPort!=null&&localPort.length>0){
                var flag=true;
                for (var n=0;n<ChatConfig.length;n++){
                    if (localPort==ChatConfig[n].localPort){
                        flag=false;
                        break;
                    }
                }
                if (flag){
                    var explain=prompt("请输入服务器配置说明");
                    if (explain!=null&&explain.length>0){
                        $.ajax({
                            url:'/add_ChatConfig',
                            method:'post',
                            data:{
                                ip:ip,
                                port:port,
                                localPort:localPort,
                                explain:explain
                            },
                            dataType:'json',
                            success:function (data) {
                                alert(data.message);
                                if (data.code===200){
                                    ref();
                                }
                            },
                            error:function (e) {
                                alert('聊天配置新增错误：'+e);
                                window.location.href="/exit_adm";
                                window.close();
                            }
                        });
                    } else {
                        alert("请输入服务器配置说明");
                    }
                }else {
                    alert("本地端口号不能重复！");
                }
            } else {
                alert("请输入服务器本地端口号");
            }
        } else {
            alert("请输入服务器端口号");
        }
    } else {
        alert("请输入服务器ip地址");
    }
});