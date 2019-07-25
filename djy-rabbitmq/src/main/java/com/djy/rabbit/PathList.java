package com.djy.rabbit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "path")
@PropertySource("classpath:application.yml")
public class PathList implements Serializable {

    private static final long serialVersionUID = -6988796053377153151L;
    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
