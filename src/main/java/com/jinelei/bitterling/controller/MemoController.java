package com.jinelei.bitterling.controller;

import com.jinelei.bitterling.domain.MemoTagDomain;
import com.jinelei.bitterling.domain.convert.MemoConvertor;
import com.jinelei.bitterling.domain.convert.MemoTagConvertor;
import com.jinelei.bitterling.domain.request.*;
import com.jinelei.bitterling.domain.response.MemoTagResponse;
import com.jinelei.bitterling.domain.result.GenericResult;
import com.jinelei.bitterling.domain.MemoDomain;
import com.jinelei.bitterling.domain.result.PageableResult;
import com.jinelei.bitterling.service.MemoService;
import com.jinelei.bitterling.service.MemoTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/memo")
@Tag(name = "备忘管理", description = "备忘相关接口")
public class MemoController extends BaseController {
    private final MemoService service;
    private final MemoTagService memoTagService;
    private final MemoConvertor memoConvertor;
    private final MemoTagConvertor memoTagConvertor;

    public MemoController(MemoService service, MemoTagService memoTagService, MemoConvertor memoConvertor, MemoTagConvertor memoTagConvertor) {
        this.service = service;
        this.memoTagService = memoTagService;
        this.memoConvertor = memoConvertor;
        this.memoTagConvertor = memoTagConvertor;
    }

    @PostMapping("tags")
    @Operation(operationId = "memoTags", summary = "查询备忘标签", description = "查询备忘标签")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GenericResult.class)))
    public GenericResult<List<MemoTagResponse>> tags() {
        Iterable<MemoTagDomain> all = memoTagService.findAll();
        List<MemoTagResponse> list = StreamSupport.stream(all.spliterator(), false).map(memoTagConvertor::toResponse).toList();
        return GenericResult.success(list);
    }

    @PostMapping("page")
    @Operation(operationId = "memoPage", summary = "查询备忘分页", description = "查询备忘分页")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GenericResult.class)))
    public PageableResult<List<MemoResponse>> page(@RequestBody @Valid PageableRequest<MemoPageRequest> req) {
        Page<MemoDomain> page = service.page(req);
        List<MemoResponse> response = memoConvertor.toResponse(page.getContent());
        return PageableResult.of(200, "查询成功", page.getPageable().getPageNumber(), page.getPageable().getPageSize(), response, page.getTotalElements());
    }

    @PostMapping("create")
    @Operation(operationId = "memoCreate", summary = "新增备忘", description = "新增备忘")
    @ApiResponse(responseCode = "200", description = "新增成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GenericResult.class)))
    public GenericResult<String> create(@RequestBody @Valid MemoCreateRequest req) {
        this.service.create(req);
        return GenericResult.success("新增成功");
    }

    @PostMapping("update")
    @Operation(operationId = "memoUpdate", summary = "更新备忘", description = "根据id更新备忘")
    @ApiResponse(responseCode = "200", description = "更新成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GenericResult.class)))
    public GenericResult<String> update(@RequestBody @Valid MemoUpdateRequest req) {
        this.service.update(req);
        return GenericResult.success("更新成功");
    }

    @PostMapping("delete")
    @Operation(operationId = "memoDelete", summary = "删除备忘", description = "根据id删除备忘")
    @ApiResponse(responseCode = "200", description = "删除成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GenericResult.class)))
    public GenericResult<String> delete(@RequestBody @Valid MemoDeleteRequest req) {
        this.service.deleteById(req.id());
        return GenericResult.success("删除成功");
    }

}
