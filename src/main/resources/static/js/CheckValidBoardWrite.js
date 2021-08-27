
function checkValidBoardWrite()
{
    var inputTitle = document.getElementById("inputTitle").value;
    var authorName = document.getElementById("authorName").value;
    var password = document.getElementById("password").value;
    var content = document.getElementById("inputContent").value;
    var isModify = document.getElementById("writeForm").action.includes("postModify/");
    var flag = false;
    var ajaxURL = "/API/checkBoardValidWriteAPI";

    if(isModify)
    {
        password = "";
    }
    var board=
    {
        title : inputTitle,
        user : authorName,
        password : password,
        content : content
    };

    $.ajax({
        type:"POST",
        data:board,
        url:"/API/checkBoardValidWriteAPI",
        async:false
    }).done(function (result) {
        if(result === "통과")
        {
            flag = true;
        }
        else
        {
            alert(result);
        }
    });
    return flag;
}

