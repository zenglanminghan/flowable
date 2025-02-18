package src.service;

import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import src.dao.mapper.GdAdviceMapper;

@Component
@Slf4j
public class ExecuteListener implements ExecutionListener {

    @Autowired
    private GdAdviceMapper gdAdviceMapper;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        // 获取单个流程变量
        Object objectResult = delegateExecution.getVariable("result");
        Integer beforeResult = null;
        if (objectResult != null) {
            beforeResult = Integer.valueOf(objectResult.toString());
        }



    }
}
