
function get_count() {
    $.ajax({
        url:'/get_count',
        method:'post',
        dataType:'json',
        success:function (d) {
            $("#count_num").text(d.count_num);
            $("#count_price").text(d.count_price);
            $("#count_merchants").text(d.count_merchants);
            $("#count_customer").text(d.count_customer);
        },
        error:function (e) {
            alert("获取统计数据错误："+e);
            window.location.href="/exit_adm";
        }
    })
}
function get_order() {
    $.ajax({
        url:'/get_order',
        method:'post',
        success:function (data) {
            console.log(data);
            // Morris数据扇形图
            Morris.Donut({
                element: 'graph-donut',
                data: data,
                backgroundColor: false,
                labelColor: '#fff',
                colors: [
                    '#6a8bbe','#f4c223','#5ab6df','#4bcacc','#fb8575'
                ],
                formatter: function (x, data) { return data.formatted; }
            });

        },
        error:function (e) {
            alert("获取订单数据错误："+e);
            window.location.href="/exit_adm";
        }
    })
}
//根据商品类型查询本店商品数量
function data1() {
    $.ajax({
            url:'/getGoodsNum_ByState',
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
                window.location.href="/exit_adm";
            }
        }
    )
}