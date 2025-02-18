package src.mode;

import io.swagger.v3.oas.annotations.media.Schema;
import liquibase.pro.packaged.S;
import lombok.Data;

@Data
public class UserVo {
    @Schema(description = "id",required = true)
    private String id;

    @Schema(description = "密码",required = true)
    private String password;

    @Schema(description = "名称",required = true)
    private String firstName;

    @Schema(description = "姓氏",required = true)
    private String lastName;

    @Schema(description = "显示名称",required = true)
    private String displayName;

    @Schema(description = "电子邮件地址",required = true)
    private String email;

    @Schema(description = "租户ID",required = false)
    private String tenantId;
}
