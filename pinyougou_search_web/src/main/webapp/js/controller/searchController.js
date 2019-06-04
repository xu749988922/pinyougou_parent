window.onload=function () {
    var app = new Vue({
        el:"#app",
        data:{
            //查询结果集
            resultMap:{},
            //搜索条件集{keyword:关键字}
            searchMap:{keyword:'',category:'',brand:'',spec:{},price:'',pageNo:1,pageSize:20},
            searchWord:'',
            //pageLable:[分页标签列表]
            pageLable:[],
            //标识分页插件中是否显示前面的省略号
            firstDot:true,
            //标识分页插件中是否显示后面的省略号
            lastDot:true
        },
        methods:{
            //搜索数据
            searchList:function () {
                axios.post("/item/search.do",this.searchMap).then(function (response) {
                    app.resultMap = response.data;
                    //刷新分页标签
                    app.buildPageLabel();
                })
            },
            addSearchItem:function (key,value) {
                if (key == "category" || key =="brand"  || key == 'price'){
                    app.$set(this.searchMap,key,value);
                }else{
                    //app.searchMap.spec[key]=value;注意，这里不能用这种方式
                    app.$set(app.searchMap.spec,key,value);
                }
                this.searchList();
            },
            //前端不要随便删除,最好把不要的变成''
            removeSearchItem:function (key) {
                if (key == "category" || key =="brand" || key == 'price'){
                    // app.$delete(this.searchMap,key);
                    app.$set(this.searchMap,key,"");
                }else{//否则是规格
                    // app.$delete(app.searchMap.spec, key);//移除此属性
                    app.$delete(app.searchMap.spec, key);//移除此属性
                }

                this.searchList();
            },
            textSearch:function () {
                this.searchMap={keyword:'',category:'',brand:'',spec:{},price:'',pageNo:1,pageSize:20};
                // app.$set(this.searchMap,"keyword",this.searchWord);
                this.searchMap.keyword =this.searchWord;
                this.searchList();
            },
            //构建分页标签
            buildPageLabel:function () {
                //查询完数据后，从新计算分页标签
                this.pageLable=[];
                var firstPage = 1; //开始页码
                var lastPage = this.resultMap.totalPages;  //截止页码
                this.firstDot=true;//前面有点
                this.lastDot=true;//后边有点
                //如果总页数 > 5
                if(this.resultMap.totalPages > 5){
                    //如果当前页码 <= 3，显示前5页
                    if(this.searchMap.pageNo <= 3){
                        lastPage = 5;
                        this.firstDot=false;//前面没点
                        //如果当前页码 >= (总页数-2)，显示后5页
                    }else if(this.searchMap.pageNo >= (this.resultMap.totalPages - 2)){
                        firstPage = this.resultMap.totalPages - 4;
                        this.lastDot=false;//后边没点
                    }else{
                        //显示当前页为中心的5个页码
                        firstPage=this.searchMap.pageNo-2;
                        lastPage=this.searchMap.pageNo+2;
                    }
                }else{
                    this.firstDot=false;//前面没点
                    this.lastDot=false;//后边没点
                }
                for(var i = firstPage; i <= lastPage; i++){
                    this.pageLable.push(i);
                }
            },
            /**
             * 页面跳转-点击事件
             * @param pageNo 当前要跳转的页数
             */
            queryByPage:function (pageNo) {
                //先把参数转换为Int
                pageNo = parseInt(pageNo);
                if(pageNo < 1 || pageNo > this.resultMap.totalPages){
                    alert("请输入正确的页码！");
                    return;
                }
                //修改当前页
                this.searchMap.pageNo = pageNo;
                //刷新数据
                this.searchList();
            }
        },
        //初始化调用
        created:function () {
            this.searchList();
        }
    });
}
