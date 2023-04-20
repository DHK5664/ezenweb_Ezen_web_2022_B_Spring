// 교재 App컴포넌트 --> AppTodo 컴포넌트
import React , { useState } from 'react';
import Todo from './Todo';
import {List , Paper , Container} from '@mui/material';
import AddTodo from './AddTodo';


export default function AppTodo(props){

    // 1.
    // item 변수에 우측 객체를 대입한것{ id : "0",  title : "Hello World 1",  done : true  }
    const [ items , setItems ] = useState(
        [ // array s
        ] // array end
    ) // useState 함수 end

    // 2. items에 새로운 item 등록하는 함수
    const addItem = (item)=>{ // 함수로부터 매개변수로 전달받은 item
        item.id = "ID-"+items.length // ID 구성 // ??? DB PK 사용
        item.done = false;          // 체크 여부
        setItems( [...items , item ] ); // 기존 상태 items 에 item 추가
        // item = { title : "입력받은값" , id="id-배열길이" , done = "기본값false" }
        // setItems( [ ...기본배열 , 값 ] );
    }

    // 3. items 에 item 삭제
    const deleteItem = (item) =>{
        console.log(item.id);
        // 만약에 items에 있는 item 중 id와 삭제할 id가 다른경우 해당 item 반환
        const newItems= items.filter(i => i.id !== item.id);
            // * 삭제할 id를 제외한 새로운 newItems 배열이 선언
        setItems([...newItems]);
    }

    // 반복문 이용한 Todo 컴포넌트 생성
    const TodoItems =
        <Paper style={{margin : 16}}> {/*JSX 의 style 속성 방법*/}
            <List>
                {
                    items.map( (i)=>
                        <Todo item = {i} key = {i.id} deleteItem={deleteItem}/>
                    )
                }
            </List>
        </Paper>
    return (<>
        <div className="App">
            <Container maxWidth="md">
                <AddTodo addItem={addItem} />
                { TodoItems }
            </Container>
        </div>
    </>);
}

// JS 반복문 함수 제공
    // r = [ 1, 2, 3 ]
    // 배열/리스트.forEach( (o)=>{} ) : 반복문만 가능 [ return 없음 ]
        // let array = r.forEach( (o)=>{ o+3 });
        // 반복문이 끝나면 array에는 아무것도 들어있지 않음.

    // 배열/리스트.map( (o)=>{} )     : + return 값들을 새로운 배열에 저장
        // let array = r.map( (o)=>{ return o+3 });
        // 반복문이 끝나면 array에는 [4,5,6]

    // 배열/리스트.filter( (o)=>{} )  : + 조건충족시 객체 반환
        // let array = r.filter( (o)=>{  o>=3 });
        // 반복문이 끝나면 array 에는 [3]