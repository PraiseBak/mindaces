
function getRandWise()
{
    let url = "/API/getRandWise";
    let wise = "";
    $.ajax({
        url : url,
        type : "POST",
        async : false
    }).done(function (result){
        wise = result;
    });
    return wise;
}
