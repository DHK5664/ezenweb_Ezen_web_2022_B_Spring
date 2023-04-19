package ezenweb.web.service;

import ezenweb.web.domain.board.*;
import ezenweb.web.domain.member.MemberDto;
import ezenweb.web.domain.member.MemberEntity;
import ezenweb.web.domain.member.MemberEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

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
    // 2. 게시물 쓰기
    public boolean write(@RequestBody BoardDto boardDto){log.info("s board dto :" + boardDto);
        // 1. 선택된 카테고리 번호를 이용한 카테고리 엔티티 찾기
        Optional<CategoryEntity> categoryEntityOptional = categoryEntityRepository.findById(boardDto.getCno());
        if(!categoryEntityOptional.isPresent()){return false;}// 2. 만약에 선택된 카테고리가 존재하지 않으면
        CategoryEntity categoryEntity = categoryEntityOptional.get(); // 3. 카테고리 엔티티 추출
        // 2.로그인 된 회원의 엔티티 출력하기
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 1. 인증된 회원 정보 찾기
        if(o.equals("anonymousUser")) {return false;}
        MemberDto loginDto = (MemberDto) o;   // 2. 형변환
        MemberEntity memberEntity = memberEntityRepository.findByMemail(loginDto.getMemail());// 3. 회원엔티티 찾기
        // 3. 게시물 쓰기
        BoardEntity boardEntity = boardEntityRepository.save(boardDto.toBoardEntity());
        if(boardEntity.getBno() < 1 ){return false;}
        // 4. 양방향 관계
        categoryEntityOptional.get().getBoardEntityList().add(boardEntity);// 1. 카테고리 엔티티에 생성된 게시물 등록
        boardEntity.setCategoryEntity(categoryEntity);// 2. 생성된 게시물에 카테고리 엔티티 등록
        // 5. 양방향 관계
        boardEntity.setMemberEntity(memberEntity); // 1. 생성된 게시물 엔티티에 로그인된 회원 등록
        memberEntity.getBoardEntityList().add(boardEntity); // 2. 로그인된 회원 엔티티에 생성된 게시물 엔티티 등록

        // --
        log.info(boardEntity.toString());

        return true;
    }
    // 3. 내가 쓴 게시물 출력
    public List<BoardDto> myboards(){
        log.info("s myboards : ");
        return null;
    }
}