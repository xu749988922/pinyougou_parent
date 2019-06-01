window.onload=function (){
    var app = new Vue({
        el:"#app",
        data:{
            entity:{},
            //所有的广告对象列表
            contentList:[]
        },
        methods:{
            //查询所有的广告
            findByCategoryId:function () {
                //查询轮播图广告
                axios.get("/content/findByCategoryId.do?categoryId=1").then(function (resp) {
                    // app.contentList[1]=resp.data
                    app.$set(app.contentList,1,resp.data)
                })
            }
        },
        created:function () {
            this.findByCategoryId();
        }
    })
}