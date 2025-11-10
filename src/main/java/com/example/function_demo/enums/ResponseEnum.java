package com.example.function_demo.enums;


import com.example.function_demo.common.conatants.ErrorCodeConstants;

public enum ResponseEnum {

    /*
        message的值，第一个单词首字母大写，其余单词首字母小写。
     */

    SUCCESS(ErrorCodeConstants.SUCCESS_CODE, "Success"),
    USER_NOT_EXIST(ErrorCodeConstants.USER_NOT_EXIST, "User not exist"),
    WRONG_PASSWORD(ErrorCodeConstants.WRONG_PASSWORD, "Wrong password"),

    MISSING_PARAM(ErrorCodeConstants.PARAM_EMPTY, "Missing necessary parameter"),
    USER_EXIST(ErrorCodeConstants.USER_EXIST, "User already existed"),
    USER_NOT_LOGIN(ErrorCodeConstants.USER_NOT_LOGIN, "User not login"),
    AUTH_FAILED(ErrorCodeConstants.AUTH_FAILED, "User auth fail"),
    AUTH_DENIED(ErrorCodeConstants.AUTH_DENIED, "User auth denied"),

    DATA_NOT_EXIST(ErrorCodeConstants.DATA_NOT_EXIST, "Data not exist"),
    RESULT_DATA_LESS_THAN_1(ErrorCodeConstants.RESULT_DATA_LESS_THAN_1, "Result data should be more than 1"),
    FILE_VERIFY_ERROR(ErrorCodeConstants.FILE_VERIFY_ERROR, "File verify error"),
    FOLDER_HAS_PROJECT(ErrorCodeConstants.FOLDER_HAS_PROJECT, "Folder has project"),
    DATA_EXIST_BIND(ErrorCodeConstants.DATA_EXIST_BIND, "Data exist bind_"),

    ADD_FAVORITE_FAIL(ErrorCodeConstants.ADD_FAVORITE_FAIL, "add favorite fail"),

    DATA_TOO_LONG(ErrorCodeConstants.DATA_TOO_LONG, "data exceeds the max length"),
    TABLE_NOT_EXIST(ErrorCodeConstants.TABLE_NOT_EXIST, "Table not config/exist"),

    PARAM_ERROR(ErrorCodeConstants.PARAM_ERROR, "Param error"),

    PARAM_EXCEED_SIZE(ErrorCodeConstants.PARAM_EXCEED_SIZE, "Param exceed max size"),
    FILE_PARSE_ERROR(ErrorCodeConstants.FILE_PARSE_ERROR, "File parse error"),
    FILE_EXISTS(ErrorCodeConstants.FILE_EXISTS, "File exists"),
    FILE_NAME_SPECIAL(ErrorCodeConstants.FILE_NAME_SPECIAL, "File name shouldn't contains '#' or '%' "),
    CONFIGURATION_ERROR(ErrorCodeConstants.CONFIGURATION_ERROR, "Configuration error"),
    REPEAT_REQUEST_ERROR(ErrorCodeConstants.REQUEST_REPEAT_ERROR, "Don't repeat request"),

    DATASET_NAME_EXIST(ErrorCodeConstants.DATASET_NAME_EXIST, "Dataset name is existed, please check"),
    PROJECT_NAME_EXIST(ErrorCodeConstants.PROJECT_NAME_EXIST, "Project name is existed, please check"),
    WAFER_MAP_CONFIG_NAME_EXIST(ErrorCodeConstants.WAFER_MAP_CONFIG_NAME_EXIST, "Wafer map config name is existed, please check"),


    UPDATE_DB_ERROR(ErrorCodeConstants.UPDATE_DB_ERROR, "Update db error because of version"),
    DATA_IS_NULL(ErrorCodeConstants.DATA_IS_NULL, "Data is null"),
    HTTP_RESPONSE_IS_NULL(ErrorCodeConstants.HTTP_RESPONSE_IS_NULL, "Upstream http response is error"),
    GENERATE_IS_ERROR(ErrorCodeConstants.GENERATE_IS_ERROR, "GenerateFile is error"),
    ORGANIZE_MAP_IS_ERROR(ErrorCodeConstants.ORGANIZE_MAP_IS_ERROR, "OrganizeByMap func error"),
    ORGANIZE_GENERIC_IS_ERROR(ErrorCodeConstants.ORGANIZE_GENERIC_IS_ERROR, "OrganizeByGenericService func error"),
    EXPORT_FILE_BY_DATA_ERROR(ErrorCodeConstants.EXPORT_FILE_BY_DATA_ERROR, "Export file by data error"),
    EXPORT_JSON_CONFIG_ERROR(ErrorCodeConstants.EXPORT_JSON_CONFIG_ERROR, "Export json config error"),
    EXPORT_ENUM_IS_NULL(ErrorCodeConstants.EXPORT_ENUM_IS_NULL, "Export enum is null"),
    FILE_UPLOAD_ERROR(ErrorCodeConstants.FILE_UPLOAD_ERROR, "File upload error"),
    FILE_DOWNLOAD_ERROR(ErrorCodeConstants.FILE_DOWNLOAD_ERROR, "File download error"),

    DB_IMPORT_URL_ERROR(ErrorCodeConstants.DB_IMPORT_URL_ERROR, "DB import url error or no suitable driver found"),
    DB_IMPORT_CREDENTIAL_ERROR(ErrorCodeConstants.DB_IMPORT_CREDENTIAL_ERROR, "DB import user name or password error"),
    DB_IMPORT_SQL_ERROR(ErrorCodeConstants.DB_IMPORT_SQL_ERROR, "DB import sql error, sql should be start with select or sql syntax error"),


    FIELD_DUPLICATE(ErrorCodeConstants.FIELD_DUPLICATE, "Field duplicate"),
    EXPORT_DATA_ERROR(ErrorCodeConstants.EXPORT_DATA_ERROR, "Export data error, please try again later."),
    COLUMN_TYPE_ERROR(ErrorCodeConstants.COLUMN_TYPE_ERROR, "Column type error."),
    NAME_DUPLICATE(ErrorCodeConstants.NAME_DUPLICATE, "Name duplicate"),
    DATA_DUPLICATE(ErrorCodeConstants.DATA_DUPLICATE, "Data duplicate"),
    PROJECT_ID_DUPLICATE(ErrorCodeConstants.PROJECT_ID_DUPLICATE, "Project id duplicate"),


    LOAD_DATASET_ERROR(ErrorCodeConstants.LOAD_DATASET_ERROR, "Load dataset error"),
    DATA_IMPORT_ERROR(ErrorCodeConstants.DATA_IMPORT_ERROR, "Data import error, please try again later."),
    DATA_IS_PROCESSED(ErrorCodeConstants.DATA_IS_PROCESSED, "The previous query is being processed, please try again later."),
    DATA_SET_IS_EMPTY(ErrorCodeConstants.DATA_SET_IS_EMPTY, "The database is empty, you can upload some data files first."),
    DATA_TYPE_IS_WRONG(ErrorCodeConstants.DATA_TYPE_IS_WRONG, "Data type is wrong"),
    PROJCET_NAME_ALREADY_EXIST(ErrorCodeConstants.PROJCET_NAME_ALREADY_EXIST, "Name already exist, please check"),

    CREATE_DATA_SET_ERROR(ErrorCodeConstants.CREATE_DATA_SET_ERROR, "Create data error"),
    SCHEDULE_TIME_ERROR(ErrorCodeConstants.SCHEDULE_TIME_ERROR, "Schedule time error"),
    START_TIME_EMPTY(ErrorCodeConstants.START_TIME_EMPTY, "Start time is empty"),
    VERSION_REVIEWED(ErrorCodeConstants.VERSION_REVIEWED, "This version has been reviewed"),
    EMAIL_SEND_ERROR(ErrorCodeConstants.EMAIL_SEND_ERROR, "Email send error"),
    CREATE_PROJECT_FAIL(ErrorCodeConstants.CREATE_PROJECT_FAIL, "Create project fail"),
    PROJECT_DISABLED(ErrorCodeConstants.PROJECT_DISABLED, "Project disabled!"),


    RULE_MISMATCH(ErrorCodeConstants.RULE_MISMATCH, "Rule mismatch"),
    NO_PERMISSION(ErrorCodeConstants.NO_PERMISSION, "No Permission"),


    BUSINESS_ERROR(ErrorCodeConstants.BUSINESS_ERROR, "Business error: "),
    SERVER_ERROR(ErrorCodeConstants.SERVER_ERROR, "Server error: "),
    NOT_SUPPORT_ERROR(ErrorCodeConstants.NOT_SUPPORT_ERROR, "Not support error"),


    SYSTEM_ERROR(ErrorCodeConstants.SYSTEM_ERROR, "System error, please contact administrator."),
    // 前端入参校验不通过
    PARAMETER_ERROR(ErrorCodeConstants.PARAM_ERROR, "input params error"),
    DATE_FORMAT_ERROR(ErrorCodeConstants.DATE_FORMAT_ERROR, "date format error"),
    NO_DATA_IN_DB(ErrorCodeConstants.NO_DATA_IN_DB, "db not data"),

    // 文档相关
    // excel
    EXCEL_EMPTY_ERROR(ErrorCodeConstants.EXCEL_EMPTY_ERROR, "excel is empty"),
    EXCEL_NO_VALID_DATA_ERROR(ErrorCodeConstants.EXCEL_NO_VALID_DATA_ERROR, "excel no valid data"),
    EXCEL_DUPLICATE_DATA_ERROR(ErrorCodeConstants.EXCEL_DUPLICATE_DATA_ERROR,"excel duplicate data");

    ResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResponseEnum getEnumByCode(int code) {
        for (ResponseEnum enumInstance : ResponseEnum.values()) {
            if (enumInstance.getCode() == code) {
                return enumInstance;
            }
        }
        throw new IllegalArgumentException("Enum not found for the given code");
    }
}
