import {
    REGISTER_FAIL,
    USER_LOADED,
    AUTH_ERROR,
    LOGIN_SUCCESS,
    LOGIN_FAIL,
    LOGOUT
} from '../actions/types';

const initialState = {
    authToken: localStorage.getItem('authToken'),
    isAuthenticated: null,
    loading: true,
    user: null
};

export default function(state = initialState, action){
    const { type, payload} = action;

    switch(type){
        case USER_LOADED:
            return {
                ...state,
                isAuthenticated: true,
                loading: false,
                user: payload
            };
        case LOGIN_SUCCESS:
            localStorage.setItem('authToken', payload.authToken);
            return {
                ...state,
                ...payload,
                isAuthenticated: true,
                loading: false
            };
        case REGISTER_FAIL:
        case AUTH_ERROR:
        case LOGIN_FAIL:
        case LOGOUT:
            localStorage.removeItem('authToken');
            return {
                ...state,
                authToken: null,
                isAuthenticated: false,
                loading: false,
                user: null
            };
        default:
            return state;
    }
};