package com.haif.haifojjudgeservice.judege.codesandbox;


import com.haif.haifojmodel.model.codesandbox.ExecuteCodeRequest;
import com.haif.haifojmodel.model.codesandbox.ExecuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码沙箱代理类（使用代理模式来增强接口方法，执行额外的日志输出）
 */
@Slf4j
@AllArgsConstructor
public class CodeSandboxProxy implements CodeSandbox {

    private final CodeSandbox codeSandbox;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("日志输出：{}","代码沙箱请求信息");
        return codeSandbox.executeCode(executeCodeRequest);
    }
}
