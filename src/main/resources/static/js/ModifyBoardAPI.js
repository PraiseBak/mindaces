
var modifyBtn = document.getElementById("modifyBtn");
var deleteBtn = document.getElementById("deleteBtn");

function getPassword(contentIdx,url)
{
    var inputPassword = prompt("비밀번호를 입력해주세요");
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
            alert("비밀번호가 일치하지 않습니다.");
        }
    });
}

