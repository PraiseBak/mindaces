window.onload=function(){
    var title = $(document).find("title");
    var url = window.location.href.split('?');
    var params = '?' + url[1];
    var url = url[0];

    if(url.charAt(url.length-1) == '/')
    {
        url = url.slice(0,-1);
        history.pushState(null,title,url + params);

    }

}