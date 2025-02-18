package src.service;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.el.FixedValue;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;
import src.config.SpringContextHolder;


@Component
public class ProcessTaskListener implements TaskListener {

    private FixedValue code;

    @Override
    public void notify(DelegateTask delegateTask) {
        Object value = code.getValue(null);
        ProcessService processService = SpringContextHolder.getBean("processService");
        try {
            processService.notify(delegateTask, Integer.parseInt(value.toString()));
        } catch (Exception e) {
            throw new RuntimeException("监听器执行异常,原因: " + e.getMessage());
        }

    }
}
