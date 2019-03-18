package com.ai.aif.osp.jstorm.common;

/**
 * @Author :zuogy@asiainfo.com
 * Created on 2018/3/27.
 */
public class Constant {
    private Constant(){}

    public static final String FIELD_ID = "id";
    public static final String FIELD_TRACEID = "traceId";
    public static final String FIELD_PARENTID = "pid";
    public static final String FIELD_SUCCESS = "success";
    public static final String FIELD_RETCODE = "retCode";
    public static final String FIELD_STARTTIME = "startTime";
    public static final String FIELD_INVOKEDTIME = "invokedTime";
    public static final String FIELD_SERVICENAME = "serviceName";
    public static final String FIELD_ELASPETIME = "elapsedTime";
    public static final String FIELD_CALLTYPE = "callType";
    public static final String FIELD_LEVEL = "level";
    public static final String FIELD_SYSCODE = "sysCode";
    public static final String FIELD_MSGTYPE = "msgType";
    public static final String FIELD_APPNAME = "appName";
    public static final String FIELD_CHANNEL = "channel";
    public static final String FIELD_REGION = "region";


    public static final String FIELD_EXC_THROWN = "thrown_name";
    public static final String FIELD_EXC_MSG = "thrown_msg";
    public static final String FIELD_MESSAGE = "message";

    public static final String FIELD_CHANNELID = "channelId";
    public static final String FIELD_CHANNELNAME = "channelName";
    public static final String FIELD_APPID = "appId";
    public static final String FIELD_ABILITYID = "abilityId";
    public static final String FIELD_ABILITYCODE = "abilityCode";
    public static final String FIELD_ABILITYNAME = "abilityName";
    public static final String FIELD_UID = "uId";
    public static final String FIELD_EXPCODE = "expCode";

    /**
     * 用于JSTORM处理流添加的额外字段，并不会存储
     */
    public static final String _FIELD_PREFIX = "__";
    public static final String _FIELD_TASKID = "__TASK_ID" ;
    public static final String TABLE_ERROR = "ERR";

    //es index
    public static final String INDEX_TYPE_TRACE = "osp_trace";
    public static final String INDEX_TYPE_AGG = "osp_agg";
}
