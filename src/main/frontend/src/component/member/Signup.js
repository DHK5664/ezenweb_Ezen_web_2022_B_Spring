import React , {useState , useEffect} from 'react'
import axios from 'axios'

export default function Signup(props) {
let [memailMsg , setMemailMsg] = useState();
let [mphoneMsg , setMphoneMsg] = useState();
    // 1. 회원가입
    //function onSignup(){} ---> 변수형 익병함수 변환
    //function onSignup(){} ---> const 변수 = () => {}
    const onSignup=()=>{ console.log("onSignup open")
        let info = {
            memail : document.querySelector(".memail").value,
            mpassword : document.querySelector(".mpassword").value,
            mname : document.querySelector(".mname").value,
            mphone : document.querySelector(".mphone").value
        }
        console.log(info)
    // ajax ---> axios 변환
    axios
        .post("http://localhost:8080/member/info" , info)
        .then(r=>{
            console.log(r);
            if(r.data == true){
                alert('회원가입 성공');window.location.href="/login"; //window.location.href="이동할 경로"
            }else{
                alert('가입 실패 [ 관리자에게 문의 ]')
            }
        } )
        .catch(err=>{ console.log(err) });
    }

    // 2. 아이디 중복체크
    const idCheck = (e)=>{
        // 1.console.log(document.querySelector('.memail').value);
        // 2. console.log(e.target.value);
        axios.get("http://localhost:8080/member/idcheck",{params:{memail:e.target.value}})
            .then(res=>{
                if(res.data==true){setMemailMsg('사용중인 아이디 입니다.')}
                else{setMemailMsg('사용가능한 이메일 입니다.')}
                }
            )
            .catch(e=>console.log(e))
    }

    const phoneCheck = (e)=>{
    axios.get("http://localhost:8080/member/phonecheck" , {params:{mphone:e.target.value}})
        .then(r=>{
            if(r.data==true){setMphoneMsg('전화번호 중복')}
            else{setMphoneMsg('★')}
            }
        )
        .catch(e=>console.log(e))
    }

    return(<div>
            <h3>회원가입 페이지</h3>
            <form>
                아이디[이메일] : <input type="text" className="memail" onChange={idCheck}/>
                <span>{memailMsg}</span>
                <br/>
                비밀번호 : <input type="text" className="mpassword"/> <br/>
                이름 : <input type="text" className="mname"/> <br/>
                전화번호 : <input type="text" className="mphone" onChange={phoneCheck} />
                <span>{mphoneMsg}</span>
                <br/>
                <button onClick={onSignup} type="button">가입</button>
            </form>
    </div>)
}
/*
    HTML ---> JSX
        1. <> </>
        2. class -> className
        3. style -> style={{}}
        4. 카멜표기법:
            onclick -> onClick
            margin-top -> marginTop
        5.

*/