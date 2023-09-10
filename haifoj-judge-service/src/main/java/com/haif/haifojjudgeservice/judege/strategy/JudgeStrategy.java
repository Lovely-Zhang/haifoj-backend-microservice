package com.haif.haifojjudgeservice.judege.strategy;


import com.haif.haifojmodel.model.codesandbox.JudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     */
    JudgeInfo doJudge(JudgeContetxt judgeContetxt);

}
