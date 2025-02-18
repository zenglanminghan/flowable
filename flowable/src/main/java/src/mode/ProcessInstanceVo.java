package src.mode;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import liquibase.pro.packaged.S;
import lombok.Data;
import org.flowable.identitylink.service.impl.persistence.entity.IdentityLinkEntity;

import java.util.List;
import java.util.Map;

@Data
public class ProcessInstanceVo {
    @Schema(description = "实体的唯一标识符")
    protected String id;

    @Schema(description = "实体的修订版本号，默认为1")
    protected int revision = 1;

    @Schema(description = "指示实体是否已被插入数据库")
    @JsonProperty("isInserted")
    protected boolean isInserted;

    @Schema(description = "指示实体是否已被更新")
    @JsonProperty("isUpdated")
    protected boolean isUpdated;

    @Schema(description = "指示实体是否已被删除")
    @JsonProperty("isDeleted")
    protected boolean isDeleted;

    @Schema(description = "实体的原始持久化状态")
    protected Object originalPersistentState;

    @Schema(description = "流程定义的名称")
    protected String name;

    @Schema(description = "本地化的流程定义名称")
    protected String localizedName;

    @Schema(description = "流程定义的描述")
    protected String description;

    @Schema(description = "本地化的流程定义描述")
    protected String localizedDescription;

    @Schema(description = "流程定义的键")
    protected String key;

    @Schema(description = "流程定义的版本号")
    protected int version;

    @Schema(description = "流程定义的类别")
    protected String category;

    @Schema(description = "流程定义的部署ID")
    protected String deploymentId;

    @Schema(description = "流程定义的资源名称")
    protected String resourceName;

    @Schema(description = "租户ID")
    protected String tenantId = "";

    @Schema(description = "历史级别")
    protected Integer historyLevel;

    @Schema(description = "流程定义图的资源名称")
    protected String diagramResourceName;

    @Schema(description = "是否定义了图形符号")
    @JsonProperty("isGraphicalNotationDefined")
    protected boolean isGraphicalNotationDefined;

    @Schema(description = "变量映射")
    protected Map<String, Object> variables;

    @Schema(description = "是否有开始表单键")
    @JsonProperty("hasStartFormKey")
    protected boolean hasStartFormKey;

    @Schema(description = "挂起状态")
    protected int suspensionState;

    @Schema(description = "身份链接是否已初始化")
    @JsonProperty("isIdentityLinksInitialized")
    protected boolean isIdentityLinksInitialized;

    @Schema(description = "流程定义的身份链接实体列表")
    protected List<IdentityLinkEntity> definitionIdentityLinkEntities;

    @Schema(description = "派生自的流程定义ID")
    protected String derivedFrom;

    @Schema(description = "根派生自的流程定义ID")
    protected String derivedFromRoot;

    @Schema(description = "派生版本号")
    protected int derivedVersion;

    @Schema(description = "引擎版本")
    protected String engineVersion;
}
