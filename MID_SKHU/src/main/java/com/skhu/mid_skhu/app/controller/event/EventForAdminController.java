package com.skhu.mid_skhu.app.controller.event;

import com.skhu.mid_skhu.app.dto.event.requestDto.EventCreateRequestDto;
import com.skhu.mid_skhu.app.dto.event.requestDto.EventUpdateRequestDto;
import com.skhu.mid_skhu.app.dto.event.responseDto.EventCreateResponseDto;
import com.skhu.mid_skhu.app.service.event.EventCreateForAdminService;
import com.skhu.mid_skhu.app.service.event.EventModifyForAdminService;
import com.skhu.mid_skhu.global.common.dto.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "행사/이벤트 ADMIN용", description = "행사/이벤트를 관리하는 ADMIN용 api그룹")
@RequestMapping("/api/v1/admin/event")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class EventForAdminController {

    private final EventCreateForAdminService eventCreateForAdminService;
    private final EventModifyForAdminService eventModifyForAdminService;

    @PostMapping("/create")
    @Operation(
            summary = "행사/이벤트 생성",
            description = "행사/이벤트를 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "행사/이벤트 생성 성공"),
                    @ApiResponse(responseCode = "403", description = "url문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "토큰 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<EventCreateResponseDto>> createEvent(
            @RequestPart EventCreateRequestDto requestDto,
            @RequestPart List<MultipartFile> images,
            Principal principal) {

        ApiResponseTemplate<EventCreateResponseDto> data = eventCreateForAdminService.createEvent(requestDto, images, principal);

        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @PutMapping("/update")
    @Operation(
            summary = "행사/이벤트 수정",
            description = "행사/이벤트를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "행사/이벤트 생성 성공"),
                    @ApiResponse(responseCode = "401", description = "권한 문제"),
                    @ApiResponse(responseCode = "404", description = "찾을 수 없는 게시글"),
                    @ApiResponse(responseCode = "500", description = "토큰 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<String>> updateEvent(@RequestBody EventUpdateRequestDto requestDto, Principal principal) {

        ApiResponseTemplate<String> data = eventModifyForAdminService.updateEventDetail(principal, requestDto);

        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @DeleteMapping("/delete/{eventId}")
    @Operation(
            summary = "행사/이벤트 수정",
            description = "행사/이벤트를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "행사/이벤트 생성 성공"),
                    @ApiResponse(responseCode = "401", description = "권한 문제"),
                    @ApiResponse(responseCode = "404", description = "찾을 수 없는 게시글"),
                    @ApiResponse(responseCode = "500", description = "토큰 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<String>> deleteEvent(@PathVariable Long eventId, Principal principal) {

        ApiResponseTemplate<String> data = eventModifyForAdminService.deleteEvent(principal, eventId);

        return ResponseEntity.status(data.getStatus()).body(data);
    }
}
