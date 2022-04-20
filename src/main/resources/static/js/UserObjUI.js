let checkBoxClass = ".form-check-input";

function toggleCSS(mode)
{
    let classInst = mode;
    let anotherInst = (mode === "objDelete") ? "objRepresent" : "objDelete";
    $(checkBoxClass + "." + classInst).each(function (){
        if(this.style.display == "none")
        {
            $(this).css("display", "block");
        }else
        {
            $(this).css("display", "none");
        }
    });

    $(checkBoxClass + "." + anotherInst).each(function (){
        $(this).css("display", "none");
    });

}


