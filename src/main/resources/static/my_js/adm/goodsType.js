var goodsTypeData;
function getData() {
    $.ajax({
        url:'/getGoodsNum_ByType',
        method:'post',
        dataType:'json',
        success:function (d) {
            goodsTypeData=d;
            var html='';
            $("#goodsType_num").text(d.length);
            $(".goodsType_tbody").html(html);
            html+='<section id="no-more-tables">' +
                '<table class="table table-bordered table-striped table-condensed cf" id="editable-sample">' +
                '<thead class="cf">' +
                '<tr>' +
                '<th style="text-align: center">类型id</th>' +
                '<th style="text-align: center">类型名称</th>' +
                '<th style="text-align: center">商品数量</th>' +
                '<th style="text-align: center">修改操作</th>' +
                '<th style="text-align: center">删除操作</th>' +
                '</tr>' +
                '</thead>' +
                '<tbody>';
            for (var i=0;i<d.length;i++) {
                html += "<tr>" +
                    "<td data-title='类型id' class='col-sm-2 t" + i + "-id' style='text-align: center'><label class='badge badge-primary'></label></td>" +
                    "<td data-title='类型名称' class='col-sm-6 t" + i + "-type' style='text-align: center'><strong class='text-info'></strong></td>" +
                    "<td data-title='商品数量' class='col-sm-2 t" + i + "-num' style='text-align: center'><label class='badge badge-info'></label></td>" +
                    "<td data-title='修改操作' class='col-sm-1 t" + i + "' style='text-align: center'>" +
                    "<button class='btn btn-warning' onclick='Update("+i+")'>修改</button></td>" +
                    "<td data-title='删除操作' class='col-sm-1 t" + i + "' style='text-align: center'>" +
                    "<button class='btn btn-danger' onclick='Del("+i+")'>删除</button></td>" +
                "</tr>";
            }
        html += "</tbody></table></section>";
        $(".goodsType_tbody").html(html);
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
//初始化数据
function inText(data) {
    for (var i=0;i<data.length;i++) {
        $(".t" + i + "-id").children("label").text("#"+data[i].id);
        $(".t" + i + "-num").children("label").text(data[i].num);
        var type=data[i].type;
        if (type=='暂无分类') {
            type="<lable class='label label-danger'>"+type+"</lable>";
            $('.t'+i).children("button").prop("disabled", true);
            $('.t'+i).parent("tr").prop("title","系统默认的商品类型，无法操作")
        }
        $(".t" + i + "-type").children("strong").html(type);
    }
}
//刷新数据
function ref() {
    getData();
}
function Update(i) {
    var person=prompt("请修改商品类型：",goodsTypeData[i].type);
    if (person!=null) {
        if (person.length > 0) {
            if (person.length < 100) {
                var b=true;
                for (var n=0;n<goodsTypeData.length;n++){
                    if (person==goodsTypeData[n].type){
                        b=false;
                        break;
                    }
                }
                if (b){
                    if (goodsTypeData[i].id!=0){
                        setDataType(goodsTypeData[i].id, person);
                    } else {
                        alert("默认商品类型无法操作！");
                    }
                } else {
                    alert("["+person+"]类型已存在，请勿重名");
                }
            } else {
                alert('商品类型长度限制100字符以内');
            }
        } else {
            alert("商品类型不能为空");
        }
    }
}

function Del(i) {
    var b;
    if (goodsTypeData[i].num>0){
        b= confirm("注意：<"+goodsTypeData[i].type+">此类型的商品数量大于0，删除后这些商品的类型将默认为【暂无分类】,确定删除<"+goodsTypeData[i].type+">此商品类型？");
    }else {
        b= confirm("确定删除<"+goodsTypeData[i].type+">此商品类型？");
    }
    if (b){
        if (goodsTypeData[i].id!=0){
            delDataType(goodsTypeData[i].id);
        } else {
            alert("默认商品类型无法操作！");
        }
    }

}
function Add() {
    var type=prompt("请输入新的商品类型","");
    if (type!=null){
        if (type.length>0){
            if (type.length<100){
                var b=true;
                for (var i=0;i<goodsTypeData.length;i++){
                    if (type==goodsTypeData[i].type){
                        b=false;
                        break;
                    }
                }
                if (b){
                    addDataType(type);
                } else {
                    alert("["+type+"]类型已存在，请勿重复添加");
                }
            } else {
                alert("商品类型长度限制100字符以内");
            }
        } else {
            alert("商品类型不能为空");
        }
    }
}
//修改
function setDataType(id,type) {
    $.ajax({
        url:'/update_goodsType',
        method:'post',
        data:{
            id:id,
            type:type
        },
        dataType:'json',
        success:function (data) {
            alert(data.message);
            if (data.code===200){
                ref();
            }
        },
        error:function (e) {
            alert('商品类型变更错误：'+e);
            window.location.href="/exit_adm";
            window.close();
        }
    })
}
//删除
function delDataType(id) {
    $.ajax({
        url:'/del_goodsType',
        method:'post',
        data:{
            id:id
        },
        dataType:'json',
        success:function (data) {
            alert(data.message);
            if (data.code===200){
                ref();
            }
        },
        error:function (e) {
            alert('商品类型变更错误：'+e);
            window.location.href="/exit_adm";
            window.close();
        }
    })
}
//新增
function addDataType(type) {
    $.ajax({
        url:'/add_goodsType',
        method:'post',
        data:{
            type:type
        },
        dataType:'json',
        success:function (data) {
            alert(data.message);
            if (data.code===200){
                ref();
            }
        },
        error:function (e) {
            alert('商品类型变更错误：'+e);
            window.location.href="/exit_adm";
            window.close();
        }
    })
}