window.onload=function () {
    var app = new Vue({
        el:"#app",
        data:{
            //查询结果集
            resultMap:{},
            //搜索条件集{keyword:关键字}
            searchMap:{keyword:''}
        },
        methods:{
            //搜索数据
            searchList:function () {
                axios.post("/item/search.do",this.searchMap).then(function (response) {
                    app.resultMap = response.data;
                })
            }
        },
        //初始化调用
        created:function () {
            this.searchList();
        }
    });
}
