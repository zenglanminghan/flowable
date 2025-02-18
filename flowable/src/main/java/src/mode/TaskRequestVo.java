package src.mode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import src.dao.entry.GdAdvice;

import java.util.List;

@Data
public class TaskRequestVo {
    @Schema(description = "任务ID", required = true)
    private String taskId;

    @Schema(description = "流程实例ID", required = false)
    private String processInstanceId;

    @Schema(description = "下一节点处理人id,下一节点非多实例节点时必传", required = false)
    private String assignee;

    @Schema(description = "下一节点处理人id,下一节点多实例节点时必传", required = false)
    private List<String> assigneeList;

    @Schema(description = "处理结果,打回节点必传,同意0,不同意1", required = false)
    private String result;

    @Schema(description = "表单对象,没有表单时就不传", required = false)
    private FormVo formVo;

    @Schema(description = "文件路径", required = false)
    private String filePath;

    @Schema(description = "描述信息", required = false)
    private String description;
}
