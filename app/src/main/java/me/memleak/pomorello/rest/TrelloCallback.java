package me.memleak.pomorello.rest;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jafar_qaddoumi on 9/7/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class TrelloCallback<T> implements Callback<T> {

    @Override
    public void success(T result, Response response) {

    }

    @Override
    public void failure(RetrofitError error) {

    }
}
