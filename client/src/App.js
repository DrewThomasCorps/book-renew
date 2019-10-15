import React, {Fragment} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import Registration from "./components/auth/Registration";
import Login from "./components/auth/Login";

import { Provider } from 'react-redux';
import store from './store';

import './App.css';

const App = () =>
    <Provider store={store}>
        <Router>
            <Fragment>
                <section className={"container"}>
                    <Switch>
                        <Route exact path="/" component={Login} />
                        <Route exact path="/registration" component={Registration} />
                    </Switch>
                </section>
            </Fragment>
        </Router>
    </Provider>;

export default App;
