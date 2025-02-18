package src.mode;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 固定返回格式
 */
@Data
@ToString(callSuper = true)

public class FlowableResponse<T> implements Serializable {

    /**
     * 响应码
     */
    @JsonProperty
    @Schema(description = "响应码", example = "200")
    private String code;

    /**
     * 提示信息
     */
    @Schema(description = "提示信息", example = "操作成功")
    private String message;

    /**
     * 具体的内容
     */
    @JsonProperty("body")
    @Schema(description = "响应体")
    private T body;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "200")
    private Integer status;

    /**
     * 私有化工具类 防止被实例化
     */
    private FlowableResponse() {
    }

    /**
     * 成功
     *
     * @param object 需要返回的数据
     * @return data
     */
    public static <T> FlowableResponse<T> success(T object) {
        FlowableResponse<T> result = new FlowableResponse<>();
        result.setBody(object);
        result.setCode("200");
        result.setMessage("操作成功");
        result.setStatus(200);
        return result;
    }

    /**
     * 成功, 自定义消息
     *
     * @param object 需要返回的数据
     * @param msg    自定义的消息
     * @return data
     */
    public static <T> FlowableResponse<T> success(T object, String msg) {
        FlowableResponse<T> result = success(object);
        result.setMessage(msg);
        return result;
    }

    /**
     * 错误
     *
     * @param code 状态码
     * @param msg  消息
     * @return ResultBean
     */
    public static <T> FlowableResponse<T> error(String code, String msg) {
        FlowableResponse<T> result = new FlowableResponse<>();
        result.setCode(code);
        result.setMessage(msg);
        result.setStatus(Integer.parseInt(code));
        return result;
    }

    /**
     * 错误, 默认500
     *
     * @param msg 错误信息
     * @return ResultBean
     */
    public static <T> FlowableResponse<T> error(String msg) {
        return error("500", msg);
    }

    public static <T> FlowableResponse<T> error(T object) {
        FlowableResponse<T> result = new FlowableResponse<>();
        result.setBody(object);
        result.setCode("500");
        result.setMessage("操作失败");
        result.setStatus(500);
        return result;
    }
}

