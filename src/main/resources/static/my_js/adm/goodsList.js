var goodsData;
function getData() {
    $.ajax({
        url:'/getGoodsList',
        method:'post',
        dataType:'json',
        success:function (d) {
            goodsData=d;
            var html='';
            $("#goods_num").text(d.length);
            $(".goods_tbody").html(html);
            html+='<section id="no-more-tables">' +
                '<table class="table table-bordered table-striped table-condensed cf" id="editable-sample">' +
                '<thead class="cf">' +
                '<tr>' +
                '<th style="text-align: center">商品图</th>' +
                '<th style="text-align: center">商品id</th>' +
                '<th style="text-align: center">商品名</th>' +
                '<th style="text-align: center">类型</th>' +
                '<th style="text-align: center">简介</th>' +
                '<th style="text-align: center">销量</th>' +
                '<th style="text-align: center">库存</th>' +
                '<th style="text-align: center">价格</th>' +
                '<th style="text-align: center">店铺名</th>' +
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
                        staText='已上线';
                        btnClass='btn-danger';
                        btnText='违规下架';
                        cli='Ban('+i+')';
                        break;
                    case 1:
                        staClass='label-default';
                        staText='已下线';
                        btnClass='btn-success';
                        btnText='上架商品';
                        cli='Unseal('+i+')';
                        break;
                    case 2:
                        staClass='label-warning';
                        staText='审核中';
                        btnClass='btn-info';
                        btnText='审核通过';
                        cli='Examine('+i+')';
                        break;
                    case 3:
                        staClass='label-danger';
                        staText='违规商品';
                        btnClass='btn-default';
                        btnText='提示整改';
                        cli='Tips('+i+')';
                        break;
                }
                html += "<tr>" +
                    "<td data-title='商品图' class='col-sm-1 t" + i + "-img' style='text-align: center'><img style='height: 50px;' src='/fileload/show/" + d[i].goods_image + "' alt=''></td>" +
                    "<td data-title='商品id' class='col-sm-1 t" + i + "-goods_id' style='text-align: center'><label class='badge badge-info'></label></td>" +
                    "<td data-title='商品名' class='col-sm-1 t" + i + "-goods_name' style='text-align: center'></td>" +
                    "<td data-title='类型' class='col-sm-1 t" + i + "-goods_type' style='text-align: center'></td>" +
                    "<td data-title='简介' class='col-sm-3 t" + i + "-goods_introduction' style='text-align: center'></td>" +
                    "<td data-title='销量' class='col-sm-1 t" + i + "-goods_sales' style='text-align: center'></td>" +
                    "<td data-title='库存' class='col-sm-1 t" + i + "-goods_num' style='text-align: center'></td>" +
                    "<td data-title='价格' class='col-sm-1 t" + i + "-goods_price' style='text-align: center'></td>" +
                    "<td data-title='店铺名' class='col-sm-1 t" + i + "-store_name' style='text-align: center'><label class='label "+staClass+"'>"+staText+"</label></td>" +
                    "<td data-title='状态' class='col-sm-1 t" + i + "-state' style='text-align: center'><label class='label "+staClass+"'>"+staText+"</label></td>" +
                    "<td data-title='操作' class='col-sm-1 t" + i + "' style='text-align: center'>" +
                    "<button class='btn "+btnClass+"' onclick='"+cli+"'>"+btnText+"</button></td>" +
                "</tr>";
            }
        html += "</tbody></table></section>";
        $(".goods_tbody").html(html);
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
        $(".t" + i + "-goods_id").children("label").text(data[i].goods_id);
        $(".t" + i + "-goods_name").text(data[i].goods_name);
        $(".t" + i + "-goods_type").text(data[i].goods_type);
        $(".t" + i + "-goods_introduction").text(data[i].goods_introduction);
        $(".t" + i + "-goods_sales").text(data[i].goods_sales);
        $(".t" + i + "-goods_num").text(data[i].goods_num);
        $(".t" + i + "-goods_price").text('¥ '+data[i].goods_price);
        $(".t" + i + "-store_name").text(data[i].store_name);
    }
}
//刷新数据
function ref() {
    getData();
}
function Ban(i) {
    if (goodsData[i].state===0){
        var b = confirm("是否下架该违规商品:<"+goodsData[i].goods_name+">？");
        if (b){
            setDataState(goodsData[i].goods_id,3);
        }
    }
}
function Unseal(i) {
    if (goodsData[i].state===1){
        var b = confirm("是否上架该商品:<"+goodsData[i].goods_name+">？");
        if (b){
            setDataState(goodsData[i].goods_id,0);
        }
    }else {
        alert("商品状态错误！")
    }
}
function Examine(i) {
    if (goodsData[i].state===2){
        var b = confirm("是否通过审核该商品:<"+goodsData[i].goods_name+">？");
        if (b){
            setDataState(goodsData[i].goods_id,0);
        }
    }else {
        alert("商品状态错误！")
    }
}
function Tips(i) {
    if (goodsData[i].state===3){
        var b = confirm("是否提示商家整改该商品:<"+goodsData[i].goods_name+">？");
        if (b){
            alert("已提示商家整改该商品:<"+goodsData[i].goods_name+">")
        }
    }else {
        alert("商品状态错误！")
    }
}
function setDataState(id,state) {
    $.ajax({
        url:'/set_goodsState',
        method:'post',
        data:{
            id:id,
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
            alert('商品状态变更错误：'+e);
            window.location.href="/exit_adm";
            window.close();
        }
    })
}
