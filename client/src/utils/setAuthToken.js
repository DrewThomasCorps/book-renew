import axios from 'axios';

const setAuthToken = authToken => {
    if(authToken) {
        axios.defaults.headers.common['Authorization'] = authToken;
    } else {
        delete axios.defaults.headers.common['Authorization'];
    }
}

export default setAuthToken;