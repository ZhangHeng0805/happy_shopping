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
                alert('错误：'+e);
            }
        }
    )
}
data1();
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
            } else {
                $("#un_goodsnum_div").hide();
            }
            document.getElementById("un_goodsnum").innerText=data[0].num;
        },
        error:function (e) {
            alert('错误：'+e);
        }
    })
}
data2();