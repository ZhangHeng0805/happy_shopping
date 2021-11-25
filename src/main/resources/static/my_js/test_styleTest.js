(function(){
    var t;
    function size(animate){
        if (animate == undefined){
            animate = false;
        }
        clearTimeout(t);
        t = setTimeout(function(){
            $("canvas").each(function(i,el){
                $(el).attr({
                    "width":$(el).parent().width(),
                    "height":$(el).parent().outerHeight()
                });
            });
            redraw(animate);
            var m = 0;
            $(".chartJS").height("");
            $(".chartJS").each(function(i,el){ m = Math.max(m,$(el).height()); });
            $(".chartJS").height(m);
        }, 30);
    }
    $(window).on('resize', function(){ size(false); });


    function redraw(animation){
        var options = {};
        if (!animation){
            options.animation = false;
        } else {
            options.animation = true;
        }

        //Chart基础柱状图数据
        var barChartData = {
            labels : ["星期一","星期二","星期三","星期四","星期五","星期六","星期日"],
            datasets : [
                {
                    fillColor : "#2a323f",
                    strokeColor : "#2a323f",
                    data : [65,59,90,81,56,55,40]
                },
                {
                    fillColor : "#6dc5a3",
                    strokeColor : "#6dc5a3",
                    data : [28,48,40,19,96,27,100]
                }
            ]
        };
        //Chart基础柱状图
        var myLine = new Chart(document.getElementById("zhu-chart-js").getContext("2d")).Bar(barChartData);

        //Chart基础扇形图数据
        var pieData = [
            {
                value: 30,
                color:"#2a323f"
            },
            {
                value : 50,
                color : "#5f728f"
            },
            {
                value : 100,
                color : "#6dc5a3"
            }

        ];
        //Chart基础扇形图
        var myPie = new Chart(document.getElementById("shanxing-chart-js").getContext("2d")).Pie(pieData);

        //Chart基础环形图数据
        var donutData = [
            {
                value: 30,
                color:"#2a323f"
            },
            {
                value : 50,
                color : "#5f728f"
            },
            {
                value : 100,
                color : "#6dc5a3"
            },
            {
                value : 40,
                color : "#95D7BB"
            },
            {
                value : 120,
                color : "#b8d3f5"
            }

        ];
        //Chart基础环形图
        var myDonut = new Chart(document.getElementById("huanxing-chart-js").getContext("2d")).Doughnut(donutData);

    }
    size(true);
}());

// Morris数据扇形图
Morris.Donut({
    element: 'huanxing-data-js',
    data: [
        {value: 70, label: '手机', formatted: '70%' },
        {value: 15.5, label: '电脑', formatted: '15.5%' },
        {value: 10, label: '电视', formatted: '10%' },
        {value: 4.5, label: '其他', formatted: '4.5%' },
        {value: 0, label: 'iPad', formatted: '0%' }
    ],
    backgroundColor: '#fff',
    labelColor: '#1fb5ac',
    colors: [
        '#414e62','#788ba0','#6dc5a3','#95D7BB'
    ],
    formatter: function (x, data) { return data.formatted; }
});

// Morris数据山峰图
Morris.Area({
    element: 'shanfeng-data-js',
    behaveLikeLine: false,
    gridEnabled: false,
    gridLineColor: '#dddddd',
    axes: true,
    fillOpacity:.7,
    data: [
        {时间: '2021-01', 华为: 1000, 小米: 780, 苹果: 980},
        {时间: '2021-02', 华为: 1778, 小米: 2294, 苹果: 1841},
        {时间: '2021-03', 华为: 1512, 小米: 2969, 苹果: 3501},
        {时间: '2021-04', 华为: 1267, 小米: 3597, 苹果: 5689},
        {时间: '2021-05', 华为: 6810, 小米: 1914, 苹果: 2293},
        {时间: '2021-06', 华为: 5670, 小米: 4293, 苹果: 1881},
        {时间: '2021-07', 华为: 4820, 小米: 3795, 苹果: 1588},
        {时间: '2021-08', 华为: 2503, 小米: 5967, 苹果: 5175},
        {时间: '2021-09', 华为: 1067, 小米: 3460, 苹果: 2028},
        {时间: '2021-10', 华为: 1000, 小米: 5713, 苹果: 7791}

    ],
    lineColors:['#07ade1','#e9ca00','#d90003'],
    xkey: '时间',
    ykeys: ['华为', '小米', '苹果'],
    labels: ['华为手机', '小米手机', '苹果手机'],
    pointSize: 0,
    lineWidth: 0,
    hideHover: 'auto'

});

//Morris数据弧线图数据
var day_data = [
    {"时间": "2021.01", "张三": 81,"李四": 62},
    {"时间": "2021.02", "张三": 92,"李四": 72},
    {"时间": "2021.03", "张三": 73,"李四": 82},
    {"时间": "2021.04", "张三": 64,"李四": 62},
    {"时间": "2021.05", "张三": 85,"李四": 52},
    {"时间": "2021.06", "张三": 76,"李四": 82},
    {"时间": "2021.07", "张三": 97,"李四": 92},
    {"时间": "2021.08", "张三": 58,"李四": 52},
    {"时间": "2021.09", "张三": 79,"李四": 82},
    {"时间": "2021.10", "张三": 100,"李四": 42},
    {"时间": "2021.11", "张三": 90,"李四": 82}
];
//Morris数据弧线图
Morris.Line({
    element: 'huxian-data-js',
    data: day_data,
    xkey: '时间',
    ykeys: ['张三','李四'],
    labels: ['张三成绩','李四成绩'],
    lineColors:['#1FB5AD','#b51300'],
    parseTime: false
});
//c3数据折线图 暂时只能单独只用
$(function () {
    var chart = c3.generate({

        bindto: '#zhexian-c3-js',

        data: {
            columns: [
                ['王五', 30, 200, 100, 400, 150, 250],
                ['赵六', 50, 20, 10, 40, 15, 25]
            ],
            types: {
                王五: 'line',
                赵六: 'line'
            }
        },

        axis: {
            x: {
                type: 'categorized'
            }
        }

    });



});

//Morris数据柱状图
Morris.Bar({
    element: 'zhuzhuang-data-js',
    data: [
        {x: '2020.08', y: 12520, z: 12454.78},
        {x: '2020.09', y: 1100, z: 1011.76},
        {x: '2020.10', y: 800, z: 737.49},
        {x: '2020.11', y: 1000, z: 884.05},
        {x: '2020.12', y: 800, z: 862.89},
        {x: '2021.02', y: 0, z: 127},
        {x: '2021.03', y: 1050, z: 861.18},
        {x: '2021.04', y: 3300, z: 2828.18},
        {x: '2021.05', y: 1050, z: 857},
        {x: '2021.06', y: 1000, z: 2533.34},
        {x: '2021.07', y: 800, z: 271.61}
    ],
    xkey: 'x',
    ykeys: ['y', 'z'],
    labels: ['收入￥', '支出￥'],
    barColors:['#6ed6ff','#d90003']
});