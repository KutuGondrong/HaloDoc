package com.kutugondrong.halodoc.model;

import java.io.Serializable;

/**
 * Created by kutu gondrong on 06/08/2017.
 */

public abstract class BaseModel<T> implements Serializable {
    public abstract T getJsonFromApi(String value);
}
