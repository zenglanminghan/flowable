package src.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import liquibase.pro.packaged.T;
import org.flowable.idm.api.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import src.mode.FlowableRequest;
import src.mode.PageParam;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BizService {


    /**
     * 根据分页参数和API调用结果创建分页对象。
     *
     * @param pageParam 分页参数
     * @param list      API调用返回的数据列表
     * @param <T>       数据类型
     * @return 封装了分页信息的 Page 对象
     */
    public static <T, V> Page<T> createPage(PageParam pageParam, List<V> list, Class<T> targetClass) {
        // 创建分页对象
        Page<T> page = new Page<>();
        page.setCurrent(pageParam.getCurrent());
        page.setSize(pageParam.getSize());

        if (list == null || list.isEmpty()) {
            // 如果没有数据，设置为空列表
            page.setRecords(new ArrayList<>());
            page.setTotal(0L);
            return page;
        }

        // 计算总记录数
        long total = list.size();
        page.setTotal(total);

        // 计算起始索引
        int start = (pageParam.getCurrent() - 1) * pageParam.getSize();
        int end = Math.min(start + pageParam.getSize(), (int) total);

        // 检查是否请求的页码超出了范围
        if (start >= total) {
            // 请求的页码超出范围，返回空结果集
            page.setRecords(new ArrayList<>());
            return page;
        }

        // 获取分页后的数据
        List<T> records = new ArrayList<>();
        for (int i = start; i < end; i++) {
            try {
                T target = targetClass.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(list.get(i),target);
                records.add(target);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException("Failed to copy properties", e);
            }
        }

        page.setRecords(records);
        return page;
    }


}
