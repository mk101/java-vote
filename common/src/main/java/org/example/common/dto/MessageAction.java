package org.example.common.dto;

public enum MessageAction {

    /**
     * create topic
     * payload: name -> string
     */
    CREATE_TOPIC,

    /**
     * view
     * optional payload: topic -> string or (topic -> string and vote -> string)
     * return payload: result -> string[]
     */
    VIEW,

    /**
     * create vote
     * payload: topic -> string, name -> string, description -> string, answers -> string[]
     */
    CREATE_VOTE,

    /**
     * vote
     * payload: topic -> string, vote -> string, choose -> string
     */
    VOTE,

    /**
     * delete
     * payload: topic -> string, vote -> string
     */
    DELETE,

    /**
     * payload: topic -> string, vote -> string
     * return payload: questions -> string[]
     */
    GET_VOTE,

    OK,
    UNAUTHORISED,
    /**
     * payload: message -> string
     */
    ERROR

}
