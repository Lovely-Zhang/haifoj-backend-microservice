package com.haif.haifojcommon.common;

/**
 * 自定义错误码
 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败"),
    API_REQUEST_ERROR(50010, "接口调用失败"),

    /**
     * OSS
     */
    OSS_NOT_EXIST(80101, "OSS未配置"),
    OSS_EXCEPTION_ERROR(80102, "文件上传失败，请稍后重试"),
    OSS_DELETE_ERROR(80103, "删除失败"),
    OSS_DOWNLOAD_ERROR(80104, "下载失败"),


    IMAGE_FILE_EXT_ERROR(1005, "不支持图片格式"),
    FILE_TYPE_NOT_SUPPORT(1010, "不支持上传的文件类型！"),
    FILE_NOT_EXIST_ERROR(1011, "上传文件不能为空");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
