import React,{useState,useEffect} from 'react'
import axios from 'axios'
export default function ReplyList(props) {
    // 1. 댓글 작성 핸들러
    const onWriteHandler = () =>{
        props.onReplyWrite(document.querySelector('.rcontent').value);
        document.querySelector('.rcontent').value='';
    }
    // 2. 삭제 핸들러
    const onDeleteHandler = (e,rno) =>{
        console.log('삭제' + rno);
        props.onReplyDelete(rno); // props 전달받은 삭제함수 실행
    }

    // 번외 수정 칸 보이기
    function onUpdateBox(e,rno){
        document.querySelector('.rUpdateBox'+rno).style.display='block';
    }

    // 3. 수정 핸들러
    const onReplyUpdate = (e,rno) =>{
        console.log('수정'+rno);
        props.onReplyUpdate(document.querySelector('.uprContent'+rno).value,rno);
        document.querySelector('.uprContent'+rno).value='';
        document.querySelector('.rUpdateBox'+rno).style.display='none';
    }

    return (<>
        <input className="rcontent" type="text"/> <button onClick={onWriteHandler}> 댓글작성 </button>
        <h6> 댓글 목록 </h6>
        {
            props.replyList.map((r)=>{
                return (<div>
                            <span>{r.rcontent}</span>
                            <span>{r.rdate}</span>
                            <div className={ 'rUpdateBox'+(r.rno ) } style={{display:'none'}}>
                                <input className={ 'uprContent'+(r.rno ) } type="text"/>
                                <button onClick={(e)=>onReplyUpdate(e,r.rno)}> 댓글 수정 </button>
                            </div>
                            {
                                /* JSX 형식에서 함수에 매개변수 전달 */
                                /* <마크업 이벤트 = { (e)=>{ 함수명( e , 매개변수 ) } } /> */
                            }
                            <button onClick={(e)=>onDeleteHandler(e,r.rno)}> 삭제 </button>
                            <button onClick={(e)=>onUpdateBox(e,r.rno)}> 수정 </button>
                        </div>)
            })
        }
    </>)
}