import React,{useState,useEffect} from 'react'
import axios from 'axios'
import {useParams} from 'react-router-dom'; // HTTP 경로 상의 매개변수 호출 해주는 함수

import ReplyList from './ReplyList'

export default function View(props) {

   const params = useParams();
   const [ board , setBoard ] = useState( {
        replyDtoList : []
   } );

   // 1. 현재 게시물 가져오는 ajax 함수
   const getBoard = ()=>{
        axios.get("/board/getboard" , { params : { bno : params.bno }})
            .then( (r) => {
                console.log( r.data );
                setBoard( r.data );
            })
   }
    // 2. 컴포넌트 처음 열렸을때
    useEffect(()=>{getBoard();},[]);

    // 3. 게시물 삭제 함수
     const onDelete = () =>{
           axios.delete("/board" , { params : { bno : params.bno }})
               .then( r => {
                   console.log( r.data );
                   if( r.data == true){
                       alert('삭제 성공 ');
                       window.location.href="/board/list";
                   }else{ alert('삭제 실패')}
               })
      }

   // 3. 수정 페이지 이동 함수
   const onUpdate = () => { window.location.href="/board/update?bno="+board.bno }

   const [ login , setLogin ] = useState( JSON.parse( sessionStorage.getItem('login_token') ) )

   // 4. 댓글 작성시 렌더링
       const onReplyWrite = (rcontent) =>{
           let info ={ rcontent : rcontent , bno : board.bno };
           console.log(info);
           axios.post("/board/reply" , info)
               .then( (r)=>{
                   if(r.data == true){
                       alert("글쓰기 완료"); getBoard();
                   }else{
                       alert('로그인 후 가능 합니다.')
                   }
               });
       }
   // 5. 댓글 삭제 렌더링
   const onReplyDelete = (rno) =>{
       console.log(rno);
       axios.delete("/board/reply" , {params : {"rno":rno}})
           .then( r=>{
                if(r.data==true){
                    alert("댓글 삭제"); getBoard();
                }else{alert('본인 댓글만 삭제 쌉가능');}
           })
   }

   // 6. 댓글 수정 렌더링
   const onReplyUpdate = (uprContent , rno) =>{
        let info = {rcontent : uprContent , rno : rno}
        console.log(info);
        axios.put("/board/reply" , info)
            .then( r=>{
                console.log(r.data)
                if(r.data==true){
                    alert('수정 완료'); getBoard();
                }else{alert('본인 댓글만 수정 쌉가능')}
            })
   }

   // 1. 현재 로그인된 회원이 들어왔으면
   const btnBox =
                login != null && login.mno == board.mno
                ? <div> <button onClick={ onDelete }>삭제</button>
                        <button onClick={onUpdate}>수정</button> </div>
                : <div> </div>

   return ( <>
        <div>
            <h3> { board.btitle } </h3> <h3> {board.bcontent} </h3> { btnBox }
        </div>
        <ReplyList
        onReplyDelete={onReplyDelete}
        onReplyWrite={onReplyWrite}
        onReplyUpdate={onReplyUpdate}
        replyList = { board.replyDtoList }  />
   </>)
}
/*
        // useParams() 훅 : 경로[URL] 상의 매개변수[객체] 반환
        // http://localhost:8080/board/view/46
        // http://localhost:8080/board/view/:bno    -----> useParams(); ----> {bno:46}

        // http://localhost:8080/board/view/46/안녕하세요
        // http://localhost:8080/board/view/:bno/:comment   -----> useParams() ----> { bno:46 , comment : 안녕하세요 }
*/
