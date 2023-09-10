package com.haif.haifojjudgeservice.judege.codesandbox.impl;


import com.haif.haifojjudgeservice.judege.codesandbox.CodeSandbox;
import com.haif.haifojmodel.model.codesandbox.ExecuteCodeRequest;
import com.haif.haifojmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用别人的第三方沙箱）
 */
public class ThirdPartyCodeSanbox implements CodeSandbox
{
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱" );
        return null;
    }
}

