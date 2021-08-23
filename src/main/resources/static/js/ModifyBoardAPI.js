
var modifyBtn = document.getElementById("modifyBtn");
var deleteBtn = document.getElementById("deleteBtn");

function getPassword(contentIdx)
{
    url = redirectURL;
    var inputPassword = document.getElementById("inputPassword").value;
    var hiddenPassword = document.getElementById("password");
    hiddenPassword.setAttribute("value",inputPassword);
    checkBoardPasswordAjax(inputPassword,contentIdx,url);
}

function checkBoardPasswordAjax(inputPassword,contentIdx,url)
{
    var form = document.getElementById("hiddenPasswordForm");
    var boardDto =
    {
        contentIdx: contentIdx,
        password: inputPassword
    };

    $.ajax({
        url: "/API/checkBoardPasswordAPI",
        data: boardDto,
        type: "POST",
        async:false
    }).done(function (result){
        if(result === true)
        {
            form.setAttribute("action",url);
            form.submit();
        }
        else
        {
            alert("비밀번호가 일치하지 않습니다");
        }
    });
}

function checkUserAjax(contentIdx,url)
{
    alert(isRoleUserVal);
    var boardDto =
    {
        contentIdx: contentIdx
    };

    $.ajax({
        url: "/API/checkUserAPI",
        data: boardDto,
        type: "POST",
        async:false
    }).done(function (result) {
        if(result === true)
        {
            form.setAttribute("action",url);
            form.submit();
        }
        else
        {
            alert("삭제할 권한이 없습니다");
        }
    });
}



function foldPasswordInput(url)
{
    alert(isRoleUserVal);

    if(url != "")
    {
        redirectURL = url;
    }
    var inputArea = document.getElementById("inputPasswordArea");
    if(inputArea.style.display === "none")
    {
        inputArea.style.display = "inline-block";
    }
    else
    {
        inputArea.style.display = "none";
    }

}



