
function checkValidBoardWrite(content)
{
    var inputTitle = document.getElementById("inputTitle").value;
    var authorName = document.getElementById("authorName").value;
    var password = document.getElementById("password").value;
    var isModify = document.getElementById("tx_editor_form_writeForm").action.includes("postModify/");
    var flag = false;
    var ajaxURL = "/API/checkBoardValidWriteAPI";
    if(isModify)
    {
        password = "";
    }
    else
    {
        if(password.length == 0)
        {
            alert("비밀번호가 공백입니다");
            return flag;
        }

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

