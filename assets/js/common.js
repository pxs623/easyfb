
function forward(url,title,options)
{	
    var obj=new Object();
    obj.url=("undefined" == typeof url)?"":url;
    obj.title=("undefined" == typeof title)?"":title;
    obj.options=("undefined" == typeof title)?"":options;
    android.action("forward:"+JSON.stringify(obj));
}

function backward(url,needReload,isBackFlag)
{
    var obj=new Object();
    obj.url=("undefined" == typeof url)?"":url;
    obj.needReload=("undefined" == typeof needReload)?false:needReload;
    obj.isBackFlag=("undefined" == typeof isBackFlag)?false:isBackFlag;
    android.action("back:"+JSON.stringify(obj));
}

function toast(data)
{
    android.action("toast:"+data);
}

//设置全局请求时间不超过40秒
$.ajaxSetup({ timeout:40000});
function execute(obj) {
   $.ajax({
        url: tempurl + "?callback=?",
        async: true,
        type: "GET",
        data: { "requestMessage": encodeURI(JSON.stringify(obj)) },
        dataType: "jsonp",
        success: function (data) {
        
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
        	toast("toast:"+textStatus);
        },
    });
}
