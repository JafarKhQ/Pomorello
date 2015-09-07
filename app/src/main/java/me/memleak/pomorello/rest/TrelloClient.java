package me.memleak.pomorello.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.memleak.pomorello.rest.apis.BaseApi;
import me.memleak.pomorello.rest.apis.TrelloApi;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by jafar_qaddoumi on 9/7/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class TrelloClient implements BaseApi {

    private static final Gson mGson;
    private static final GsonConverter mGsonConverter;

    static {
        mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        mGsonConverter = new GsonConverter(mGson);
    }

    private TrelloClient() {
    }

    public static TrelloApi getTrelloApi() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(URL_BASE)
                .setRequestInterceptor(new TrelloRequestInterceptor())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(mGsonConverter)
                .build();

        return restAdapter.create(TrelloApi.class);
    }
}
