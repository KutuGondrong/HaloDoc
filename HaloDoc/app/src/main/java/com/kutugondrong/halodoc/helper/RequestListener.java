package com.kutugondrong.halodoc.helper;

/**
 * Created by kutugondrong.com on 17/07/2017.
 */

public interface RequestListener<T> {
    void onStart();
    void onSuccess(T value);
    void onFailed();
}
