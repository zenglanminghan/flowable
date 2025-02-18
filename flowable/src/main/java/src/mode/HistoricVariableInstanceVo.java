package src.mode;

import io.swagger.v3.oas.annotations.media.Schema;
import liquibase.pro.packaged.S;
import lombok.Data;
import src.dao.entry.GdAdvice;

import java.util.List;

@Data
public class HistoricVariableInstanceVo {
    @Schema(description = "变量名")
    private String variableName;
//    @Schema(description = "变量值")
//    private String value;
    @Schema(description = "变量值")
    private String textValue;
    @Schema(description = "变量值")
    private String textValue2;
    @Schema(description = "流程实例id")
    private String processInstanceId;
}
