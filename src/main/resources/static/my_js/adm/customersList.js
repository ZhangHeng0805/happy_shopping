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
                '<th style="text-align: center">顾客头像</th>' +
                '<th style="text-align: center">用户名</th>' +
                '<th style="text-align: center">手机号</th>' +
                '<th style="text-align: center">性别</th>' +
                '<th style="text-align: center">地址</th>' +
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
                    "<td data-title='顾客头像' class='t" + i + "-img' style='text-align: center'><img style='height: 50px;' src='/fileload/show/" + d[i].store_image + "' alt=''></td>" +
                    "<td data-title='用户名' class='col-sm-1 t" + i + "-store_id' style='text-align: center'><label class='badge badge-primary'></label></td>" +
                    "<td data-title='手机号' class='col-sm-1 t" + i + "-store_name' style='text-align: center'></td>" +
                    "<td data-title='性别' class='col-sm-1 t" + i + "-mer_name' style='text-align: center'></td>" +
                    "<td data-title='地址' class='col-sm-2 t" + i + "-tel' style='text-align: center'></td>" +
                    "<td data-title='注册时长(天)' class='col-sm-1 t" + i + "-time' style='text-align: center'></td>" +
                    "<td data-title='账号状态' class='col-sm-1 t" + i + "-state' style='text-align: center'><label class='label "+staClass+"'>"+staText+"</label></td>" +
                    "<td data-title='操作' class='col-sm-1 t" + i + "' style='text-align: center'><button class='btn "+btnClass+"' onclick='"+cli+"'>"+btnText+"</button></td>" +
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
        $(".t" + i + "-turnover").text(data[i].turnover);
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