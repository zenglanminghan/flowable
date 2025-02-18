package src.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import src.dao.entry.GdAdvice;
import src.mode.*;
import src.service.ProcessService;

@RestController
@RequestMapping("/api")
@Api(tags = "flowable相关")
public class ProcessController {
    @Autowired
    private ProcessService processService;

    @PostMapping("/saveUser")
    @ApiImplicitParam(name = "flowableUser", value = "用户对象", required = true)
    @ApiOperation(value = "1.新增用户,需要新增用户时调用")
    public FlowableResponse<Boolean> saveUser(@RequestBody UserVo userVo) {
        Boolean result = processService.saveUser(userVo);
        if (result) {
            return FlowableResponse.success(true);
        } else {
            return FlowableResponse.error(false);
        }
    }

    @PostMapping("/saveXmlAndPublish")
    @ApiOperation(value = "2.上传bpmn并发布流程,此接口用于保存bpmn.xml并发布流程,并返回状态")
    public ResponseEntity<String> saveXmlAndPublish(@RequestPart("file") MultipartFile file) {
        String deploymentId = processService.saveXmlAndPublish(file);
        if (deploymentId != null) {
            return ResponseEntity.ok("xml发布成功,id为: " + deploymentId);
        } else {
            return ResponseEntity.ok("xml发布失败");
        }
    }

    @PostMapping("/getProcessList")
    @ApiOperation(value = "3.查看当前可用流程列表")
    public FlowableResponse<Page<ProcessInstanceVo>> getProcessList(@RequestBody FlowableRequest flowableRequest) {
        Page<ProcessInstanceVo> processList = processService.getProcessList(flowableRequest);
        if (processList != null) {
            return FlowableResponse.success(processList);
        } else {
            return FlowableResponse.error("查看当前可用流程列表失败");
        }
    }

    @PostMapping("/getAllProcessList")
    @ApiOperation(value = "4.查看所有流程列表")
    public FlowableResponse<Page<ProcessInstanceVo>> getAllProcessList(@RequestBody FlowableRequest flowableRequest) {
        Page<ProcessInstanceVo> processList = processService.getAllProcessList(flowableRequest);
        if (processList != null) {
            return FlowableResponse.success(processList);
        } else {
            return FlowableResponse.error("查看所有流程列表失败");
        }
    }

    @PostMapping("/getUserList")
    @ApiOperation(value = "5.获取用户列表")
    public FlowableResponse<Page<UserVo>> getUserList(@RequestBody FlowableRequest flowableRequest) {
        Page<UserVo> userList = processService.getUserList(flowableRequest);
        if (userList != null) {
            return FlowableResponse.success(userList);
        } else {
            return FlowableResponse.error("获取用户列表失败");
        }
    }

    @PostMapping("/submitProcess")
    @ApiOperation(value = "6.提交流程,用于启动一个流程")
    public FlowableResponse<String> submitProcess(@RequestBody FlowableRequest flowableRequest) {
        String instanceId = processService.submitProcess(flowableRequest);
        if (instanceId != null) {
            return FlowableResponse.success("流程发布成功,id为: " + instanceId);
        } else {
            return FlowableResponse.error("流程发布异常");
        }
    }

    @PostMapping("/getUserTaskList")
    @ApiOperation(value = "7.获取用户任务列表,处理任务前调用,用于获取任务id")
    public FlowableResponse<Page<TaskVo>> getUserTaskList(@RequestBody FlowableRequest processVo) {
        Page<TaskVo> userTaskList = processService.getUserTaskList(processVo);
        if (userTaskList != null) {
            return FlowableResponse.success(userTaskList);
        } else {
            return FlowableResponse.error(null);
        }
    }

    @PostMapping("/deleteAllMode")
    @ApiOperation(value = "8.删除所有流程模型应用包")
    public FlowableResponse<Boolean> deleteMode() {
        boolean result = processService.deleteAllMode();
        if (result) {
            return FlowableResponse.success(result);
        } else {
            return FlowableResponse.error(result);
        }
    }

    @PostMapping("/getTaskForm")
    @ApiOperation(value = "9.查询流程实例表单信息,传流程实例id,用于后续需要查看表单信息及各节点审批意见等")
    public FlowableResponse<HistoryVo> getTaskForm(@RequestBody FlowableRequest flowableRequest) {
        HistoryVo historyVo = processService.getTaskForm(flowableRequest);
        if (historyVo != null) {
            return FlowableResponse.success(historyVo);
        } else {
            return FlowableResponse.error("表单查询失败");
        }
    }

    @PostMapping("/uploadAdviceFile")
    @ApiOperation(value = "10.上传意见附件")
    public FlowableResponse<String> uploadAdviceFile(@RequestPart("file") MultipartFile file) {
        String result = processService.uploadAdviceFile(file);
        if (result != null) {
            return FlowableResponse.success(result);
        } else {
            return FlowableResponse.error("上传意见附件失败");
        }
    }


    @PostMapping("/processUserTask")
    @ApiOperation(value = "12.用户任务处理,用于任务节点,传任务id")
    public FlowableResponse<Boolean> processUserTask(@RequestBody TaskRequestVo taskRequestVo) {
        boolean result = processService.processUserTask(taskRequestVo);
        if (result) {
            return FlowableResponse.success(result);
        } else {
            return FlowableResponse.error(result);
        }
    }

    @PostMapping("/processMultiInstanceTask")
    @ApiOperation(value = "13.处理多实例节点,isNextNodeMultiInstanceParallel接口返回为true时调用")
    public FlowableResponse<Boolean> processMultiInstanceTask(@RequestBody TaskRequestVo taskRequestVo) {
        boolean result = processService.processMultiInstanceTask(taskRequestVo);
        if (result) {
            return FlowableResponse.success(result);
        } else {
            return FlowableResponse.error(result);
        }
    }

    @PostMapping("/getGdAdviceListByProcessInstanceId")
    @ApiOperation(value = "14.获取上一节点结果,每次任务处理前调用,如果beforeResult属性不为空则下一个节点的result值传beforeResult的值")
    public FlowableResponse<GdAdvice> getGdAdviceListByProcessInstanceId(@RequestBody FlowableRequest flowableRequest) {
        return FlowableResponse.success(processService.getGdAdviceListByProcessInstanceId(flowableRequest));
    }

    @PostMapping("/isNextNodeMultiInstanceParallel")
    @ApiOperation(value = "11.查看下个节点是否为多实例节点,任务处理前调用,用于判断下个节点是否为多实例,为true时调用多实例接口处理任务,为false时调用普通任务处理接口")
    public FlowableResponse<Boolean> isNextNodeMultiInstanceParallel(@RequestBody FlowableRequest flowableRequest) {
        String nextNodeMultiInstanceParallel = processService.isNextNodeMultiInstanceParallel(flowableRequest.getTaskId());
        if (nextNodeMultiInstanceParallel!=null) {
            return FlowableResponse.success(true);
        } else {
            return FlowableResponse.success(false);
        }
    }

    @PostMapping("/isNextNodeMultiInstanceParallelSingle")
    @ApiOperation(value = "15.查看多实例节点是否为抢签节点")
    public FlowableResponse<Boolean> isNextNodeMultiInstanceParallelSingle(@RequestBody FlowableRequest flowableRequest) {
        String nextNodeMultiInstanceParallel = processService.isNextNodeMultiInstanceParallel(flowableRequest.getTaskId());
        if (nextNodeMultiInstanceParallel!=null) {
            return FlowableResponse.success(nextNodeMultiInstanceParallel.contains(">=1"));
        } else {
            return FlowableResponse.success(false);
        }
    }
}
