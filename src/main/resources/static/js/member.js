
function onSignup(){

    let info = {
        memail : document.querySelector(".memail").value,
        mpassword : document.querySelector(".mpassword").value,
        mname : document.querySelector(".mname").value,
        mphone : document.querySelector(".mphone").value
    }

    $.ajax({
        url:"/member/info",
        method : "post",
        data : JSON.stringify(info),
        contentType : "application/json",
        success : (r)=>{
            console.log(r);
            if(r==true){alert("가입이 되셨습니다."); }
        }
    })
}
/*
    // ---------------------- 시큐리티 적용 될 경우 아래코드 사용 안함 ---------------------- //
function onLogin(){
    let info = {
        memail : document.querySelector(".memail").value,
        mpassword : document.querySelector(".mpassword").value
    }

    $.ajax({
        url:"/member/login",
        method : "post",
        data : JSON.stringify(info),
        contentType : "application/json",
        success : (r)=>{
            console.log(r);
            if(r==true){alert("로그인이 되셨습니다.");
             location.href = "/"; }
        }
    })
}
*/
getMember();
function getMember(){

    $.ajax({
        url : "/member/info",
        method : "get",
        success : (r)=>{
            document.querySelector('.infobox').innerHTML = `${r.mname}님`
            document.querySelector('.infobox').innerHTML +=
                `<a href="/member/logout"><button>로그아웃</button></a>`
        }
    })
}
/*
function getLogout(){
    $.ajax({
        url : "/member/logout",
        method : "get",
        success : (r)=>{
            location.href="/";
        }
    })
}
*/
function getMemail(){
    $.ajax({
        url:"/member/findemail",
        method : "get",
        data:{"mname" : document.querySelector(".mname").value,
        "mphone" : document.querySelector(".mphone").value},
        success : (r)=>{
            console.log(r);
            alert("찾으신 이메일은 :"+" "+r+" "+"입니다.")
        }
    })
}

function getPassword(){
    $.ajax({
        url:"/member/findpw",
        method : "get",
        data:{"memail" : document.querySelector(".memail").value,
        "mphone" : document.querySelector(".mphone").value},
        success:(r)=>{
            console.log(r);
        }
    })
}