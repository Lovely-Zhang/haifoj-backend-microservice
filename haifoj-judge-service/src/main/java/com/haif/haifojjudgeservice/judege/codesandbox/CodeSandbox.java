package com.haif.haifojjudgeservice.judege.codesandbox;


import com.haif.haifojmodel.model.codesandbox.ExecuteCodeRequest;
import com.haif.haifojmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 实现代码沙箱的不同调用
 */
public interface CodeSandbox {

    /**
     * 代码沙箱接口
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);


}
