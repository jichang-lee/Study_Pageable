package org.spring.jpaboardpj.controller;

import lombok.RequiredArgsConstructor;
import org.spring.jpaboardpj.dto.BoardDto;
import org.spring.jpaboardpj.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

 /* @Autowired
  private BoardService boardService;*/
  private final BoardService boardService;
  //  /board/   ,  /board/index
  @GetMapping({"","/index"})
  public String index(){
    return "board/index";
  }
  @GetMapping("/write")
  public String write(){
    return "board/write";
  }

  @PostMapping("/write")
  public String writeInsert(@ModelAttribute BoardDto boardDto){
    boardService.insertBoard(boardDto);
    //글목록으로 바로이동
   return "redirect:/board/list";
  }

  @GetMapping("/list")
 public String boardList(Model model){

   List<BoardDto> boardList=boardService.boardListDo();
   model.addAttribute("lists",boardList);

   return "/board/boardList";
  }

  @GetMapping("/detail/{id}")
  public String boardDetail(@PathVariable Long id, Model model){

    // 게시글 조회수 1증가
    boardService.upHit(id);
    BoardDto board=boardService.boardDetail(id);
    if(board!=null){
      System.out.println("게시글 OK-> 글상세보기로 이동");
      model.addAttribute("board",board);
      return "board/boardDetail";
    }else {
      System.out.println("게시글 NO-> 글작성 페이지로 이동");
      return "redirect:/board/write";
    }

  }




  @GetMapping("/pagingList")
  public String paginList(Model model , @PageableDefault(page = 0,size = 3,sort = "id",
                                  direction = Sort.Direction.DESC) Pageable pageable) {

    Page<BoardDto> boardList=boardService.boardPagingList(pageable);

    Long total = boardList.getTotalElements(); //전체 레코드 수 return 타입 Long

    int blockNum=4;
    int nowPage= boardList.getNumber()+1 ;// 현재페이지-> boardList.getNumber()는 0부터 시작
      //Math.max (숫자1,숫자2) 두 수를 비교해서 큰 값을 반환
      //Math.max(1, 0-4);
    int startPage=Math.max(1,boardList.getNumber()-blockNum); //시작페이지
    int endPage=boardList.getTotalPages(); // 마지막페이지

      model.addAttribute("total",total);  //boardList에 전체 레코드 수
    model.addAttribute("boardList",boardList);  //boardList (게시글 리스트)
    model.addAttribute("nowPage",nowPage);      //현재페이지
    model.addAttribute("startPage",startPage);  //시작페이지
    model.addAttribute("endPage",endPage);      //끝페이지

    return "/board/pagingList";
  }

  @GetMapping("/pagingList2")
    public String pagingList2(Model model,
                              @PageableDefault(page = 0,size = 4, sort = "id",
                                      direction = Sort.Direction.DESC) Pageable pageable){

      Page<BoardDto> boardList = boardService.boardPagingList2(pageable);

      int blockNum=4;
      int nowPage=boardList.getNumber()+1;
      int startPage=Math.max(1,boardList.getNumber()-blockNum);
      int endPage = boardList.getTotalPages();

      model.addAttribute("boardList",boardList);
      model.addAttribute("nowPage",nowPage);
      model.addAttribute("startPage",startPage);
      model.addAttribute("endPage",endPage);

      return "/board/pagingList2";
  }















}
