//页面初始化完成后再创建Vue对象
	//创建Vue对象
	var app = new Vue({
		//接管id为app的区域
		el:"#app",
		data:{
			//声明数据列表变量，供v-for使用
			list:[],
			//总页数
			pages:1,
			//当前页
			pageNo:1,
			//声明对象specificationItems[]商品规格列表
			//itemList:[商品sku列表]
			entity:{tbGoods:{typeTemplateId:0},
				tbGoodsDesc:{itemImages:[],customAttributeItems:[],specificationItems:[]},
				itemList:[]},
			//将要删除的id列表
			ids:[],
			//搜索包装对象
			searchEntity:{},
			//图片上传成功后保存的对象
			image_entity:{url:''},
			itemCatList1:[],
			itemCatList2:[],
			itemCatList3:[],
			//品牌列表
			brandIds:[],
			//[]规格列表
			specIds:[],
			status:["未审核","审核通过","审核未通过","下架"],
			//商品分类对象
			itemCatMap: {"1":"手机"},
			mStatus:["已上架","已下架"]

		},
		methods:{
			//查询所有
			findAll:function () {
				axios.get("../goods/findAll.do").then(function (response) {
					//vue把数据列表包装在data属性中
					app.list = response.data;
				}).catch(function (err) {
					console.log(err);
				});
			},
			//分页查询
			findPage:function (pageNo) {
				axios.post("../goods/findPage.do?pageNo="+pageNo+"&pageSize="+10,this.searchEntity)
					.then(function (response) {
						app.pages = response.data.pages;  //总页数
						app.list = response.data.rows;  //数据列表
						app.pageNo = pageNo;  //更新当前页
					});
			},
			//分页查询把分类ID转换成分类的名称
			findItemCatName:function(){
				axios.get("/itemCat/findAll.do?").then(function (resp) {
					var tbItemCat=resp.data;
					for (i=0;i<tbItemCat.length;i++){
						// app.category.push(tbItemCat[i])
						//$set(操作的变量名,修改的属性,修改的值)
						app.$set(app.itemCatMap,tbItemCat[i].id,tbItemCat[i].name);
					}
				})
			},
			//让分页插件跳转到指定页
			goPage:function (page) {
				app.$children[0].goPage(page);
			},
			//新增
			add:function () {
				this.entity.tbGoodsDesc.introduction =editor.html();
				var url = "../goods/add.do";
				if(this.entity.tbGoods.id != null){
					url = "../goods/update.do";
				}
				axios.post(url, this.entity).then(function (response) {
					alert(response.data.message);
					if (response.data.success) {
						app.entity={tbGoods:{typeTemplateId:0},
							tbGoodsDesc:{itemImages:[],customAttributeItems:[],specificationItems:[]},
							itemList:[]}
						editor.html("");
					}
					window.location.href="goods.html"
				});
			},
			//跟据id查询
			getById:function (id) {
				//获取url上的id
				id = this.getUrlParam()["id"];
				axios.get("/goods/getById.do?id="+id).then(function (response) {
					app.entity = response.data;
					editor.html(app.entity.tbGoodsDesc.introduction);
					app.entity.tbGoodsDesc.itemImages=JSON.parse(app.entity.tbGoodsDesc.itemImages);
					app.entity.tbGoodsDesc.customAttributeItems=JSON.parse(app.entity.tbGoodsDesc.customAttributeItems);
					app.entity.tbGoodsDesc.specificationItems=JSON.parse(app.entity.tbGoodsDesc.specificationItems);
					for (var i =0;i<app.entity.itemList.length;i++){
						app.entity.itemList[i].spec=JSON.parse(app.entity.itemList[i].spec)
					}

				})
			},
			//规格回显判断
			checkAttributeValue : function (specName,optionName){
				var item =this.entity.tbGoodsDesc.specificationItems;
				var obj=this.searchObjectByKey(item,"attributeName",specName);
				if (obj != null){
					if (obj.attributeValue.indexOf(optionName)>-1) {
						return true;
					}
				} 
				return false;
			},
			//批量删除数据
			dele:function () {
				axios.get("../goods/delete.do?ids="+this.ids).then(function (response) {
					if(response.data.success){
						//刷新数据
						app.findPage(app.pageNo);
						//清空勾选的ids
						app.ids = [];
					}else{
						alert(response.data.message);
					}
				})
			},
			//文件上传
			upload:function(){
				//创建<form>表单对象
				var formData = new FormData() // 声明一个FormData对象
				// 'file' 这个名字要和后台获取文件的名字一样-表单的name属性
				//<input name="file" type="file" id="file"/>
				formData.append('file', document.querySelector('input[type=file]').files[0]);

				//post提交
				axios({
					url: '/upload.do',
					data: formData,
					method: 'post',
					headers: {
						'Content-Type': 'multipart/form-data'
					}
				}).then(function (response) {
					if(response.data.success){
						//上传成功
						app.image_entity.url=response.data.message;
					}else{
						//上传失败
						alert(response.data.message);
					}
				})
			},
            add_image_entity:function () {
                this.entity.tbGoodsDesc.itemImages.push(this.image_entity);
            },
            deleImage:function (index) {
                this.entity.tbGoodsDesc.itemImages.splice(index,1);
            },
			findItemCatByParentId:function (itemCatParentId,itemCatListId) {
				axios.get("/itemCat/findByParentId.do?parentId="+itemCatParentId).then(function (resp) {
					app["itemCatList"+itemCatListId] =resp.data
					if (itemCatListId<=3){
						app.entity.tbGoods.typeTemplateId={}
					}
					if (itemCatListId<=2){
						app.itemCatList3 = [];
					}
				})
			},
			//商品的上下架
			isMarketableStatus:function(id){
				axios.get("/goods/isMarketableStatus.do?id="+id).then(function (resp) {
					alert(resp.data.message);
					window.location.href="goods.html"
				})
			},
			//商品录入的规格选择
			searchObjectByKey:function(list,key,attributeName){
				for(var i =0;i<list.length;i++){
					if (list[i][key]==attributeName){
						return list[i];
					}
				}
				return null;
			},
			updateSpecAttribute:function (event,attributeName,attributeValue) {
				// var obj = this.searchObjectByKey(this.entitiy.tbGoodsDesc.specificationItems,'attributeName',attributeName)
				var obj = this.searchObjectByKey(this.entity.tbGoodsDesc.specificationItems,'attributeName',attributeName);
				//如果需要的规格不存在，那么就追加一个
				if (obj == null){
					this.entity.tbGoodsDesc.specificationItems.push({
						"attributeName":attributeName,
						"attributeValue":[
							attributeValue
						]
					})
				}else {
					//如果点击后是添加,在确定的对象后面追加attributeValue
					if (event.target.checked){
						obj.attributeValue.push(attributeValue)
					}else {
						//如果点击后是取消,先去掉当前的属性
						var attrIndex = obj.attributeValue.indexOf(attributeValue)
						obj.attributeValue.splice(attrIndex,1)
						//再判断剩下的是否为空,如果是,删除
						if (obj.attributeValue.length==0){
							var objIndex = this.entity.tbGoodsDesc.specificationItems.indexOf(obj)
							this.entity.tbGoodsDesc.specificationItems.splice(objIndex,1)
						}
					}
				}

			},
			createItemList:function () {
				//1.创建createItemList方法，同时创建一条有基本数据，不带规格的初始数据
				this.entity.itemList = [{spec: {}, price: 0, num: 99999, status: '0', isDefault: '0'}];
				var list = this.entity.tbGoodsDesc.specificationItems;
				//打开数据库中的specificationItems,开始循环,试着往里面加东西
				for(var i=0;i<list.length;i++){
					this.entity.itemList = this.addColumn(this.entity.itemList,list[i].attributeName,list[i].attributeValue)
				}
			},
			addColumn:function (itemList,attributeName,attributeValue) {
				var newList=[];
				for(var i=0;i<itemList.length;i++) {
					for (var j = 0; j < attributeValue.length; j++) {
						var addRow = JSON.parse(JSON.stringify(itemList[i]));
						// addRow.spec[attributeName] = attributeValue[j];
                        app.$set(addRow.spec,attributeName,attributeValue[j])
						newList.push(addRow);
					}
				}
				return newList;
			},/**
			 * 解析一个url中所有的参数
			 * @return {参数名:参数值}
			 */
			getUrlParam:function(){
				var paramMap={}
				var url=document.location.toString();
				var arrObj = url.split("?");
				if (arrObj.length>1){
					var argsArr = arrObj[1].split("&");
					for (var i=0;i<argsArr.length;i++){
						var lastArr = argsArr[i].split("=");
						if (lastArr !=null){
							// paramMap.put(lastArr[0],lastArr[1])
							paramMap[lastArr[0]]=lastArr[1];
						}
					}
				}
				return paramMap;
			}

		},
		watch:{
			"entity.tbGoods.category1Id":function (newValue, oldValue) {
				this.findItemCatByParentId(newValue,2);

			},
			"entity.tbGoods.category2Id":function (newValue, oldValue) {
				this.findItemCatByParentId(newValue,3);
			},
			"entity.tbGoods.category3Id":function (newValue, oldValue) {
				axios.get("/itemCat/getById.do?id="+newValue).then(function (resp) {
					app.entity.tbGoods.typeTemplateId =resp.data.typeId
				})
				// this.findBrandByTypetempalteId(35);
				// this.findBrandByTypetempalteId(this.entity.tbGoods.typeTemplateId);
			},
			"entity.tbGoods.typeTemplateId":function (newValue,oldValue) {
				if (newValue !=null){
					//查询模板信息
					axios.get("/typeTemplate/getById.do?id="+newValue).then(function (response) {
						app.brandIds = JSON.parse(response.data.brandIds);
						//回显与添加冲突,一个是typeTemplate里面的customAttributeItems,一个是tbgoodsdesc里面的
						var id= app.getUrlParam()["id"];
						if (id == null){
							app.entity.tbGoodsDesc.customAttributeItems = JSON.parse(response.data.customAttributeItems);
						}

					});
					axios.get("/typeTemplate/findSpecList.do?id="+newValue).then(function (response) {
						app.specIds = response.data;
					})
				}
			}
		},
		//Vue对象初始化后，调用此逻辑
		created:function () {
			//调用用分页查询，初始化时从第1页开始查询
			// this.findPage(1);
			this.findItemCatByParentId(0,1);
			this.findPage(1);
			this.findItemCatName();
			// this.getUrlParam();
			this.getById(1);
		}
	});
