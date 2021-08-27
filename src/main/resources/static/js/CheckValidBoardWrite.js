
function checkValidBoardWrite()
{
    var inputTitle = document.getElementById("inputTitle").value;
    var authorName = document.getElementById("authorName").value;
    var password = document.getElementById("password").value;
    var content = document.getElementById("inputContent").value;
    var flag = false;
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
        url:"/API/checkBoardValidAPI",
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

