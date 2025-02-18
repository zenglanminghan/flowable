package src;

import liquibase.pro.packaged.E;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import src.service.ProcessService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class FlowableApplicationTests {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Test
    void contextLoads() {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("assignee", "啊啊");
            ProcessInstance test = runtimeService.startProcessInstanceByKey("LC-4", variables);
            System.out.println("aaa");
        } catch (Exception e) {
            System.out.println("aaa");
        }


        System.out.println("aaa");
    }


    @Test
    void contextLoads2() {
        try {
            ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
            List<Task> tasks = taskService.createTaskQuery().active().list();
            TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId("2271cfc5-cd95-11ef-bc4c-14169ecd962d");
            List<Task> tasks1 = taskQuery.taskCandidateUser("啊啊").list();
            Task task = taskService.createTaskQuery().taskId("15163253-cd92-11ef-ad11-14169ecd962d").singleResult();
            System.out.println("aaa");
        } catch (Exception e) {
            System.out.println("aaa");
        }


        System.out.println("aaa");
    }

    @Autowired
    private ProcessService processService;

    @Test
    void contextLoads22() {
        try {
            processService.isNextNodeMultiInstanceParallel("fcad5db3-d3eb-11ef-bf6e-14169ecd962d");

        } catch (Exception e) {
            System.out.println("aaa");
        }


        System.out.println("aaa");
    }

}
