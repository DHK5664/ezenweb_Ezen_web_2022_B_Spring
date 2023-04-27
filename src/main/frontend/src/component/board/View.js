import React,{useState,useEffect} from 'react'
import axios from 'axios'
import {useParams} from 'react-router-dom'; // HTTP 경로 상의 매개변수 호출 해주는 함수

import ReplyList from './ReplyList'

export default function View(props) {
   const params = useParams();

   const [ board , setBoard ] = useState( {} );
   useEffect( ()=>{
        axios.get("/board/getboard" , { params : { bno : params.bno }})
            .then( (r) => {
                console.log( r.data );
                setBoard( r.data );

            })
   } , [] ) // setBoard() 할때마다 실행되는 useEffect

    // 삭제 함수
     const onDelete = () =>{
           axios.delete("/board" , { params : { bno : params.bno }})
               .then( r => {
                   console.log( r.data );
                   if( r.data == true ){
                       alert('삭제 성공 ');
                       window.location.href="/board/list";
                   }else{ alert('삭제 실패')}
               })
      }

   // 수정 페이지 이동 함수
   const onUpdate = () => { window.location.href="/board/update?bno="+board.bno }

   const [ login , setLogin ] = useState( JSON.parse( sessionStorage.getItem('login_token') ) )

   // 2. 댓글 작성시 렌더링
       const onReplyWrite = (rcontent) =>{
           let info ={ rcontent : rcontent , bno : board.bno };
           console.log(info);
           axios.post("/board/reply" , info)
               .then( (r)=>{
                   if(r.data == true){
                       alert("글쓰기 완료")
                       setBoard( r.data );
                        console.log( board  );
                        console.log( board.replyDtoList  );
                   }else{
                       alert('로그인 후 가능 합니다.')
                   }
               });
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
        <ReplyList onReplyWrite={onReplyWrite}  replyDtoList = { board.replyDtoList }  />
   </>)
}
/*
        // useParams() 훅 : 경로[URL] 상의 매개변수[객체] 반환
        // http://localhost:8080/board/view/46
        // http://localhost:8080/board/view/:bno    -----> useParams(); ----> {bno:46}

        // http://localhost:8080/board/view/46/안녕하세요
        // http://localhost:8080/board/view/:bno/:comment   -----> useParams() ----> { bno:46 , comment : 안녕하세요 }
*/
