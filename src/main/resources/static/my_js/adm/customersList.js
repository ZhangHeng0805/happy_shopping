var cusData;
function getData() {
    $.ajax({
        url:'/getCustomerssList',
        method:'post',
        dataType:'json',
        success:function (d) {
            cusData=d;
            var html='';
            $("#cus_num").text(d.length);
            $(".cus_tbody").html(html);
            html+='<section id="no-more-tables">' +
                '<table class="table table-bordered table-striped table-condensed cf" id="editable-sample">' +
                '<thead class="cf">' +
                '<tr>' +
                '<th style="text-align: center">顾客头像</th>' +
                '<th style="text-align: center">用户名</th>' +
                '<th style="text-align: center">手机号</th>' +
                '<th style="text-align: center">性别</th>' +
                '<th style="text-align: center">地址</th>' +
                '<th style="text-align: center">订单数</th>' +
                '<th style="text-align: center" title="所有订单总金额之和">消费额(元)</th>' +
                '<th style="text-align: center">注册时长(天)</th>' +
                '<th style="text-align: center">账号状态</th>' +
                '<th style="text-align: center">操作</th>' +
                '</tr>' +
                '</thead>' +
                '<tbody>';
            for (var i=0;i<d.length;i++) {
                var staClass,btnClass;
                var staText,btnText;
                var cli;
                switch (d[i].state) {
                    case 0:
                        staClass='label-success';
                        staText='账号正常';
                        btnClass='btn-danger';
                        btnText='封禁账号';
                        cli='Ban('+i+')';
                        break;
                    case 1:
                        staClass='label-danger';
                        staText='违规封禁';
                        btnClass='btn-success';
                        btnText='解封账号';
                        cli='Unseal('+i+')';
                        break;
                }
                html += "<tr>" +
                    "<td data-title='顾客头像' class='col-sm-1 t" + i + "-img' style='text-align: center'><img style='height: 50px;' src='/fileload/show/" + d[i].icon + "' alt=''></td>" +
                    "<td data-title='用户名' class='col-sm-1 t" + i + "-username' style='text-align: center'><label class='badge badge-primary'></label></td>" +
                    "<td data-title='手机号' class='col-sm-1 t" + i + "-phone' style='text-align: center'></td>" +
                    "<td data-title='性别' class='col-sm-1 t" + i + "-sex' style='text-align: center'></td>" +
                    "<td data-title='地址' class='col-sm-3 t" + i + "-address' style='text-align: center'></td>" +
                    "<td data-title='订单数' class='col-sm-1 t" + i + "-orderNum' style='text-align: center'></td>" +
                    "<td data-title='消费额(元)' class='col-sm-1 t" + i + "-orderPrice' style='text-align: center'></td>" +
                    "<td data-title='注册时长(天)' class='col-sm-1 t" + i + "-time' style='text-align: center'></td>" +
                    "<td data-title='账号状态' class='col-sm-1 t" + i + "-state' style='text-align: center'><label class='label "+staClass+"'>"+staText+"</label></td>" +
                    "<td data-title='操作' class='col-sm-1 t" + i + "' style='text-align: center'>" +
                    "<button class='btn "+btnClass+"' onclick='"+cli+"'>"+btnText+"</button><br>" +
                    "<a onclick='reset_pwd("+i+")'>重置密码</a>" +
                    "</td>" +
                "</tr>";

            }
        html += "</tbody></table></section>";
        $(".cus_tbody").html(html);
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
        $(".t" + i + "-username").children("label").text(data[i].username);
        $(".t" + i + "-phone").text(data[i].phone);
        var html_sex='';
        if (data[i].sex=='男') {
            html_sex='<lable class="label label-info">'+data[i].sex+'<label>';
        }else {
            html_sex='<lable class="label label-warning">'+data[i].sex+'<label>';
        }
        $(".t" + i + "-sex").html(html_sex);
        $(".t" + i + "-tel").text(data[i].tel);
        $(".t" + i + "-address").text(data[i].address);
        $(".t" + i + "-orderNum").text(data[i].orderNum);
        $(".t" + i + "-orderPrice").text('¥ '+data[i].orderPrice);
        $(".t" + i + "-time").text(data[i].time);
    }
}
//刷新数据
function ref() {
    getData();
}
function Ban(i) {
    if (cusData[i].state===0){
        var b = confirm("是否封禁["+cusData[i].phone+"]的账号？");
        if (b){
            setDataState(cusData[i].phone,1);
        }
    }else {
        alert("错误：该顾客的账号状态为非正常,无法操作");
    }
}
function Unseal(i) {
    if (cusData[i].state===1){
        var b = confirm("是否解封["+cusData[i].phone+"]的账号？");
        if (b){
            setDataState(cusData[i].phone,0);
        }
    }else {
        alert("错误：该顾客的账号状态为非封禁,无法操作");
    }
}

function setDataState(phone,state) {
    $.ajax({
        url:'/set_customerState',
        method:'post',
        data:{
            phone:phone,
            state:state
        },
        dataType:'json',
        success:function (data) {
            alert(data.message);
            if (data.code===200){
                ref();
            }
        },
        error:function (e) {
            alert('账号状态变更错误：'+e);
            window.location.href="/exit_adm";
            window.close();
        }
    })
}
function setDataPwd(phone) {
    $.ajax({
        url:'/reset_customer_pwd',
        method:'post',
        data:{
            phone:phone
        },
        dataType:'json',
        success:function (data) {
            alert(data.message);
            if (data.code===200){
            }
        },
        error:function (e) {
            alert('账号密码重置错误：'+e);
            window.location.href="/exit_adm";
            window.close();
        }
    })
}
function reset_pwd(i) {
    if (cusData[i].state===0){
        var b = confirm("是否重置["+cusData[i].phone+"]的账号密码为初始密码");
        if (b){
            setDataPwd(cusData[i].phone);
        }
    }else {
        alert("对不起，该顾客的账号状态为非正常,无法操作");
    }
}