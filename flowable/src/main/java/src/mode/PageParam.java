package src.mode;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import liquibase.pro.packaged.T;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageParam {
    @Schema(description = "当前页码", required = true)
    private int current;
    @Schema(description = "每页显示的记录数", required = true)
    private int size;
}
