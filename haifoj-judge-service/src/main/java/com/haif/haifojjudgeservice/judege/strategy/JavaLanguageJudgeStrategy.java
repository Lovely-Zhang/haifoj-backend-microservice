package com.haif.haifojjudgeservice.judege.strategy;


import cn.hutool.json.JSONUtil;
import com.haif.haifojcommon.common.ErrorCode;
import com.haif.haifojcommon.exception.ThrowUtils;
import com.haif.haifojmodel.model.codesandbox.JudgeInfo;
import com.haif.haifojmodel.model.dto.question.JudgeCase;
import com.haif.haifojmodel.model.dto.question.JudgeConfig;
import com.haif.haifojmodel.model.entity.Question;
import com.haif.haifojmodel.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * java 程序 判题策略
 */
public class JavaLanguageJudgeStrategy implements JudgeStrategy {


    /**
     * 执行判题
     */
    @Override
    public JudgeInfo doJudge(JudgeContetxt judgeContetxt) {
        JudgeInfo judgeInfo = judgeContetxt.getJudgeInfo();
        Long memory = judgeInfo.getMemory();
        ThrowUtils.throwIf(memory == null, ErrorCode.NOT_FOUND_ERROR);
        Long time = judgeInfo.getTime();
        ThrowUtils.throwIf(time == null, ErrorCode.NOT_FOUND_ERROR);
        Question question = judgeContetxt.getQuestion();
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);

        List<JudgeCase> judgeCaseList = judgeContetxt.getJudgeCaseList();
        List<String> inputList = judgeContetxt.getInputList();
        List<String> outputList = judgeContetxt.getOutputList();
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        // 先判断沙箱代码的结果输出数量和预期输出数量是否相等
        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        // 依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        // 判断题目限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needTimeLimit = judgeConfig.getTimeLimit();
        if (memory > needMemoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        // JAVA 程序本身需要额外执行 10 ms
        long JAVA_PROGRAM_TIME_COST = 10000L;
        if ((time - JAVA_PROGRAM_TIME_COST) > needTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }


}
