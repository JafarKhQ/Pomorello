package me.memleak.pomorello.rest;

import me.memleak.pomorello.BuildConfig;
import me.memleak.pomorello.PomorelloApp;
import me.memleak.pomorello.models.PomorelloUser;
import me.memleak.pomorello.rest.apis.BaseApi;
import retrofit.RequestInterceptor;

/**
 * Created by jafar_qaddoumi on 9/7/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class TrelloRequestInterceptor implements RequestInterceptor, BaseApi {

    private PomorelloUser mPomorelloUser;

    public TrelloRequestInterceptor() {
        mPomorelloUser = PomorelloApp.getPomorelloUser();
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addQueryParam(PARAM_KEY, BuildConfig.TRELLO_API_KEY);
        request.addQueryParam(PARAM_TOKEN, mPomorelloUser.getAccessToken());
    }
}
