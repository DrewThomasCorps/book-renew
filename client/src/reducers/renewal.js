import {
    GET_RENEWALS,
    RENEWAL_ERROR
} from "../actions/types";

const initialState = {
    renewals: [],
    loading: true,
    error: {}
};

export default function(state = initialState, action) {
    const {type, payload} = action;

    switch(type){
        case GET_RENEWALS:
            return {
                ...state,
                renewals: payload,
                loading: false
            };
        case RENEWAL_ERROR:
            return {
                ...state,
                error: payload
            };
        default:
            return state;
    }
}