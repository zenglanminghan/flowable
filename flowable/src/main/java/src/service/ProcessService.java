package src.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import liquibase.pro.packaged.S;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.*;
import org.flowable.engine.*;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.form.api.FormDefinition;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.IdmEngine;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import src.dao.entry.GdAdvice;
import src.dao.mapper.GdAdviceMapper;
import src.mode.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;


@Service
@Slf4j
public class ProcessService {
    private static final String UPLOAD_DIR = "C:\\Users\\13094\\Desktop\\desk\\logs3\\New Folder (1)\\";

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdmEngine idmEngine;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FormService formService;

    @Autowired
    private GdAdviceMapper gdAdviceMapper;

    @Autowired
    private ProcessEngine processEngine;

    /**
     * 新增用户
     *
     * @param userVo 用户对象
     * @return Boolean
     */
    public Boolean saveUser(@RequestBody UserVo userVo) {
        try {
            // 使用Flowable Identity Service创建新用户
            User user = idmEngine.getIdmIdentityService().newUser(userVo.getId());
            user.setFirstName(userVo.getFirstName());
            user.setLastName(userVo.getLastName());
            user.setPassword(userVo.getPassword());
            user.setEmail(userVo.getEmail());
            idmEngine.getIdmIdentityService().saveUser(user);
            return true;
        } catch (Exception e) {
            log.error("新增对象失败,原因: " + e.getMessage());
            return false;
        }
    }

    /**
     * 上传bpmn并发布流程,此接口用于保存bpmn.xml并发布流程,并返回状态
     *
     * @param file xml文件
     * @return String
     */
    public String saveXmlAndPublish(@RequestPart("file") MultipartFile file) {
        String result = "false";
        try {
            if (file.isEmpty()) {
                log.error("bpmn为空");
                return null;
            }
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            // 构建目标文件路径
            Path targetLocation = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            // 将文件保存到本地磁盘
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            File zip = targetLocation.toFile();
            try (InputStream inputStream = Files.newInputStream(zip.toPath())) {
                // 部署ZIP文件
                ZipInputStream zipInputStream = new ZipInputStream(inputStream);
                Deployment deployment = repositoryService.createDeployment()
                        .name("test")
                        .addZipInputStream(zipInputStream)
                        .deploy();
                result = deployment.getId();
            } catch (Exception e) {
                log.error("部署失败: " + e.getMessage());
                throw new RuntimeException("部署失败: " + e.getMessage());
            }
        } catch (Exception e) {
            log.error("xml发布失败,原因: " + e.getMessage());
        }
        return result;
    }

    /**
     * 通过bpmn名称发布流程,如果已经上传了xml,该接口通过xml名称发布流程,并返回状态
     *
     * @param fileName xml文件名
     * @return String
     */
    public String publishXml(String fileName) {
        try {
            File file = new File(UPLOAD_DIR + fileName + ".xml");
            // 部署流程定义
            Deployment deployment = repositoryService.createDeployment()
                    .addInputStream(file.getName(), Files.newInputStream(file.toPath()))
                    .deploy();
            return deployment.getId();
        } catch (Exception e) {
            log.error("xml发布失败,原因: " + e.getMessage());
            return null;
        }
    }

    /**
     * 查看当前可用流程列表
     *
     * @return ProcessInstanceVo
     */
    public Page<ProcessInstanceVo> getProcessList(FlowableRequest flowableRequest) {
        Page<ProcessInstanceVo> page = new Page<>();
        try {
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().active().latestVersion().list();

            if (!list.isEmpty()) {
                page = BizService.createPage(flowableRequest, list, ProcessInstanceVo.class);
            }

            return page;
        } catch (Exception e) {
            log.error("查看当前可用流程列表失败,原因: " + e.getMessage());
            return null;
        }
    }

    /**
     * 查看所有流程列表
     *
     * @return ProcessInstanceVo
     */
    public Page<ProcessInstanceVo> getAllProcessList(FlowableRequest flowableRequest) {
        Page<ProcessInstanceVo> page = new Page<>();
        try {
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
            if (!list.isEmpty()) {
                page = BizService.createPage(flowableRequest, list, ProcessInstanceVo.class);
            }
            return page;
        } catch (Exception e) {
            log.error("查看所有流程列表,原因: " + e.getMessage());
            return null;
        }
    }

    /**
     * 查询所有用户
     *
     * @return List<User>
     */
    public Page<UserVo> getUserList(FlowableRequest flowableRequest) {
        Page<UserVo> page = new Page<>();
        try {
            List<User> list = identityService.createUserQuery().list();
            if (!list.isEmpty()) {
                page = BizService.createPage(flowableRequest, list, UserVo.class);
            }
        } catch (Exception e) {
            log.error("用户列表查询失败: 原因: " + e.getMessage());
        }
        return page;
    }

    /**
     * 提交流程
     *
     * @param flowableRequest 流程key
     * @return String
     */
    public String submitProcess(FlowableRequest flowableRequest) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("initiator", flowableRequest.getAssignee());
            ProcessInstance instance = runtimeService.startProcessInstanceById(flowableRequest.getProcessInstanceId(), variables);
            return instance.getId();
        } catch (Exception e) {
            log.error("提交流程失败,原因: " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取用户任务列表
     *
     * @param flowableRequest 用户id
     * @return String
     */
    public Page<TaskVo> getUserTaskList(FlowableRequest flowableRequest) {
        Page<TaskVo> page = new Page<>();
        try {
            List<Task> list = taskService.createTaskQuery()
                    .taskAssignee(flowableRequest.getUserId())  // 查询分配给指定用户的任务
                    .orderByTaskCreateTime()      // 可选：按创建时间排序
                    .asc().list();

            if (!list.isEmpty()) {
                page = BizService.createPage(flowableRequest, list, TaskVo.class);
            }
            return page;
        } catch (Exception e) {
            log.error("提交流程失败,原因: " + e.getMessage());
            return null;
        }
    }

    /**
     * 删除所有流程模型应用包
     */
    public boolean deleteAllMode() {
        boolean result = false;
        try {
//            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
            List<ProcessDefinition> processDefinitions = new ArrayList<>(repositoryService.createProcessDefinitionQuery().list().stream()
                    .collect(Collectors.toMap(
                            ProcessDefinition::getDeploymentId, // Key: 使用ID作为键
                            person -> person, // Value: 整个对象
                            (existing, replacement) -> existing // 解决冲突策略：保留第一个出现的对象
                    ))
                    .values());

            if (processDefinitions.isEmpty()) {
                log.error("没有找到任何流程定义");
            } else {
                for (ProcessDefinition processDefinition : processDefinitions) {

                    try {
                        // 使用命令模式删除流程定义
                        repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
                        log.info("成功删除流程定义: " + processDefinition.getDeploymentId());
                    } catch (Exception e) {
                        log.info("删除流程定义: " + processDefinition.getDeploymentId() + " 时出错: " + e.getMessage());
                    }
                }
                result = true;
            }

        } catch (Exception e) {
            log.error("删除所有流程模型应用包失败,原因: " + e.getMessage());
        }
        return result;
    }

    /**
     * 根据任务ID获取流程实例表单属性。
     *
     * @param flowableRequest 任务ID
     * @return TaskFormData
     */
    public HistoryVo getTaskForm(FlowableRequest flowableRequest) {
        HistoryVo history = new HistoryVo();
        List<HistoricVariableInstanceVo> HistoricVariableInstanceVos = new ArrayList<>();
        try {
            List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(flowableRequest.getProcessInstanceId()).excludeTaskVariables().list();
            for (HistoricVariableInstance historicVariableInstance : list) {
                HistoricVariableInstanceVo historicVariableInstanceVo = new HistoricVariableInstanceVo();
                BeanUtils.copyProperties(historicVariableInstance, historicVariableInstanceVo);
                HistoricVariableInstanceVos.add(historicVariableInstanceVo);
            }
            QueryWrapper<GdAdvice> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("process_instance_id", flowableRequest.getProcessInstanceId());
            List<GdAdvice> gdAdvices = gdAdviceMapper.selectList(queryWrapper);
            history.setGdAdvices(gdAdvices);
            history.setHistoricVariableInstanceVos(HistoricVariableInstanceVos);
        } catch (Exception e) {
            log.error("查询流程表单异常,原因: " + e.getMessage());
        }
        return history;
    }

    /**
     * 上传意见附件
     *
     * @return 表单模型列表
     */
    public String uploadAdviceFile(MultipartFile file) {
        String result = "";
        try {
            if (file.isEmpty()) {
                log.error("bpmn为空");
                return null;
            }
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            // 构建目标文件路径
            Path targetLocation = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            // 将文件保存到本地磁盘
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();
        } catch (Exception e) {
            log.error("上传意见附件失败,原因: " + e.getMessage());
        }
        return result;
    }

    /**
     * 判断给定的执行对象的下一个节点是否为多实例会签节点。
     *
     * @param taskId 任务id
     * @return 如果下一个节点是多实例会签节点，则返回true；否则返回false。
     */
    public String isNextNodeMultiInstanceParallel(String taskId) {

        // 获取任务对象
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return null; // 任务不存在
        }

        // 获取任务关联的执行对象
        Execution execution = runtimeService.createExecutionQuery()
                .executionId(task.getExecutionId())
                .singleResult();

        if (execution == null) {
            return null; // 执行对象不存在
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        if (bpmnModel == null) {
            return null; // BPMN模型不存在
        }
        // 获取当前活动的流出序列流
        FlowElement currentElement = bpmnModel.getMainProcess().getFlowElement(execution.getActivityId());
        List<SequenceFlow> outgoingFlows = ((FlowNode) currentElement).getOutgoingFlows();

        // 检查每个流出序列流的目标节点是否为多实例会签节点
        for (SequenceFlow flow : outgoingFlows) {
            FlowElement targetElement = bpmnModel.getMainProcess().getFlowElement(flow.getTargetRef());
            if (targetElement instanceof UserTask) {
                UserTask userTask = (UserTask) targetElement;
                MultiInstanceLoopCharacteristics miProps = userTask.getLoopCharacteristics();
                if (miProps != null && !miProps.isSequential()) { // 并行多实例
                    return miProps.getCompletionCondition();
                }
            }
        }
        return null;
    }

    public void notify(DelegateTask delegateTask, int code) {
        // 查询用户信息
        User user = identityService.createUserQuery().userId(delegateTask.getAssignee()).singleResult();
        // 获取与当前任务关联的执行上下文
        Object objectResult = delegateTask.getVariable("result");
        Object filePath = delegateTask.getVariable("filePath");
        Object description = delegateTask.getVariable("description");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beforeResult = null;
        if (objectResult != null) {
            beforeResult = objectResult.toString();
        }
        GdAdvice gdAdvice = new GdAdvice();
        if (user != null) {
            gdAdvice.setId(UUID.randomUUID().toString());
            gdAdvice.setTaskId(delegateTask.getId());
            gdAdvice.setProcessInstanceId(delegateTask.getProcessInstanceId());
            gdAdvice.setCreateId(user.getId());
            gdAdvice.setCreateBy(user.getFirstName() + user.getLastName());
            gdAdvice.setCreateTime(simpleDateFormat.format(new Date()));
            if (code == 0) {
                gdAdvice.setResult(beforeResult);
            } else {
                gdAdvice.setResult(beforeResult);
                gdAdvice.setBeforeResult(beforeResult);
            }

            gdAdvice.setFilePath(filePath != null ? filePath.toString() : null);
            gdAdvice.setDescription(description != null ? description.toString() : null);
        } else {
            throw new RuntimeException("该用户不存在");
        }
        gdAdviceMapper.insert(gdAdvice);
    }

    /**
     * 处理流程
     *
     * @param taskRequestVo 任务
     */
    @Transactional
    public boolean processUserTask(TaskRequestVo taskRequestVo) {
        boolean result = false;
        try {
            String taskId = taskRequestVo.getTaskId();
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task != null) {
                Map<String, String> variables = setVariables(taskRequestVo);
                formService.submitTaskFormData(task.getId(), variables);
                result = true;
            } else {
                log.error("未找到指定的任务");
            }
        } catch (Exception e) {
            log.error("提交流程失败,原因: " + e.getMessage());
        }
        return result;
    }

    /**
     * 讲不为空的属性存入map
     *
     * @param taskRequestVo 任务
     */
    public Map<String, String> setVariables(TaskRequestVo taskRequestVo) {
        Map<String, String> variables = new HashMap<>();
        String processResult = taskRequestVo.getResult();
        String assignee = taskRequestVo.getAssignee();
        String description = taskRequestVo.getDescription();
        String filePath = taskRequestVo.getFilePath();
        if (processResult != null && !processResult.isEmpty()) {
            variables.put("result", processResult);
        }
        if (assignee != null && !assignee.isEmpty()) {
            variables.put("assignee", assignee);
        }
        if (description != null && !description.isEmpty()) {
            variables.put("description", description);
        }
        if (filePath != null && !filePath.isEmpty()) {
            variables.put("filePath", filePath);
        }
        Map<String, String> form = null;
        if (taskRequestVo.getFormVo() != null) {
            form = taskRequestVo.getFormVo().toMap();
        }
        if (form != null && !form.isEmpty()) {
            variables.putAll(form);
        }
        return variables;
    }

    /**
     * 处理多实例节点
     *
     * @param taskRequestVo 多实例用户集合
     * @return boolean
     */
    public boolean processMultiInstanceTask(TaskRequestVo taskRequestVo) {
        boolean result = false;
        Map<String, Object> varivales = new HashMap<>();
        String processResult = taskRequestVo.getResult();
        varivales.put("assigneeList", taskRequestVo.getAssigneeList());
        if (processResult != null && !processResult.isEmpty()) {
            varivales.put("result", processResult);
        }
        try {
            taskService.complete(taskRequestVo.getTaskId(), varivales);
            result = true;
        } catch (Exception e) {
            System.out.println("");
        }
        return result;
    }

    /**
     * 获取上一节点结果
     *
     * @param flowableRequest 流程实例id
     * @return GdAdvice
     */
    public GdAdvice getGdAdviceListByProcessInstanceId(FlowableRequest flowableRequest) {
        QueryWrapper<GdAdvice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_instance_id", flowableRequest.getProcessInstanceId());
        queryWrapper.orderByDesc("create_time");
        List<GdAdvice> gdAdvices = gdAdviceMapper.selectList(queryWrapper);
        if (!gdAdvices.isEmpty()) {
            return gdAdvices.get(0);
        } else {
            return null;
        }
    }


    /**
     * 获取流程过程图
     */
    public InputStream diagram(String processId) {
        String processDefinitionId;
        // 获取当前的流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        // 如果流程已经结束，则得到结束节点
        if (Objects.isNull(processInstance)) {
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();

            processDefinitionId = pi.getProcessDefinitionId();
        } else {// 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        }

        // 获得活动的节点
        List<HistoricActivityInstance> highLightedFlowList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processId).orderByHistoricActivityInstanceStartTime().asc().list();

        List<String> highLightedFlows = new ArrayList<>();
        List<String> highLightedNodes = new ArrayList<>();
        //高亮线
        for (HistoricActivityInstance tempActivity : highLightedFlowList) {
            if ("sequenceFlow".equals(tempActivity.getActivityType())) {
                //高亮线
                highLightedFlows.add(tempActivity.getActivityId());
            } else {
                //高亮节点
                highLightedNodes.add(tempActivity.getActivityId());
            }
        }


        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();
        //获取自定义图片生成器
        configuration.setActivityFontName("SimSun");
        configuration.setLabelFontName("SimSun");
        configuration.setAnnotationFontName("SimSun");
        ProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        return diagramGenerator.generateDiagram(bpmnModel, "png", highLightedNodes, highLightedFlows, configuration.getActivityFontName(),
                configuration.getLabelFontName(), configuration.getAnnotationFontName(), configuration.getClassLoader(), 1.0, true);
    }

}
