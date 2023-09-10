package com.haif.haifojjudgeservice.judege;

import cn.hutool.json.JSONUtil;
import com.haif.haifojcommon.common.ErrorCode;
import com.haif.haifojcommon.exception.BusinessException;
import com.haif.haifojjudgeservice.judege.codesandbox.CodeSandbox;
import com.haif.haifojjudgeservice.judege.codesandbox.CodeSandboxFactory;
import com.haif.haifojjudgeservice.judege.codesandbox.CodeSandboxProxy;
import com.haif.haifojjudgeservice.judege.strategy.JudgeContetxt;
import com.haif.haifojjudgeservice.judege.strategy.JudgeManager;
import com.haif.haifojmodel.model.codesandbox.ExecuteCodeRequest;
import com.haif.haifojmodel.model.codesandbox.ExecuteCodeResponse;
import com.haif.haifojmodel.model.codesandbox.JudgeInfo;
import com.haif.haifojmodel.model.dto.question.JudgeCase;
import com.haif.haifojmodel.model.entity.Question;
import com.haif.haifojmodel.model.entity.QuestionSubmit;
import com.haif.haifojmodel.model.enums.JudgeInfoMessageEnum;
import com.haif.haifojmodel.model.enums.QuestionSubmitStatusEnum;
import com.haif.haifojserviceclient.service.QuestionService;
import com.haif.haifojserviceclient.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    QuestionService questionService;
    @Resource
    QuestionSubmitService questionSubmitService;
    @Resource
    JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String type;


    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1）传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 2）如果题目提交状态不为等待中，就不用重复执行了
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 3）更改判题（题目提交）的状态为 “判题中”，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "正在判题中");
        }
        // 4）调用沙箱，获取到执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        // 增强
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        // 获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        // 获取代码沙箱输出结果
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        // 5）根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContetxt judgeContetxt = new JudgeContetxt();
        judgeContetxt.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContetxt.setJudgeCaseList(judgeCaseList);
        judgeContetxt.setInputList(inputList);
        judgeContetxt.setOutputList(executeCodeResponse.getOutputList());
        judgeContetxt.setQuestion(question);
        judgeContetxt.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContetxt);
        // 6）修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        // 判断返回的结果信息是否成功，为其改变题目提交状态
        if (judgeInfo.getMessage().equals(JudgeInfoMessageEnum.ACCEPTED.getValue())) {
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        } else {
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
        }
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        return questionSubmitService.getById(questionSubmitId);
    }
}
