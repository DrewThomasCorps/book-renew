import React, {Fragment} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import Registration from "./components/auth/Registration";
import Login from "./components/auth/Login";
import './App.css';

const App = () =>
    <Router>
        <Fragment>
            <section className={"container"}>
                <Switch>
                    <Route exact path="/" component={Login} />
                    <Route exact path="/registration" component={Registration} />
                </Switch>
            </section>
        </Fragment>
    </Router>;

export default App;
