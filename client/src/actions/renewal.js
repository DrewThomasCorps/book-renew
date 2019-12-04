import axios from 'axios';
import {
    GET_POTENTIAL_RENEWALS,
    GET_RENEWALS,
    RENEWAL_ERROR,
    OFFER_RENEWAL,
    CANCEL_RENEWAL,
    COMPLETE_RENEWAL,
    ACCEPT_RENEWAL
} from "./types";

import {BASE_URL} from "../config/config";
import {setAlert} from "./alert";

export const getPotentialRenewals = () => async dispatch => {
    try {
        const res = await axios.get(BASE_URL + "/potential-trades");
        dispatch({
            type: GET_POTENTIAL_RENEWALS,
            payload: res.data
        })

    } catch (error) {
        dispatch({
            type: RENEWAL_ERROR
        })
    }
};

export const getRenewals = () => async dispatch => {
    try {
        const res = await axios.get(BASE_URL + "/renewals");
        dispatch({
            type: GET_RENEWALS,
            payload: res.data
        })

    } catch (error) {
        dispatch({
            type: RENEWAL_ERROR
        })
    }
};

export const offerRenewal = (trader_book_user_id,tradee_book_user_id) => async dispatch => {
    const setHeaders = {
        headers: {
            'Content-Type' : 'application/json'
        }
    };

    const body = JSON.stringify(
        {
        "trader_book_user_id" : trader_book_user_id,
        "tradee_book_user_id" : tradee_book_user_id
    });

    try {
        await axios.post(BASE_URL + "/renewals", body, setHeaders);
        dispatch({
            type: OFFER_RENEWAL
        });
        dispatch(setAlert("Successfully sent offer",'success'));
    } catch (error) {
        dispatch({
            type: RENEWAL_ERROR,
            payload: error
        })
    }
};

export const cancelRenewal = id => async dispatch => {
    const setHeaders = {
        headers: {
            'Content-Type' : 'application/json'
        }
    };

    const body = JSON.stringify(
        {
            "status" : "declined"
        });

    try {
        await axios.put(BASE_URL + "/renewals/" + id, body, setHeaders);
        dispatch({
            type: CANCEL_RENEWAL,
            payload: id
        });
        dispatch(setAlert("Renewal cancelled",'success'));
        dispatch(getRenewals());
    } catch (error) {
        dispatch({
            type: RENEWAL_ERROR,
            payload: error
        })
    }
};

export const completeRenewal = id => async dispatch => {
    const setHeaders = {
        headers: {
            'Content-Type' : 'application/json'
        }
    };

    const body = JSON.stringify(
        {
            "status" : "completed"
        });

    try {
        await axios.put(BASE_URL + "/renewals/" + id, body, setHeaders);
        dispatch({
            type: COMPLETE_RENEWAL,
            payload: id
        });
        dispatch(setAlert("Renewal has been completed",'success'));
        dispatch(getRenewals());
    } catch (error) {
        dispatch({
            type: RENEWAL_ERROR,
            payload: error
        })
    }
};

export const acceptRenewal = id => async dispatch => {
    const setHeaders = {
        headers: {
            'Content-Type' : 'application/json'
        }
    };

    const body = JSON.stringify(
        {
            "status" : "active"
        });

    try {
        await axios.put(BASE_URL + "/renewals/" + id, body, setHeaders);
        dispatch({
            type: ACCEPT_RENEWAL,
            payload: id
        });
        dispatch(setAlert("Successfully accepted renewal",'success'));
    } catch (error) {
        dispatch({
            type: RENEWAL_ERROR,
            payload: error
        })
    }
};
