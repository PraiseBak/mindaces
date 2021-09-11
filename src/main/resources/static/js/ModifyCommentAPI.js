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

//제출할때 댓글 내용 확인
function checkCommentValid()
{
    var user = document.getElementById("arthorName").value;
    var content = document.getElementById("commentContent").value;
    var password = document.getElementById("password").value;


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

function  setCommentSubmitMode(mode)
{
    this.commentSubmitMode = mode;
}

//수정,삭제시에 비밀번호 입력하는 창 보이게하는 유무
function toggleCommentPasswordInputPassword(aTag)
{
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

//비밀번호 입력후 확인
//삭제인 경우라면 비밀번호 입력 후 서버에 요청보냄(물론 서버에서 한번 더 유효한가 체크함)
function checkPasswordAndSubmitNotLogged(childTag, commentIdx)
{
    var formTag =
        getParentTag(
        getParentTag(
        getParentTag(childTag)));
    var inputArea = formTag.parentNode.querySelector("#inputCommentPassword");
    var commentDto =
    {
        commentIdx : commentIdx,
        commentPassword : inputArea.value
    };

    //수정창 열어놓고 또 다른창 누르는 경우 이전의 창을 닫아주는 역할
    if(prevCommentsTag != null)
    {
        modifyInputToggle(prevCommentsTag);
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
                formSubmit(formTag)
            }
            else
            {
                toggleCommentPasswordInputPassword(childTag,commentSubmitMode);
                modifyInputToggle(formTag.parentNode.parentNode);
            }
        }
    });
}

function formSubmit(formTag)
{
    var url = getCommentSubmitURL(formTag.action);
    formTag.action = url;
    formTag.submit();
}

//댓글 수정시에 비밀번호가 일치하여 입력창 보여주고
function modifyInputToggle(commentsTag)
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

function getCommentSubmitURL(url)
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
    var formTag =
        getParentTag(
        getParentTag(
        getParentTag(btnTag)));
    formSubmit(formTag);
}

function isUser()
{
    return isRoleUserVal;
}

function getParentTag(childTag)
{
    return childTag.parentNode;
}

function checkCommentUserAJAX(commentIdx,childTag)
{
    var formTag =
        getParentTag(
        getParentTag(
        getParentTag(childTag)));

    var commentIdxRequest =
    {
        commentIdx : commentIdx
    };

    commentUserCheckAndSubmitAJAX(formTag,commentIdxRequest);
}


function checkCommentUserAndInputToggle(aTag, mode, commentIdx)
{
    setCommentSubmitMode(mode);
    if(isUser() === "true")
    {
        checkCommentUserAJAX(commentIdx,aTag);
    }
    else
    {
        toggleCommentPasswordInputPassword(aTag);
    }
}

function commentUserCheckAndSubmitAJAX(formTag,commentIdxRequest)
{
    $.ajax({
        data : commentIdxRequest,
        url : "/API/checkCommentUserAPI",
        type : "post",
        async : false

    }).done(function (result){

        if(result == true)
        {
            if(commentSubmitMode == "deleteComment")
            {
                formSubmit(formTag)
            }
            else
            {
                modifyInputToggle(formTag.parentNode.parentNode);
            }
        }
        else
        {
            alert("권한이 없습니다");
        }

    });

}






