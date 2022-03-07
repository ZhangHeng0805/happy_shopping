var merData;
function getData() {
    $.ajax({
        url:'/getMerchantsList',
        method:'post',
        dataType:'json',
        success:function (d) {
            merData=d;
            var html='';
            $("#mer_num").text(d.length);
            $(".mer_tbody").html(html);
            html+='<section id="no-more-tables">' +
                '<table class="table table-bordered table-striped table-condensed cf" id="editable-sample">' +
                '<thead class="cf">' +
                '<tr>' +
                '<th style="text-align: center">店铺图</th>' +
                '<th style="text-align: center">店铺id</th>' +
                '<th style="text-align: center">店铺名</th>' +
                '<th style="text-align: center">商家名</th>' +
                '<th style="text-align: center">商家电话</th>' +
                '<th style="text-align: center">邮箱</th>' +
                '<th style="text-align: center">店铺地址</th>' +
                '<th style="text-align: center">店铺简介</th>' +
                '<th style="text-align: center" title="所有确认收货的订单金额之和">营业额(元)</th>' +
                '<th style="text-align: center">注册时长(天)</th>' +
                '<th style="text-align: center">状态</th>' +
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
                    case 2:
                        staClass='label-warning';
                        staText='账号异常';
                        btnClass='btn-default';
                        btnText='异常账号';
                        cli='Cue('+i+')';
                        break;
                }
                html += "<tr>" +
                    "<td data-title='店铺图片' class='t" + i + "-img' style='text-align: center'><img style='height: 50px;' src='/fileload/show/" + d[i].store_image + "' alt=''></td>" +
                    "<td data-title='店铺id' class='col-sm-1 t" + i + "-store_id' style='text-align: center'><label class='badge badge-primary'></label></td>" +
                    "<td data-title='店铺名称' class='col-sm-1 t" + i + "-store_name' style='text-align: center'></td>" +
                    "<td data-title='商家姓名' class='col-sm-1 t" + i + "-mer_name' style='text-align: center'></td>" +
                    "<td data-title='商家电话' class='col-sm-1 t" + i + "-tel' style='text-align: center'></td>" +
                    "<td data-title='商家邮箱' class='t" + i + "-email' style='text-align: center'></td>" +
                    "<td data-title='店铺地址' class='col-sm-2 t" + i + "-store_address'></td>" +
                    "<td data-title='店铺简介' class='col-sm-2 t" + i + "-store_introduce'></td>" +
                    "<td data-title='营业额(元)' class='col-sm-1 t" + i + "-turnover' style='text-align: center'></td>" +
                    "<td data-title='注册时长(天)' class='col-sm-1 t" + i + "-time' style='text-align: center'></td>" +
                    "<td data-title='账号状态' class='col-sm-1 t" + i + "-state' style='text-align: center'><label class='label "+staClass+"'>"+staText+"</label></td>" +
                    "<td data-title='操作' class='col-sm-1 t" + i + "' style='text-align: center'>" +
                    "<button class='btn "+btnClass+"' onclick='"+cli+"'>"+btnText+"</button><br>" +
                    "<a onclick='reset_pwd("+i+")'>重置密码</a>" +
                    "</td>" +
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
        // $(".t" + i + "-img").children("img").attr('alt', data[i].store_name);
        $(".t" + i + "-store_id").children("label").text(data[i].store_id);
        $(".t" + i + "-store_name").text(data[i].store_name);
        $(".t" + i + "-mer_name").text(data[i].mer_name);
        $(".t" + i + "-tel").text(data[i].tel);
        $(".t" + i + "-email").text(data[i].email);
        $(".t" + i + "-store_address").text(data[i].store_address);
        $(".t" + i + "-store_introduce").text(data[i].store_introduce);
        $(".t" + i + "-turnover").text('¥ '+data[i].turnover);
        $(".t" + i + "-time").text(data[i].time);
    }
}
//刷新数据
function ref() {
    getData();
}
function Ban(i) {
    if (merData[i].state===0){
        var b = confirm("是否封禁["+merData[i].tel+"]的账号？");
        if (b){
            setData(merData[i].tel,1);
        }
    }else {
        alert("错误：该商家的状态非正常账号,无法操作");
    }
}
function Unseal(i) {
    if (merData[i].state===1){
        var b = confirm("是否解封["+merData[i].tel+"]的账号？");
        if (b){
            setData(merData[i].tel,0);
        }
    }else {
        alert("错误：该商家的状态非封禁账号,无法操作");
    }
}
function Cue(i) {
    if (merData[i].state===2){
        alert(merData[i].mer_name+"的账号异常,无法变更,请联系平台检查");
    }else {
        alert("错误：该商家的状态非异常账号,无法操作");
    }
}
function setData(tel,state) {
    $.ajax({
        url:'/set_merchantsState',
        method:'post',
        data:{
            tel:tel,
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
            alert('账号变更错误：'+e);
            window.location.href="/exit_adm";
            window.close();
        }
    })
}
function setDataPwd(tel) {
    $.ajax({
        url:'/reset_merchants_pwd',
        method:'post',
        data:{
            tel:tel
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
    if (merData[i].state===0){
        var tel = merData[i].tel;
        var b = confirm("是否重置["+ tel+"]的账号密码为初始密码");
        if (b){
            setDataPwd(tel);
        }
    }else {
        alert("对不起，该商家的账号状态为非正常,无法操作");
    }
}