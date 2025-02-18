package src.mode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class FormVo {
    @Schema(description = "场景位置名称")
    private String sceneLocationName;
    @Schema(description = "统一社会信用代码")
    private String unifiedSocialCreditCode;
    @Schema(description = "企业全称")
    private String enterpriseFullName;
    @Schema(description = "法人姓名")
    private String legalPersonName;
    @Schema(description = "法人身份证号")
    private String legalPersonIdNumber;
    @Schema(description = "需协调单位")
    private String unitsToCoordinate;
    @Schema(description = "重点原创网络影视剧规划信息备案表")
    private String keyOriginalWebDramaPlanningInfoFilingForm;
    @Schema(description = "拍摄开始时间 ")
    private String shootingStartTime;
    @Schema(description = "拍摄结束时间")
    private String shootingEndTime;
    @Schema(description = "拍摄内容")
    private String shootingContent;
    @Schema(description = "拍摄场景实景图片")
    private String shootingSceneRealPictures;
    @Schema(description = "申报单位")
    private String declaringOrganization;
    @Schema(description = "申报节目名称")
    private String declaredProgramName;
    @Schema(description = "地区选择")
    private String regionSelection;
    @Schema(description = "申报节目类型")
    private String declaredProgramType;
    @Schema(description = "营业执照副本")
    private String businessLicenseCopy;
    @Schema(description = "广播电视节目制作经营许可证")
    private String broadcastingProgramProductionLicense;
    @Schema(description = "法人手机号(验证码验证)")
    private String legalPersonMobile;
    @Schema(description = "摄制组外联负责人身份证及委托书")
    private String filmingCrewExternalLiaisonManagerIdAndAuthorization;
    @Schema(description = "外联负责人姓名")
    private String externalLiaisonManagerName;
    @Schema(description = "制片单位法人身份证")
    private String productionCompanyLegalPersonId;
    @Schema(description = "制片单位营业执照")
    private String productionCompanyBusinessLicense;
    @Schema(description = "主创人员与主演信息")
    private String mainCreatorsAndActorsInfo;
    @Schema(description = "协调申请书")
    private String coordinationApplication;
    @Schema(description = "外联负责人联系方式")
    private String externalLiaisonManagerContact;
    @Schema(description = "完整剧本")
    private String completeScript;
    @Schema(description = "自律公约书")
    private String selfRegulationConvention;
    @Schema(description = "场景清单（文档导入）")
    private String sceneListImport;
    public Map<String, String> toMap() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY); // 忽略空值

        try {
            // 将对象序列化为Map
            return mapper.convertValue(this, LinkedHashMap.class);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error converting object to Map", e);
        }
    }
}
