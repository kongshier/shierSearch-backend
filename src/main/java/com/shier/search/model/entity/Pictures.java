package com.shier.search.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Shier 2023/3/16 14:15
 */
@Data
public class Pictures implements Serializable {

    private static final long serialVersionUID = 7448600246466184410L;

    private String title;

    private String url;
}
