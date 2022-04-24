
function getRepreObj()
{
    let apiURL = "/API/getRepresentObj";
    let repreObj = "";
    $.ajax({
        type : "GET",
        url : apiURL,
        async : false
    }).done(function (result){
        repreObj = result;
    });
    return repreObj;
}


function getSelectedObjIdxList()
{
    let classInst = checkBoxClass + ".objDelete";
    let idxList = [];
    $(".userObjTable").find('input:checked').each(function (){
        let idStr = this.id + "";
        idxList.push(idStr.split("-")[1]);
    });

    return idxList;
}

function updateUserObjAjax(input, isDelete)
{
    let apiURL = "/API/setRepresentObj";
    if(isDelete)
    {
        apiURL = "/API/delObjs";
        input =
        {
            checkedList : input
        }
    }
    else
    {
        input =
        {
            objIdx : input
        };
    }
    $.ajax({
        type : "POST",
        url : apiURL,
        data : input,
        async : false
    }).done(function (result){
        if (result === "fail")
        {
            alert("요청을 수행하는데 실패했습니다");
        }
        else
        {
            location.reload();
        }
    });

}

//delete or set userobj represent by ajax
function updateUserObjInfo(input)
{
    let isDelete = input === "delete";
    if (isDelete)
    {
        let selectedItem = getSelectedObjIdxList();
        updateUserObjAjax(selectedItem,isDelete);
    }
    else
    {
        updateUserObjAjax(input,isDelete);
    }

}

