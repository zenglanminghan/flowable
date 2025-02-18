package src.mode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FlowableRequest extends PageParam {
    @Schema(description = "用户id,调用用户相关时必传,默认0", required = false, defaultValue = "0")
    private String userId = "0";
    @Schema(description = "流程id,调用流程相关时必传,默认0", required = false, defaultValue = "0")
    private String processInstanceId = "0";
    @Schema(description = "任务id,调用任务相关时必传", required = false)
    private String taskId;
    @Schema(description = "任务处理人,处理人id", required = false)
    private String assignee;
}
