package com.zzy.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class MsgModel implements Serializable {
    private String msg;
    private List list;

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    private Map<String, Object> attributes;// 其他参数
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
