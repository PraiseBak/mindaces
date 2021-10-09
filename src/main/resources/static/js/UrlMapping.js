function setSearchFormActionByMode(formID,mode)
{
    var form = document.getElementById(formID);
    var url = form.action;
    var urlArr = url.split("search/");
    var modeStartWithMode = urlArr[1];
    form.action =urlArr[0] + 'search/' + mode + '/' + modeStartWithMode.split("/")[1];
    form.submit();


}