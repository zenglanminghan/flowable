package src.mode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProcessRequestVo {
    @Schema(description = "流程id,调用流程相关时必传,默认0", required = true, defaultValue = "0")
    private String processId = "0";
    @Schema(description = "用户id,调用用户相关时必传,默认0", required = true, defaultValue = "0")
    private String userId = "0";
    @Schema(description = "任务id,调用任务相关时必传,默认0", required = true, defaultValue = "0")
    private String taskId = "0";
    @Schema(description = "任务处理人", required = true)
    private String assignee;
}
