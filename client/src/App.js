import React, {Fragment} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import './App.css';

const App = () =>
    <Router>
        <Fragment>
            <section className={"container"}>
                <Switch>
                    <Route path={'/'}>
                        <h1>Book Renew</h1>
                    </Route>
                </Switch>
            </section>
        </Fragment>
    </Router>;

export default App;
