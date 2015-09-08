package me.memleak.pomorello.rest.apis;

import java.util.ArrayList;

import me.memleak.pomorello.models.TrelloBoard;
import me.memleak.pomorello.models.TrelloCard;
import me.memleak.pomorello.models.TrelloList;
import me.memleak.pomorello.rest.TrelloCallback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by jafar_qaddoumi on 9/7/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public interface TrelloApi {

    @GET("/lists/{listId}/cards?fields=id,name")
    void getCards(
            @Path("listId") String listId,
            TrelloCallback<ArrayList<TrelloCard>> callback);

    @GET("/members/me/boards?fields=id,name")
    void getAllBoards(
            TrelloCallback<ArrayList<TrelloBoard>> callback
    );

    @GET("/boards/{boardId}/lists?fields=id,name")
    void getAllLists(
            @Path("boardId") String boardId,
            TrelloCallback<ArrayList<TrelloList>> callback
    );

    @PUT("/cards/{cardId}")
    void moveCard(
            @Path("cardId") String cardId,
            @Query("idList") String listId,
            TrelloCallback<String> callback
    );

    @POST("/cards/{cardId}/actions/comments")
    void addComment(
            @Path("cardId") String cardId,
            @Query("text") String comment,
            TrelloCallback<String> callback
    );
}
