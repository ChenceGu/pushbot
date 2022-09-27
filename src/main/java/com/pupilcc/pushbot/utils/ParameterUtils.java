package com.pupilcc.pushbot.utils;

import com.pupilcc.pushbot.entity.SendMessageDTO;
import com.pupilcc.pushbot.extension.ApiErrorCode;
import com.pupilcc.pushbot.extension.ApiResult;
import org.apache.commons.lang3.ObjectUtils;

public class ParameterUtils {
    public static ApiResult checkParams(SendMessageDTO dto){
        if (ObjectUtils.isEmpty(dto)){
            return ApiResult.failed(ApiErrorCode.PARAMETER_NULL);
        }
        return ApiResult.success();
    }


}
