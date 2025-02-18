package src.mode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VariablesVo {
    @Schema(description = "处理结果,打回节点必传,同意0,不同意1", required = false)
    private String result;
}
