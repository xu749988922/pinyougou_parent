window.onload = function () {
    var app = new Vue({
        el: "#app",
        data: {
            list: [],
            pages: 0,
            pageNo: 1,
            entity:{},
            ids:[],
            searchEntity:{}
        },
        methods: {
            findAll: function () {
                axios.get("/brand/findAll.do").then(function (respsonse) {
                    app.list = respsonse.data
                }).catch(function (reason) {
                    alert("错误")
                })
            },
            findPage: function (pageNo) {
                axios.post("/brand/findPage.do?pageNo="+pageNo+"&pageSize=10",this.searchEntity).then(function (respsonse) {
                        app.list = respsonse.data.rows;
                        app.pages = respsonse.data.pages;
                        app.pageNo = pageNo;
                }).catch(function (reason) {
                    alert("分页查询错误")
                })
            },
            // //分页查询
            // findPage:function (pageNo) {
            //     axios.post("../brand/findPage.do?pageNo="+pageNo+"&pageSize="+10,this.searchEntity)
            //         .then(function (response) {
            //             app.pages = response.data.pages;  //总页数
            //             app.list = response.data.rows;  //数据列表
            //             app.pageNo = pageNo;  //更新当前页
            //         });
            // },

            add:function (){
                var url =null;
                if (this.entity.id !=null){
                    url="/brand/update.do"
                } else {
                    url="/brand/add.do"
                }
                axios.post(url,this.entity).then(function (response) {
                    if (response.data.flag){
                        app.findPage(app.pageNo)
                    } else {
                        alert(response.data.message)
                    }
                }).catch(function (reason) {
                    alert(reason)
                })
            },
            findById:function (id) {
                axios.get("/brand/findById.do?id="+id).then(function (response) {
                    app.entity =response.data;
                }).catch(function (reason) {
                    alert(reason)
                })
            },
            dele:function () {
                axios.get("/brand/dele.do?ids="+app.ids).then(function (response) {
                    if (response.data.flag){
                        app.findPage(app.pageNo);
                        app.ids =[]
                    } else {
                        alert(response.data.message)
                    }
                })
            },
            goPage:function (page) {
                app.$children[0].goPage(page);
            }
        },
        created:function () {
            this.findPage(1)
            // this.findAll()
        }
    })
}