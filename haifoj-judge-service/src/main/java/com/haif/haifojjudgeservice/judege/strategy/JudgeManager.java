package com.haif.haifojjudgeservice.judege.strategy;

import com.haif.haifojmodel.model.codesandbox.JudgeInfo;
import com.haif.haifojmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化配置）
 */
@Service
public class JudgeManager {

    /**
     * 执行逻辑
     */
    public JudgeInfo doJudge(JudgeContetxt judgeContetxt){
        QuestionSubmit questionSubmit = judgeContetxt.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContetxt);
    }

}
