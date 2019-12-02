import axios from 'axios';

const setAuthToken = authToken => {
    //the common variable specifies axios default for all requests
    if(authToken) {
        axios.defaults.headers.common['Authorization'] = authToken;
    } else {
        delete axios.defaults.headers.common['Authorization'];
    }
};

export default setAuthToken;