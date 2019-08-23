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

    private static List basicData;

    private static Map<String, Map<String, Object>> dataMap;

    /**
     * 获取数据
     *
     * @return
     */
    protected abstract List<T> getBasicData();

    public T getData(String fieldType, String primaryKey) throws IllegalAccessException, NoSuchFieldException {
        Map<String, Object> data = null;
        synchronized (this.getClass()) {
            if (basicData == null) {
                initData();
            }
            data = dataMap.get(fieldType);
        }
        if (data == null) {
            data = new ConcurrentHashMap<String, Object>();
            Field field = basicData.get(0).getClass().getDeclaredField(fieldType);
            field.setAccessible(true);
            for (Object basicDatum : basicData) {
                String key = field.get(basicDatum).toString();
                data.put(key, basicDatum);
            }
            dataMap.put(fieldType,data);
        }
        return (T)data.get(primaryKey);
    }

    private void initData() {
        basicData = getBasicData();
    }

    public synchronized void importData(List<T> basicData) {
        this.basicData = basicData;
        dataMap = new ConcurrentHashMap<String, Map<String, Object>>();

    }
}
