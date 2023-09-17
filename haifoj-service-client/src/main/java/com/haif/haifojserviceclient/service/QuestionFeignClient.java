package com.haif.haifojserviceclient.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haif.haifojmodel.model.dto.question.QuestionQueryRequest;
import com.haif.haifojmodel.model.dto.question.QuestionUpdateRequest;
import com.haif.haifojmodel.model.entity.Question;
import com.haif.haifojmodel.model.entity.QuestionSubmit;
import com.haif.haifojmodel.model.vo.QuestionVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhf
 * @description 针对表【question(题目)】的数据库操作Service
 * @createDate 2023-08-16 01:09:28
 */
@FeignClient(name = "haifoj-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {


    /**
     * 根据 id 获取题目信息
     * @param questionId
     * @return
     */
    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);

    /**
     * 根据 id 获取题目提交信息
     * @param questionSubmitId
     * @return
     */
    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);

    /**
     * 更新题目
     * @param questionSubmitUpdate
     * @return
     */
    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmitUpdate);

}