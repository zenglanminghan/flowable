package src.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import src.service.ProcessService;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/bpmn")
@Api(tags = "流程图相关")
public class BpmnController {
    @Autowired
    private ProcessService processService;

    /**
     * 生成流程图
     *
     * @param processInstanceId 流程实例id
     */
    @GetMapping("/diagram/{processInstanceId}")
    @ApiOperation(value = "1.生成流程实例图")
    public void genProcessDiagram(HttpServletResponse response,
                                  @PathVariable("processInstanceId") String processInstanceId) {
        InputStream inputStream = processService.diagram(processInstanceId);
        OutputStream os = null;
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
            response.setContentType("image/png");
            os = response.getOutputStream();
            if (image != null) {
                ImageIO.write(image, "png", os);
            }
        } catch (Exception e) {
            throw new RuntimeException("流程图输出异常,原因: " + e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("流资源关闭异常,原因: " + e.getMessage());
            }
        }
    }
}
