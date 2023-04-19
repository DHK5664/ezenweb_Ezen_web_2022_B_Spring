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
        item.id = "ID-"+item.length // ID 구성
        item.done = false;          // 체크 여부
        setItems( [...items , item ] ); // 기존 상태 items 에 item 추가
        // setItems( [ ...기본배열 , 값 ] );
    }
    // 반복문 이용한 Todo 컴포넌트 생성
    const TodoItems =
        <Paper style={{margin : 16}}> {/*JSX 의 style 속성 방법*/}
            <List>
                {
                    items.map( (i)=>
                        <Todo item = {i} key = {i.id}/>
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