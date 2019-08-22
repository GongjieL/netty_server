package com.gjie.netty.cache;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author j_gong
 * @date 2019/8/22 7:13 PM
 */
public abstract class BaseCache<T> {
    private List<T> basicData;

    private Map<String, Map<String, T>> dataMap;
    /**
     * 获取数据
     *
     * @return
     */
    protected abstract List<T> getBasicData();

    public T getData(String fieldType, String primaryKey) throws IllegalAccessException, NoSuchFieldException {
        if (basicData == null) {
            initData();
        }
        Map<String, T> data = dataMap.get(fieldType);
        if (data == null) {
            data = new ConcurrentHashMap<String, T>();
            for (T basicDatum : basicData) {
                Field field = basicDatum.getClass().getField(fieldType);
                String key = field.get(basicDatum).toString();
                data.put(key,basicDatum);
            }
        }
        return data.get(primaryKey);
    }

    private void initData() {
        basicData = getBasicData();
        dataMap = new ConcurrentHashMap<>();
    }
}
