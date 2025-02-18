package src.dao.entry;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.flowable.engine.impl.el.FixedValue;

import java.io.Serializable;

@Data
public class GdAdvice implements Serializable {
    @Schema(description = "id", required = false)
    private String id;

    @Schema(description = "文件路径", required = true)
    private String filePath;

    @Schema(description = "描述信息", required = false)
    private String description;

    @Schema(description = "处理结果,同意0,不同意1", required = false)
    private String result;

    @Schema(description = "任务ID", required = false)
    private String taskId;

    @Schema(description = "流程实例ID", required = false)
    private String processInstanceId;

    @Schema(description = "创建者ID", required = false)
    private String createId;

    @Schema(description = "创建人", required = false)
    private String createBy;

    @Schema(description = "创建时间", required = false)
    private String createTime;

    @Schema(description = "上一节点结果", required = false)
    private String beforeResult;


}
