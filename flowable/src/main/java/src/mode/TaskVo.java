package src.mode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import liquibase.pro.packaged.S;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.identitylink.service.impl.persistence.entity.IdentityLinkEntity;
import org.flowable.task.api.DelegationState;
import org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntity;

import java.util.Date;
import java.util.List;

@Data
public class TaskVo {
    @Schema(description = "任务id")
    private String id;
    @Schema(description = "任务的所有者")
    private String owner;

    @Schema(description = "分配人更新次数")
    private int assigneeUpdatedCount;

    @Schema(description = "原始分配人")
    private String originalAssignee;

    @Schema(description = "当前分配人")
    private String assignee;

    @Schema(description = "委托状态")
    private DelegationState delegationState;

    @Schema(description = "父任务ID")
    private String parentTaskId;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "本地化的任务名称")
    private String localizedName;

    @Schema(description = "任务描述")
    private String description;

    @Schema(description = "本地化的任务描述")
    private String localizedDescription;

    @Schema(description = "优先级", example = "50")
    private int priority = 50;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "到期时间")
    private Date dueDate;

    @Schema(description = "挂起状态")
    private int suspensionState;

    @Schema(description = "任务类别")
    private String category;

    @Schema(description = "身份链接是否已初始化")
    @JsonProperty("isIdentityLinksInitialized")
    private boolean isIdentityLinksInitialized;

    @Schema(description = "任务的身份链接实体列表")
    private List<IdentityLinkEntity> taskIdentityLinkEntities;

    @Schema(description = "执行实例ID")
    private String executionId;

    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "流程定义ID")
    private String processDefinitionId;

    @Schema(description = "任务定义ID")
    private String taskDefinitionId;

    @Schema(description = "作用域ID")
    private String scopeId;

    @Schema(description = "子作用域ID")
    private String subScopeId;

    @Schema(description = "作用域类型")
    private String scopeType;

    @Schema(description = "作用域定义ID")
    private String scopeDefinitionId;

    @Schema(description = "传播的阶段实例ID")
    private String propagatedStageInstanceId;

    @Schema(description = "任务定义键")
    private String taskDefinitionKey;

    @Schema(description = "表单键")
    private String formKey;

    @Schema(description = "任务是否已取消")
    @JsonProperty("isCanceled")
    private boolean canceled;

    @Schema(description = "是否启用计数")
    @JsonProperty("isCountEnabled")
    private boolean isCountEnabled;

    @Schema(description = "变量数量")
    private int variableCount;

    @Schema(description = "身份链接数量")
    private int identityLinkCount;

    @Schema(description = "子任务数量")
    private int subTaskCount;

    @Schema(description = "认领时间")
    private Date claimTime;

    @Schema(description = "租户ID", example = "tenant-id-909")
    private String tenantId;

    @Schema(description = "事件名称")
    private String eventName;

    @Schema(description = "事件处理器ID")
    private String eventHandlerId;

    @Schema(description = "查询变量列表")
    private List<VariableInstanceEntity> queryVariables;

    @Schema(description = "查询身份链接列表")
    private List<IdentityLinkEntity> queryIdentityLinks;

    @Schema(description = "是否强制更新")
    private boolean forcedUpdate;
}
