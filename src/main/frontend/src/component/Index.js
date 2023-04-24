import React from 'react';
import
    { BrowserRouter , Routes , Route }
     from "react-router-dom" // npm install react-router-dom
import Login from "./member/Login"
import Signup from "./member/Signup"
import Header from "./Header"
import Footer from "./Footer"
import Main from "./Main"
import Find from "./member/Find"
import List from "./board/List"
import DashBoard from "./admin/DashBoard"
import Write from "./board/Write"



export default function Index(props) {
    return (<>
        <BrowserRouter>
            <Header />
            <Routes>
                <Route path="/" element={ <Main /> } />
                <Route path="/member/login" element={ <Login /> } />
                <Route path="/member/signup" element={ <Signup /> } />
                <Route path="/member/find" element={ <Find /> } />
                <Route path="/board/list" element={ <List /> } />
                <Route path="/admin/dashboard" element={ <DashBoard /> } />
                <Route path="/board/write" element={ <Write /> } />
            </Routes>
            <Footer />
        </BrowserRouter>
    </> )
}

/*
    react-router-dom 다양한 라우터 컴포넌트 제공
    1. <BrowserRouter>  : 가상 URL 관리[ 브라우저 URL 동기화 ]
    2. <Routes>         : 가장 적합한 <Route> 컴포넌트 검토 후 찾는다.
            요청된 path에 적합한 <Route> 찾아서 <Routes> 범위내 렌더링
    3. <Route>          : 실제 URL 경로 지정해주는 컴포넌트
       <Route path="login" element={<Login />} />  ↓
       http://localhost:3000/login      get 요청시 Login 컴포넌트 반환
*/