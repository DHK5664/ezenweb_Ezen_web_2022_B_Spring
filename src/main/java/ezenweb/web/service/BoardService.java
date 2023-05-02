package ezenweb.web.service;

import ezenweb.web.domain.board.*;
import ezenweb.web.domain.member.MemberDto;
import ezenweb.web.domain.member.MemberEntity;
import ezenweb.web.domain.member.MemberEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
@Slf4j
public class BoardService {

    @Autowired private CategoryEntityRepository categoryEntityRepository;
    @Autowired private BoardEntityRepository boardEntityRepository;
    @Autowired private MemberEntityRepository memberEntityRepository;

    // 1. 카테고리 등록
    public boolean categoryWrite( @RequestBody BoardDto boardDto ){
        // 1. 입력받은 cname을 Dto에서 카테고리 entity 형변환 해서 save
        log.info("s board dto :" + boardDto);
        CategoryEntity entity = categoryEntityRepository.save(boardDto.toCategoryEntity());

        // 2. 만약에 생성된 엔티티의 pk가  1보다 크면 save 성공
        if(entity.getCno()>= 1){return true;}
        return false;
    }
    // 2. 모든 카테고리 출력
    @Transactional
    public List<CategoryDto> categoryList(  ){    log.info("s categoryList : " );
        List<CategoryEntity> categoryEntityList = categoryEntityRepository.findAll();
        // 형변환 List<엔티티> ---> MAP
       List<CategoryDto> list = new ArrayList<>();
        categoryEntityList.forEach( (e)->{
            list.add(new CategoryDto(e.getCno() , e.getCname()) );
        });  return list;
    }

    // 3. 게시물 쓰기
    @Transactional
    public byte write( BoardDto boardDto ){ log.info("s board dto : " + boardDto );
        // 1. 카테고리 엔티티 찾기 [ 왜?? 게시물엔티티 안에 카테고리 엔티티객체 대입 할려고 ]
        Optional<CategoryEntity> categoryEntityOptional = categoryEntityRepository.findById( boardDto.getCno() ); // 1. 선택된 카테고리 번호를 이용한 카테고리 엔티티 찾기
        if( !categoryEntityOptional.isPresent() ){ return 1; }         // 2. 만액에 선택된 카테고리가 존재하지 않으면  리턴
        CategoryEntity categoryEntity = categoryEntityOptional.get();    // 3. 카테고리 엔티티 추출
        // 2.로그인된 회원의 엔티티 찾기 [ JSP : request.getSession().getAttribute()  ]
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 1. 인증된 인증 정보  찾기
        if( o.equals("anonymousUser") ){ return 2; }
        MemberDto loginDto = (MemberDto)o;   // 2. 형변환
        MemberEntity memberEntity = memberEntityRepository.findByMemail( loginDto.getMemail() ); // 3. 회원엔티티 찾기
        // 3. 게시물 쓰기
        BoardEntity boardEntity = boardEntityRepository.save( boardDto.toBoardEntity() );
        if( boardEntity.getBno() < 1 ){ return  3; }
        // 4. 양방향 관계 [ 카테고리안에 게시물 힙[주소] 대입 , 게시물안에 카테고리 힙[주소] 대입 ]
        categoryEntity.getBoardEntityList().add( boardEntity ); // 1. 카테고리 엔티티에 생성된 게시물 등록
        boardEntity.setCategoryEntity( categoryEntity ); // 2. 생성된 게시물에 카테고리 엔티티 등록
        // 5. 양방향 관계
        boardEntity.setMemberEntity( memberEntity ); // 1. 생성된 게시물 엔티티에 로그인된 회원 등록
        memberEntity.getBoardEntityList().add(boardEntity);  // 2. 로그인된 회원 엔티티에 생성된 게시물 엔티티 등록

        // 공지사항 게시물 정보 확인
        /*
            Optional<CategoryEntity> optionalCategory =  categoryRepository.findById( 1 );
            log.info( "공지사항 카테고리 엔티티 확인 :" + optionalCategory.get() );
            log.info( "공지사항 카테고리 엔티티 확인 :" + optionalCategory.get().getBoardEntityList().get(1).getMemberEntity().getMemail() );
        */

        return 4;
    }

    // 4. 게시물 출력
    @Transactional
    public PageDto list(  PageDto pageDto  ){
        // 1. pageable 인터페이스 [ 페이징처리 관련 api ] // import org.springframework.data.domain.Pageable;
        Pageable pageable = PageRequest.of( pageDto.getPage()-1 , 5 , Sort.by( Sort.Direction.DESC , "bno") );
        // PageRequest.of( 현재 페이지번호[0시작] , 페이지당 표시할 게시물수  , Sort.by( Sort.Direction.ASC/DESC , '정렬기준필드명'  ) );
        Page<BoardEntity> entityPage =
                boardEntityRepository.findBySearch( pageDto.getCno() , pageDto.getKey() , pageDto.getKeyword() , pageable );
        // entity --> dto
        List<BoardDto> boardDtoList = new ArrayList<>();
        entityPage.forEach( (b)->{ boardDtoList.add( b.toDto() ); });
        //log.info("총 게시물수 : " + entityPage.getTotalElements() );  log.info("총 페이지수 : " + entityPage.getTotalPages() );
        pageDto.setBoardDtoList( boardDtoList );
        pageDto.setTotalPage(entityPage.getTotalPages());
        pageDto.setTotalCount(entityPage.getTotalElements() );
        return pageDto;
    }
    // 5. 게시물 개별 조회/출력 + 댓글 출력
    public BoardDto getboard( int bno ){
        Optional<BoardEntity> optionalBoardEntity = boardEntityRepository.findById( bno );
        if( optionalBoardEntity.isPresent() ){  // 게시물 출력시 현재 게시물의 댓글도 같이~~ 출력
            BoardEntity boardEntity = optionalBoardEntity.get();
            // 상위 댓글
            List<ReplyDto> list = new ArrayList<>();
            boardEntity.getReplyEntityList().forEach((r)->{ // 댓글 같이~~ 형변환 [ toDto vs 서비스 ]

                if(r.getRindex()==0){ // 상위 댓글 필터
                    list.add(r.todto()); // 상위댓글 리스트
                    // 하위 "답"글
                    boardEntity.getReplyEntityList().forEach((r2)->{
                        if(r.getRno()==r2.getRindex()){
                            list.get(list.size()-1).getRereplyDtoList().add(r2.todto());
                                // 리스트길이-1 : 최근에 리스트에 등록한 인덱스 번호
                        }
                    });
                }


            });
            BoardDto boardDto = boardEntity.toDto();
            boardDto.setReplyDtoList(list);
            return boardDto;
        }
        return null;
    }
    // 6. 내가 쓴 게시물 출력
    public List<BoardDto> myboards( ){log.info("s myboards : " );
        // 1. 로그인 인증 세션[object] --> dto 강제형변환
        MemberDto memberDto = (MemberDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 일반회원dto : 모든 정보 , oauth2dto : memail , mname , mrol
        // 2. 회원 엔티티 찾기
        MemberEntity entity =  memberEntityRepository.findByMemail( memberDto.getMemail() );
        // 3. 회원 엔티티 내 게시물리스트를 반복문 돌려서 dto 리스트 로 변환
        List<BoardDto> list = new ArrayList<>();
        entity.getBoardEntityList().forEach( (e)->{
            list.add( e.toDto() );
        });
        return list;
    }

    // 7.
    public boolean delete( int bno  ){
        Optional<BoardEntity> optionalBoardEntity = boardEntityRepository.findById( bno );
        if( optionalBoardEntity.isPresent() ){
            boardEntityRepository.delete( optionalBoardEntity.get() );
            return true;
        }
        return false;
    }

    // 8.
    @Transactional
    public boolean update(BoardDto boardDto){
        Optional<BoardEntity> optionalBoardEntity = boardEntityRepository.findById(boardDto.getBno());
        if(optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
                boardEntity.setBtitle(boardDto.getBtitle());
                boardEntity.setBcontent(boardDto.getBcontent());
                return true;
        }
        return false;
    }
    @Autowired
    private ReplyEntityRepository replyEntityRepository;
    @Transactional
    public boolean postReply(@RequestBody ReplyDto replyDto){ log.info("postReply : " + replyDto);
        // 0. 로그인 했는지
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(o.equals("anonymousUser")){return false;}
        MemberDto memberDto = (MemberDto)o;
        MemberEntity memberEntity = memberEntityRepository.findById(memberDto.getMno()).get();

        // 0. 댓글 작성할 게시물 호출
        Optional<BoardEntity> optionalBoardEntity = boardEntityRepository.findById(replyDto.getBno());
        if(!optionalBoardEntity.isPresent()){return false;}
        BoardEntity boardEntity = optionalBoardEntity.get();

        // 1. 댓글 작성한다.
        ReplyEntity replyEntity = replyEntityRepository.save(replyDto.toEntity());
        if(replyEntity.getRno() <1) {return false;}
        
        // 2. 댓글과 회원의 양방향 관계[ 댓글->회원 / 회원->댓글 == 양방향 , 댓글->회원 == 단방향 ]
        replyEntity.setMemberEntity( memberEntity );
        memberEntity.getReplyEntityList().add(replyEntity);
        // 3. 댓글과 게시물의 양방향 관계 [ 댓글->게시물 / 게시물->댓글 == 양방향 , 댓글->게시물 == 단방향]
        replyEntity.setBoardEntity(optionalBoardEntity.get());
        boardEntity.getReplyEntityList().add(replyEntity);

        return true;
    }
    @Transactional
    public boolean getReply(){ // 게시물 출력 시 대체
        return true;
    }
    @Transactional
    public boolean putReply(@RequestBody ReplyDto replyDto){ log.info("putReply : " + replyDto);
        // {rno : "수정할 번호" , rcontent : "수정할 내용"}
        Optional<ReplyEntity> optionalReplyEntity = replyEntityRepository.findById(replyDto.getRno());
        if(optionalReplyEntity.isPresent()){
            optionalReplyEntity.get().setRcontent(replyDto.getRcontent());
            return true;
        }
        return false;
    }
    @Transactional
    public boolean deleteReply(@RequestParam int rno){ log.info("deleteReply : " + rno);
        Optional<ReplyEntity> optionalReplyEntity = replyEntityRepository.findById(rno);
        if(optionalReplyEntity.isPresent()){
            replyEntityRepository.delete(optionalReplyEntity.get());
            
            // [과제 : 만일 상위댓글이 삭제되면 답글도 같이 삭제]
            
            return true;
        }
        return false;
    }
}
