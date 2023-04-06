console.log("아이템 js 열림")

// 1. 등록
function onwrite(){
    console.log("등록버튼 눌림");

    $.ajax({
        url:"/item/write",
        method:"post",
        data:JSON.stringify({"pname" : document.querySelector(".pname").value
                     , "pcontent" : document.querySelector(".pcontent").value}),
        contentType:"application/json",
        success:(r)=>{
            console.log(r)
            if(r==true){
                alert("등록 성공"); onget();
                document.querySelector(".pname").value="";
                document.querySelector(".pcontent").value="";
            }
            else{alert("등록 실패");}
        }
    })
}// function end

// 2. 호출
function onget(){
    $.ajax({
        url:"/item/get",
        method:"get",
        success:(r)=>{
            console.log(r)
            let html=`<tr>
                        <th>제품번호</th>   <th>제품명</th>   <th>제품내용</th>   <th>비고</th>
                    </tr>
                    `;
            r.forEach((p)=>{
                html +=`
                        <tr>
                            <th>${p.pno}</th>   <th>${p.pname}</th> <th>${p.pcontent}</th>
                            <th>
                                <button type="button" onclick="ondelete(${p.pno})" >삭제</button>
                                <button type="button" onclick="onupdate(${p.pno})">수정</button>
                            </th>
                        </tr>
                        `
            })
            document.querySelector(".itemTable").innerHTML=html;
        }
    })
}// end

//  3. 삭제
function ondelete(pno){
    $.ajax({
        url:"/item/delete",
        method:"delete",
        data:{"pno" : pno},
        success:(r)=>{
            console.log(r)
            if(r==true){
            alert("삭제 성공"); onget();}
            else{alert("삭제 실패")}
        }
    })
}// end

// 4. 삭제
function onupdate(pno){
    let pname=prompt("수정할 제품명"); console.log(pname);
    let pcontent = prompt("수정할 내용"); console.log(pcontent);
    $.ajax({
    url:"/item/update",
    method:"put",
    data:JSON.stringify({"pno" : pno, "pname" : pname , "pcontent" : pcontent}),
    contentType:"application/json",
    success:(r)=>{
        console.log(r)
        if(r==true){
        alert("수정 성공"); onget();}
        else{alert("수정실패...ㅠㅠ")}
        }
    })
}// end





















