var commentSubmitMode = "modifyComment";
var curURL = window.location.pathname;
var prevInputTag = null;
var prevCheckTag = null;
var inputDisableUserTag = "<span id=\"commentModifyUser\"></span>";
var inputDisableContentTag = "<pre id=\"commentModifyContent\" class=\"comment-content form-control\"></textarea>";
var inputEnableContentTag = "<textarea minlength=\"2\" maxlength=\"180\" id=\"commentModifyContent\" name=\"content\" class=\"form-control\" rows=\"4\" cols=\"50\" style=\"resize: none;\" placeholder=\"코멘트\"></textarea>"
var inputEnableUserTag = "<input type=\"text\" name=\"user\" minlength=\"2\" maxlength=\"20\" class=\"form-control\" id=\"commentModifyUser\" >";
var originUserVal = null;
var originCommentVal = null;
var prevCommentsTag = null;


function checkCommentValid()
{
    var user = document.getElementById("commentUser").value;
    var content = document.getElementById("commentContent").value;
    var password = document.getElementById("commentPassword").value;


    var data=
    {
        user : user,
        content : content,
        commentPassword : password
    };

    $.ajax({
        url: "/API/checkCommentValidAPI",
        type : "POST",
        data : data,
        async : false
    }).done(function (result){
        if(result == false)
        {
            alert("유효하지 않은 댓글입니다");
            return false;
        }
        else
        {
            return true;
        }
    });

}

function showInput(aTag,mode)
{
    this.mode = mode;
    commentSubmitMode = mode;

    var inputArea = aTag.parentNode.querySelector("#inputCommentPassword");
    var checkBtn = aTag.parentNode.querySelector("#checkBtn");
    if(inputArea.style.display === "none")
    {
        if(prevInputTag != null)
        {
            prevInputTag.style.display = "none";
            prevCheckTag.style.display = "none";
        }
        prevInputTag = inputArea;
        prevCheckTag = checkBtn;
        inputArea.style.display = "block";
        checkBtn.style.display = "block";
    }
    else
    {
        inputArea.style.display = "none";
        checkBtn.style.display = "none";
    }
}


function checkAndSubmit(childTag,commentIdx)
{


    var formTag = childTag.parentNode;
    var inputArea = formTag.parentNode.querySelector("#inputCommentPassword");
    var commentInputArea = "";

    var commentDto =
    {
        commentIdx : commentIdx,
        commentPassword : inputArea.value
    };

    //수정창 열어놓고 또 다른창 누르는 경우 이전의 창을 닫아주는 역할
    if(prevCommentsTag != null)
    {
        changeInputEnable(prevCommentsTag);
    }


    $.ajax({
        url : "/API/checkCommentPasswordAPI",
        type : "POST",
        data : commentDto,
        async: false

    }).done(function (result){
        if(result == false)
        {
            alert("비밀번호가 일치하지 않습니다");
        }
        else
        {
            if(commentSubmitMode == "deleteComment")
            {
                var url = getCorrectURL(formTag.action);
                formTag.action = url;
                formTag.submit();
            }
            else
            {
                showInput(childTag,commentSubmitMode);
                changeInputEnable(formTag.parentNode.parentNode);
            }
        }
    });
}


function changeInputEnable(commentsTag)
{
    var userTag = commentsTag.querySelector("#commentModifyUser");
    var modifySubmitBtn = commentsTag.querySelector("#modifySubmitBtn");
    var userTagValue = userTag.innerText;

    if(userTagValue == "")
    {
        userTagValue = userTag.value;
    }
    var commentTag = commentsTag.querySelector("#commentModifyContent");
    var commentTagValue = commentTag.innerText;
    if(commentTagValue == "")
    {
        commentTagValue = commentTag.value;
    }
    //수정버튼을 누른 경우 백업했던 데이터를 정보출력란에 출력(수정하다가 제출안했으므로)
    //input을 안보이게
    if(originUserVal != null)
    {
        userTag.parentNode.innerHTML = inputDisableUserTag;
        commentsTag.querySelector("#commentModifyUser").innerHTML = originUserVal;

        commentTag.parentNode.innerHTML = inputDisableContentTag;
        commentsTag.querySelector("#commentModifyContent").innerText = originCommentVal;

        modifySubmitBtn.style.display = "none";
        originUserVal = null;
        originCommentVal = null;
        prevCommentsTag = null;
    }
    //수정버튼을 누르지 않은 경우 기존 데이터를 백업
    //input 보이게
    else
    {
        originUserVal = userTagValue;
        originCommentVal = commentTagValue;
        userTag.parentNode.innerHTML = inputEnableUserTag;
        commentsTag.querySelector("#commentModifyUser").value = userTagValue;
        modifySubmitBtn.style.display = "inline-block";

        commentTag.parentNode.innerHTML = inputEnableContentTag;
        commentsTag.querySelector("#commentModifyContent").value = commentTagValue;
        prevCommentsTag = commentsTag;
    }

}

function getCorrectURL(url)
{
    if(url.charAt(url.length-1) == '/')
    {
        url += commentSubmitMode;
    }
    else
    {
        url += "/" + commentSubmitMode;
    }
    return url;
}


function modifySubmit(btnTag)
{
    var formTag = btnTag.parentNode.parentNode.parentNode;
    var url = getCorrectURL(formTag.action);
    formTag.action = url;
    formTag.submit();
}








