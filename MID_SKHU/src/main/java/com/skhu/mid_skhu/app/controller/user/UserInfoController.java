package com.skhu.mid_skhu.app.controller.user;

import com.skhu.mid_skhu.app.dto.user.responseDto.UserInfoResponseDto;
import com.skhu.mid_skhu.app.dto.user.responseDto.UserTodoListWrapperResponseDto;
import com.skhu.mid_skhu.app.service.user.UserInfoService;
import com.skhu.mid_skhu.app.service.user.UserTodoListCheckService;
import com.skhu.mid_skhu.global.common.dto.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Printable;
import java.security.Principal;

@RestController
@AllArgsConstructor
@Tag(name = "사용자", description = "사용자 정보를 관리하는 api 그룹")
@RequestMapping("/api/v1/user")
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final UserTodoListCheckService userTodoListCheckService;

    @GetMapping("/info")
    @Operation(
            summary = "마이페이지 정보 조회",
            description = "현재 로그인된 사용자의 마이페이지 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "마이페이지 정보 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            })
    public ResponseEntity<ApiResponseTemplate<UserInfoResponseDto>> getUserInfo(Principal principal) {
        ApiResponseTemplate<UserInfoResponseDto> apiResponseTemplate = userInfoService.getUserInfo(principal);

        return new ResponseEntity<>(apiResponseTemplate, HttpStatus.OK);
    }

    @GetMapping("/todo")
    @Operation(
            summary = "나의 할 일 조회",
            description = "등록한 관심사에 맞는 본인의 일정을 조회합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            })
    public ResponseEntity<ApiResponseTemplate<UserTodoListWrapperResponseDto>> getUserTodoList(Principal principal) {
        ApiResponseTemplate<UserTodoListWrapperResponseDto> data = userTodoListCheckService.checkTodoList(principal);

        return ResponseEntity.status(data.getStatus()).body(data);
    }
}