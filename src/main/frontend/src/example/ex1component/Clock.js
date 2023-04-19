import React from 'react';

export default function Clock( props ){
    // 함수 안에서 js 문법은 자유롭게 작성 가능
        //let clock = new Date().toLocaleTimeString();
    // 함수 안에 return ( ) 안에서 js 문법은 {} 처리 해야함 = JSX 문법

    return ( <>
        <div>
            <h3> 리액트 시계 </h3>
            <h4> 현재 시계 : { new Date().toLocaleTimeString() }</h4>
        </div>
     </> )
}