
var modifyBtn = document.getElementById("modifyBtn");
var deleteBtn = document.getElementById("deleteBtn");

function getPassword(contentIdx, galleryName)
{
    url = redirectURL;
    var inputPassword = document.getElementById("inputPassword").value;
    var hiddenPassword = document.getElementById("password");
    hiddenPassword.setAttribute("value",inputPassword);
    checkBoardPasswordAjax(inputPassword,contentIdx,url,galleryName);
}

function checkBoardPasswordAjax(inputPassword,contentIdx,url,galleryName)
{
    var form = document.getElementById("hiddenPasswordForm");
    var boardDto =
    {
        contentIdx: contentIdx,
        password: inputPassword,
        gallery: galleryName

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

function checkUserAjax(contentIdx,url,galleryName)
{

    if(!isWriteLoggedBoard)
    {
        alert("권한이 없습니다");
        return;
    }

    var boardDto =
    {
        contentIdx: contentIdx,
        gallery: galleryName
    };
    var form = document.getElementById("hiddenPasswordForm");
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
            alert("권한이 없습니다");
        }
    });
}



function foldPasswordInput(url)
{
    if(isWriteLoggedBoard)
    {
        alert("권한이 없습니다");
    }
    else
    {
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


}



