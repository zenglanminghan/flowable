package src.mode;

import io.swagger.v3.oas.annotations.media.Schema;
import liquibase.pro.packaged.L;
import lombok.Data;
import src.dao.entry.GdAdvice;

import java.util.List;

@Data
public class HistoryVo {
    @Schema(description = "表达属性集合")
    private List<HistoricVariableInstanceVo> HistoricVariableInstanceVos;
    @Schema(description = "意见集合")
    private List<GdAdvice> gdAdvices;
}
