//根据商品类型查询本店商品数量
function data1() {
    $.ajax({
            url:'/findGoodsNumByType',
            method:'post',
            dataType:'json',
            success:function (data) {
                document.getElementById("zhuzhuang-data-js").innerHTML='';
                // console.log(data);
                Morris.Bar({
                    element: 'zhuzhuang-data-js',
                    data: data,
                    xkey: 'type',
                    ykeys: ['num'],
                    labels: ['商品数量'],
                    barColors:['#6dc5a3']
                });
                var count=0;
                for (var i=0;i<data.length;i++){
                    count+=data[i].num;
                }
                // console.log(count);
                document.getElementById("zhuzhuang-data").innerHTML=count;
            },
            error:function (e) {
                alert('data1错误：'+e);
                window.location.href="/exit_mer";
            }
        }
    )
}
//根据商品的订单状态查询本店订单商品数量
function data2() {
    $.ajax({
        url:'/findGoodsNumByOrderState',
        method:'post',
        dataType:'json',
        success:function (data) {
            // console.log(data);
            for (var i=0;i<data.length;i++){
                document.getElementById("state"+data[i].state).innerHTML=data[i].num;
            }
            document.getElementById("count_num").innerText=data[3].num;
            if (data[0].num>0){
                $("#un_goodsnum_div").show();
                $("#tit_goodsnum_div").hide();
            } else {
                $("#un_goodsnum_div").hide();
                $("#tit_goodsnum_div").show();
            }
            $(".un_goodsnum").html(data[0].num);
        },
        error:function (e) {
            alert('data2错误：'+e);
            window.location.href="/exit_mer";
        }
    })
}
//查询本店总营业额
function data3() {
  $.ajax({
      url:'/count_price?t='+Date.now(),
      method:'get',
      success:function (data) {
          document.getElementById("count_price").innerText=data.toString();
      },
      error:function (e) {
          alert('data3错误：'+e);
          window.location.href="/exit_mer";
      }
  })
}
//查询近几天的订单数据
function data4() {
    $.ajax({
        url:'/findGoodnumBytimeAndtype?t='+Date.now(),
        method:'get',
        data:{
            count_down:10
        },
        dataType:'json',
        success:function (data) {
            // console.log(data);
            $("#days").text(data.length);
            document.getElementById("huxian-data-js").innerHTML='';
            Morris.Line({
                element: 'huxian-data-js',
                data: data,
                xkey: 'time',
                ykeys: ['sta0','sta1','sta2','sta3','sta4'],
                labels: ['待处理','拒绝发货',"待收货","已收货","退货"],
                lineColors:['#f4be00','#e300eb','#07ade1','#0aeb00','#ff0003'],
                parseTime: false
            });
            var datum = data[data.length-1];//最新一天数据
            var count=datum.sta0+datum.sta1+datum.sta2+datum.sta3+datum.sta4;
            $("#count_order").text(count);
        },
        error:function (e) {
            console.log(e);
            alert('data4错误：'+e);
            window.location.href="/exit_mer";
        }
    })
}
//查询本店今日营业额
function data6(){
    $.ajax({
        url:'/findTodayTurnover',
        method:'post',
        dataType:'json',
        success:function (data) {
            $("#today_money").text('¥ '+data)
        },
        error:function (e) {
            console.log(e);
            alert('data6错误：'+e);
            window.location.href="/exit_mer";
        }
    })
}
//查询本店未处理的订单商品
function data5() {
    $.ajax({
        url:'/un_goodsOrder',
        method:'post',
        dataType:'json',
        success:function (data) {
            $("#un_goodsnum").html(data.length);
            var body = $(".goods_tbody");
            var html='';
            body.html(html);
            html+='<table class="table table-bordered table-striped table-condensed cf" id="editable-sample">' +
                    '<thead class="cf">' +
                    '<tr>' +
                    '<th style="text-align: center">收货人</th>' +
                    '<th style="text-align: center">订单时间</th>' +
                    '<th style="text-align: center">联系电话</th>' +
                    '<th style="text-align: center">收货地址</th>' +
                    '<th style="text-align: center">商品id</th>' +
                    '<th style="text-align: center">商品名</th>' +
                    '<th style="text-align: center">单价</th>' +
                    '<th style="text-align: center">数量</th>' +
                    '<th style="text-align: center">总价</th>' +
                    '<th style="text-align: center">确认订单</th>' +
                    '<th style="text-align: center">拒绝订单</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>';
            for (var i=0;i<data.length;i++){
                html+="<tr>" +
                    "<td data-title='收货人' class='col-sm-1 t"+i+"-username' style='text-align: center'></td>" +
                    "<td data-title='订单时间' class='col-sm-1 t"+i+"-time' style='text-align: center'></td>" +
                    "<td data-title='联系电话' class='col-sm-1 t"+i+"-tel' style='text-align: center'></td>" +
                    "<td data-title='收货地址' class='col-sm-2 t"+i+"-address' style='text-align: center'></td>" +
                    "<td data-title='商品id' class='col-sm-1 t"+i+"-goods_id' style='text-align: center'><label class='label label-default'></label></td>" +
                    "<td data-title='商品名' class='col-sm-1 t"+i+"-goods_name' style='text-align: center'></td>" +
                    "<td data-title='单价' class='col-sm-1 t"+i+"-price' style='text-align: center'></td>" +
                    "<td data-title='数量' class='col-sm-1 t"+i+"-num' style='text-align: center'></td>" +
                    "<td data-title='总价' class='col-sm-1 t"+i+"-all_price' style='text-align: center'></td>" +
                    "<td data-title='确认订单' class='col-sm-1' style='text-align: center'><button class='btn btn-success ok_order' onclick='ok("+data[i].id_goods+",\""+data[i].goods_name+"\")' >确认订单</button></td>" +
                    "<td data-title='拒绝订单' class='col-sm-1' style='text-align: center'><button class='btn btn-danger no_order' onclick='no("+data[i].id_goods+",\""+data[i].goods_name+"\")'>拒绝订单</button></td>" +
                    "</tr>";
            }
            html+="</tbody></table>";
            body.html(html);
            inText(data);
            EditableTable.init();
        },
        error:function (e) {
            alert('data5错误：'+e);
            window.location.href="/exit_mer";
        }
    })
}
//填充数据
function inText(data){
    for (var i=0;i<data.length;i++) {
        $(".t"+i+"-username").text(data[i].username);
        $(".t"+i+"-time").text(data[i].time);
        $(".t"+i+"-tel").text(data[i].tel);
        $(".t"+i+"-address").text(data[i].address);
        $(".t"+i+"-goods_name").text(data[i].goods_name);
        $(".t"+i+"-goods_id").children("label").text(data[i].goods_id);
        $(".t"+i+"-price").text(data[i].price);
        $(".t"+i+"-num").text(data[i].num);
        $(".t"+i+"-all_price").text(data[i].all_price);
    }
}
//滚动至未处理订单表格处
$(".un_goodsnum_div").click(function () {
    $("html,body").animate({
        scrollTop:$(".goods_tbody").offset().top
    },800);
});
//确认订单
function ok(id,name) {
    var d=confirm("确定接受此订单商品（"+name+"）？");
    if(d) {
        $.ajax({
            url:"/Ok_Order",
            method:'post',
            dataType:'json',
            data:{
                id:id
            },
            success:function (data) {
                if (data.code===200){
                    alert("[成功]："+data.message);
                    ref();
                } else {
                    alert("[失败]："+data.message);
                }
            },
            error:function (e) {
                alert('确认订单错误：'+e);
                window.location.href="/exit_mer";
            }

        });
        return false;
    }
}

//拒绝订单
function no(id,name) {
    var d=confirm("确定拒绝此订单商品（"+name+"）？");
    if(d) {
        $.ajax({
            url:"/No_Order",
            method:'post',
            dataType:'json',
            data:{
                id:id
            },
            success:function (data) {
                if (data.code===200){
                    alert("[成功]；"+data.message);
                    ref();
                } else {
                    alert("[失败]；"+data.message);
                }
            },
            error:function (e) {
                alert('拒绝订单错误：'+e);
                window.location.href="/exit_mer";
            }

        });
        return false;
    }
}
//刷新
function ref() {
    data1();//根据商品类型查询本店商品数量
    data2();//根据商品的订单状态查询本店订单商品数量（店铺订单数据标签）
    data3();//查询本店总营业额
    data4();//查询近几天的订单数据（订单数据曲线图）
    data5();//查询本店未处理的订单商品(未处理商品订单的表格)
    data6();//查询本店今日营业额
}