package me.memleak.pomorello.rest.apis;

import java.util.List;

import me.memleak.pomorello.models.TrelloBoard;
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
//    @GET("/1/members/me?fields=fullName&boards=all&board_fields=name")
//    void getMember(
//            TrelloCallback<Member> memberCallback
//    );
//
//    @GET("/1/boards/{boardId}?fields=id,name&lists=all&list_fields=name")
//    void getBoard(
//            @Path("boardId") String boardId,
//            TrelloCallback<TrelloBoard> callback
//    );
//
//    @GET("/1/lists/{listId}?fields=name&cards=open&card_fields=name")
//    void getList(
//            @Path("listId") String listId,
//            TrelloCallback<TrelloList> callback
//    );

    @GET("/members/me/boards?fields=id,name")
    void getAllBoards(
            TrelloCallback<List<TrelloBoard>> callback
    );

    @GET("/boards/{boardId}/lists?fields=id,name")
    void getAllLists(
            @Path("boardId") String boardId,
            TrelloCallback<List<TrelloList>> callback
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
