
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
        async: false
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

function checkBoardUserAjax(contentIdx, url, galleryName)
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
        url: "/API/checkBoardUserAPI",
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



function foldBoardPasswordInput(url)
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

function sendBoardLikeRequest(mode,galleryName,boardIdx)
{
    var info =
    {
        gallery : galleryName,
        mode : mode,
        boardIdx : boardIdx
    };

    $.ajax({
        url : "/API/requestBoardRecommendAPI",
        data: info,
        type: "POST",
        async : false
    }).done(function (result){
        if(result.match("통과"))
        {
            refreshLikes();
        }
        else
        {
            alert(result);
        }
    });
}

function refreshLikes()
{
    var likesBtn = document.getElementById("likesBtn");
    var dislikesBtn = document.getElementById("dislikesBtn");
    var info=
    {
        gallery : galleryName,
        boardIdx : boardIdx
    };

    $.ajax({
        url : "/API/getRenewBoardLikesAPI",
        data : info,
        type : "POST",
        dataType : "json",
        async : false
    }).done(function (result)
    {
        likesBtn.innerText = "개추 " + result['likes'];
        dislikesBtn.innerText = "비추 " + result['dislikes']
    });
}
