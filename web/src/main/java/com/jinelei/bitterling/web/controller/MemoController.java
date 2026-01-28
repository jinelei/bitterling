package com.jinelei.bitterling.web.controller;

import com.jinelei.bitterling.core.controller.BaseController;
import com.jinelei.bitterling.core.domain.result.GenericResult;
import com.jinelei.bitterling.web.domain.MemoDomain;
import com.jinelei.bitterling.web.service.MemoService;
import com.jinelei.bitterling.web.service.MemoTagService;
import com.jinelei.bitterling.web.service.IndexService;
import com.jinelei.bitterling.web.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/memo")
@Tag(name = "备忘管理", description = "备忘相关接口")
public class MemoController extends BaseController {
    private final MemoService service;
    private final MemoTagService memoTagService;
    private final IndexService indexService;
    private final MessageService messageService;

    public MemoController(MemoService service, IndexService indexService, MessageService messageService,
                          MemoTagService memoTagService) {
        this.service = service;
        this.indexService = indexService;
        this.messageService = messageService;
        this.memoTagService = memoTagService;
    }

    @PostMapping("create")
    @Operation(operationId = "memoCreate", summary = "新增备忘", description = "新增备忘")
    public GenericResult<String> create(@Valid MemoDomain.CreateRequest req) {
        this.service.create(req);
        return GenericResult.success("新增成功");
    }

    @PostMapping("update")
    @Operation(operationId = "memoUpdate", summary = "更新备忘", description = "根据id更新备忘")
    public GenericResult<String> update(@Valid MemoDomain.UpdateRequest req) {
        this.service.update(req);
        return GenericResult.success("更新成功");
    }

    @PostMapping("delete/{id}")
    @Operation(operationId = "memoDelete", summary = "删除备忘", description = "根据id删除备忘")
    public GenericResult<String> deleteById(@PathVariable(value = "id", required = true) Long id) {
        this.service.deleteById(id);
        return GenericResult.success("删除成功");
    }

}
