import axios from 'axios';
import {
    REGISTER_SUCCESS,
    REGISTER_FAIL,
    USER_LOADED,
    AUTH_ERROR,
    LOGIN_SUCCESS,
    LOGIN_FAIL
} from "../actions/types";
import {BASE_URL} from "../config/config";
import setAuthToken from "../utils/setAuthToken";

export const loadUser = () => async dispatch => {
    if (localStorage.authToken) {
        setAuthToken(localStorage.authToken)
    }
    axios.get(BASE_URL + "/users/self").then(response => {
        dispatch({
            type: USER_LOADED,
        })
    }).catch(error => {
        dispatch({
            type: AUTH_ERROR
        })
    });
};

export const registerUser = ({name, email, password}) => async dispatch => {
    const setHeaders = {
        headers: {
            'Content-Type': 'application/json'
        }
    };

    const body = JSON.stringify({name, email, password});

    try {
        await axios.post(BASE_URL + "/users", body, setHeaders);
        dispatch({
            type: REGISTER_SUCCESS,
            payload: {'authToken': '123456'}
        })
    } catch (err) {
        console.log(err);
        dispatch({
            type: REGISTER_FAIL
        })
    }
};

export const loginUser = (email, password) => async dispatch => {
    const setHeaders = {
        headers: {
            'Content-Type': 'application/json'
        }
    };

    const body = JSON.stringify({email, password});

    axios.post(BASE_URL + "/login", body, setHeaders)
        .then(response => {
            console.log(response);
            dispatch({
                type: LOGIN_SUCCESS,
                payload: {'authToken': response.headers['authorization']}
            });
            dispatch(loadUser());
        }).catch(error => {
        console.log(error);
        dispatch({
            type: LOGIN_FAIL
        })
    });
};
