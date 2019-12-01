import axios from 'axios';
import {
    GET_RENEWALS,
    RENEWAL_ERROR,
    OFFER_RENEWAL
} from "./types";

export const getRenewals = () => async dispatch => {
    try {
        //TODO Handle axios request for GET_RENEWALS action
        const renewals = [
            {
                "trader": {
                    "user" : {
                        "name" : "Drew Thomas",
                        "id": 1,
                        "email" : "amthomas12@bsu.edu"
                    },
                    "book": {
                        "title": "Some Cool Title",
                        "isbn": "123"
                    },
                    "book_user_id" : 4
                },
                "tradee": {
                    "user" : {
                        "name" : "Drew Thomas",
                        "id": 2,
                        "email" : "test@test.edu"
                    },
                    "book": {
                        "title": "Seven Ate Nine!!",
                        "isbn": "789"
                    },
                    "book_user_id" : 8
                },
                "status": "active"
            },
            {
                "trader": {
                    "user" : {
                        "name" : "Drew Thomas",
                        "id": 1,
                        "email" : "amthomas12@bsu.edu"
                    },
                    "book": {
                        "title": "Some Cool Title",
                        "isbn": "123"
                    },
                    "book_user_id" : 4
                },
                "tradee": {
                    "user" : {
                        "name" : "Drew Thomas",
                        "id": 2,
                        "email" : "test@test.edu"
                    },
                    "book": {
                        "title": "Seven Ate Nine!!",
                        "isbn": "789"
                    },
                    "book_user_id" : 8
                }
            }
        ];
        dispatch({
            type: GET_RENEWALS,
            payload: renewals
        })

    } catch (error) {
        dispatch({
            type: RENEWAL_ERROR,
            loading: false
        })
    }
};

export const offerRenewal = (traderBookId,tradeeBookId) => async dispatch => {
    try {
        //TODO Handle axios request for OFFER_RENEWAL action
        dispatch({
            type: OFFER_RENEWAL
        });
    } catch (error) {
        dispatch({
            type: RENEWAL_ERROR,
            loading: false
        })
    }
};

