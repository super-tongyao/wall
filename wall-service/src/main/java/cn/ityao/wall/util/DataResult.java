package cn.ityao.wall.util;

import lombok.Data;

/**
 * 数据结果返回类
 *
 * @version 2.0
 * @author tongyao
 * @since 2021-08-01
 */
@Data
public class DataResult {

    private int code = 200;
    private String message = "success";
    private Object data = null;

    public DataResult() {

    }

    public DataResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public DataResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 设置状态为200的返回结果数据
     * @param data
     * @return
     */
    public static DataResult setResult(Object data) {
        DataResult dataResult = new DataResult();
        dataResult.setData(data);
        return dataResult;
    }

    /**
     * 设置自定义状态的返回结果数据
     * @param data
     * @return
     */
    public static DataResult setResult(int code, Object data) {
        DataResult dataResult = new DataResult();
        dataResult.setCode(code);
        dataResult.setData(data);
        return dataResult;
    }

    /**
     * 设置自定义状态和返回信息返回结果数据
     * @param data
     * @return
     */
    public static DataResult setResult(int code, String message, Object data) {
        DataResult dataResult = new DataResult();
        dataResult.setCode(code);
        dataResult.setMessage(message);
        dataResult.setData(data);
        return dataResult;
    }

    /**
     * 设置自定义状态和返回信息内容，但无结果数据
     * @param code
     * @param message
     * @return
     */
    public static DataResult setResult(int code, String message) {
        DataResult dataResult = new DataResult();
        dataResult.setCode(code);
        dataResult.setMessage(message);
        return dataResult;
    }

    /**
     * 设置默认200状态，自定义返回信息和结果数据
     * @param data
     * @return
     */
    public static DataResult setResult(String message, Object data) {
        DataResult dataResult = new DataResult();
        dataResult.setMessage(message);
        dataResult.setData(data);
        return dataResult;
    }

    /**
     * 默认返回错误方法，自定义返回错误信息
     * @param message
     * @return
     */
    public static DataResult setError(String message) {
        DataResult dataResult = new DataResult();
        dataResult.setCode(500);
        dataResult.setMessage(message);
        return dataResult;
    }

    /**
     * 默认返回成功方法，自定义返回结果数据
     * @param data
     * @return
     */
    public static DataResult setSuccess(Object data) {
        DataResult dataResult = new DataResult();
        dataResult.setData(data);
        return dataResult;
    }

}
