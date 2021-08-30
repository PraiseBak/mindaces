
window.onload = function ()
{
    loadContent();
    var author = document.getElementById("authorName");
    var authorVal = author.getAttribute("value");
    var password = document.getElementById("password");

    if(authorVal != "")
    {
        author.readOnly = true;
        password.readOnly = true;
    }
}
