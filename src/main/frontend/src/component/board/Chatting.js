import React,{useEffect,useState,useRef} from 'react'
import Container from '@mui/material/Container';

export default function Chatting(props) {

    let [msgContent , setMsgContent] = useState([]); // 현재 채팅중인 메시지를 저장하는 변수
    let msgInput = useRef(null);    // 채팅입력창[input] DOM객체 제어 변수

    // 1. 재랜더링 될때마다 새로운 접속
    // let 클라이언트소켓 = new WebSocket("ws://localhost:8080/chat");
    // 2. 재렌더링 될때 데이터 상태 유지
    let 클라이언트소켓 = useRef( new WebSocket("ws://localhost:8080/chat") );
    // 3.서버소켓에 접속했을때 이벤트
    클라이언트소켓.current.onopen = () => {console.log('접속했습니다.');}
    // 3. 서버소켓에서 나갔을때 이벤트
    클라이언트소켓.current.onclose = (e) => {console.log('서버 나갔습니다.');}
    // 3. 서버소켓과 오류 발생했을때ㅐ 이벤트
    클라이언트소켓.current.onerror = (e) => {console.log('소켓 오류.');}
    // 3. 서버소켓으로 부터 메세지 받았을때 이벤트
    클라이언트소켓.current.onmessage = (e) => {
        console.log('서버소켓으로 메시지 받음.'); console.log(e); console.log(e.data);
        //msgContent.push(e.data);    // 배열에 내용 추가
        //setMsgContent([...msgContent]); // 재 렌더링
        setMsgContent([...msgContent , e.data]); // 재 렌더링
    }

    const onSend = () =>{
        // mspInput변수가 참조중인 <input ref={ msgInput } /> 해당 input 을 DOM객체로 호출
        let msg = msgInput.current.value;
        클라이언트소켓.current.send( msg );    // 클라이언트가 서버에게 메시지 전송 [ .send() ]
        msgInput.current.value = '';
    }

    return (<>
        <Container>
            <h6>익명 채팅방</h6>
            <div className="chatContentsBox">
            {
                msgContent.map((m)=>{
                    return(<><div> { m } </div></>)
                })
            }
            </div>
            <div className="chatInputBox">
                <span> 익명94 </span>
                <input ref={ msgInput } type="text" />
                <button onClick={ onSend }>전송</button>
            </div>
        </Container>
    </>)
}
/*
    JSX : html + js     {   }
*/