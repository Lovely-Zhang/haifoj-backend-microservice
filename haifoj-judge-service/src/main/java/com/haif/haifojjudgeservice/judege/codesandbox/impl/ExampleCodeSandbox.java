package com.haif.haifojjudgeservice.judege.codesandbox.impl;


import com.haif.haifojjudgeservice.judege.codesandbox.CodeSandbox;
import com.haif.haifojmodel.model.codesandbox.ExecuteCodeRequest;
import com.haif.haifojmodel.model.codesandbox.ExecuteCodeResponse;
import com.haif.haifojmodel.model.codesandbox.JudgeInfo;
import com.haif.haifojmodel.model.enums.JudgeInfoMessageEnum;
import com.haif.haifojmodel.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 示例代码沙箱（仅为了跑通流程测试专用）
 */
@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        log.info("测试代码沙箱的实现:{}", "我是测试代码沙箱");
        return executeCodeResponse;
    }
}
