import React, {Fragment} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import Registration from "./components/auth/Registration";
import Login from "./components/auth/Login";
import Dashboard from "./components/dashboard/Dashboard";
import Profile from "./components/profile/Profile";
import PrivateRoute from "./components/routing/PrivateRoute";

import { Provider } from 'react-redux';
import store from './store';
import setAuthToken from "./utils/setAuthToken";

import './App.css';

if (localStorage.authToken){
    setAuthToken(localStorage.authToken);
}

const App = () =>{

    return (
    <Provider store={store}>
        <Router>
            <Fragment>
                <section className={"container"}>
                    <Switch>
                        <Route exact path="/" component={Login} />
                        <Route exact path="/registration" component={Registration} />
                        <PrivateRoute exact path="/dashboard" component={Dashboard} />
                        <PrivateRoute exact path="/profile" component={Profile} />
                    </Switch>
                </section>
            </Fragment>
        </Router>
    </Provider>
)};

export default App;
