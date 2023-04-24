import React from 'react'
import axios from 'axios'
import Button from '@mui/material/Button';
export default function Find(props) {

    // 1. 아이디찾기
    const findEmail=()=>{ console.log("findEmail 실행")
        let mname = document.querySelector('.mname').value;
        let mphone1 = document.querySelector('.mphone1').value;
    console.log(mname+""+mphone1)

        axios.get("http://localhost:8080/member/findemail" , {params:{mname: mname ,mphone:mphone1 }})
            .then(r=>{ if(r.data!=''){ alert(r.data);} })
            .catch();
    }

    // 2. 비밀번호 찾기
    const findPassword=()=>{ console.log("findPassword 실행")
        let memail = document.querySelector('.memail').value;
        let mphone2 = document.querySelector('.mphone2').value;
    console.log(memail+""+mphone2)

        axios.get("http://localhost:8080/member/findpw" , {params:{memail: memail ,mphone:mphone2 }})
            .then(r=>{ console.log(r);if(r.data!=''){ alert(r.data);} })
            .catch();
    }

    return (<>
       <h3>아이디[이메일] 찾기</h3>
            이름 : <input type="text" className="mname"/> <br/>
            전화번호 : <input type="text" className="mphone1"/> <br/>
            <Button variant="outlined" onClick={findEmail}> 아이디[이메일]찾기 </Button>

       <h3>비밀번호찾기</h3>
            이메일 : <input type="text" className="memail"/> <br/>
            전화번호 : <input type="text" className="mphone2"/> <br/>
            <Button variant="outlined" onClick={findPassword}> 비밀번호 찾기 </Button>
    </>);
}