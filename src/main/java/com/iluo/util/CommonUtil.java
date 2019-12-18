package com.iluo.util;

import com.alibaba.fastjson.JSONObject;
import com.iluo.util.constants.Constants;
import com.iluo.util.constants.ErrorEnum;


public class CommonUtil {
    /**
     * 返回一个returnData为空对象的成功消息的json
     *
     * @return
     */
    public static JSONObject successJson() {
        return successJson(new JSONObject());
    }

    /**
     * 返回一个返回码为200的json
     *
     * @param returnData json里的主要内容
     * @return
     */
    public static JSONObject successJson(Object returnData) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", Constants.SUCCESS_CODE);
        resultJson.put("msg", Constants.SUCCESS_MSG);
        resultJson.put("data", returnData);
        return resultJson;
    }

    /**
     * 返回错误信息JSON
     *
     * @param errorEnum 错误码的errorEnum
     * @return
     */
    public static JSONObject errorJson(ErrorEnum errorEnum) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", errorEnum.getErrorCode());
        resultJson.put("msg", errorEnum.getErrorMsg());
        resultJson.put("data", new JSONObject());
        return resultJson;
    }

    /**
     * 返回错误信息JSON
     *
     * @param errorMsg
     * @return
     */
    public static JSONObject errorJson(String errorMsg) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", Constants.ERROR_CODE);
        resultJson.put("msg", errorMsg);
        resultJson.put("data", new JSONObject());
        return resultJson;
    }
}
