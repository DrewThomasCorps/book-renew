import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import Navigation from "../layout/Navigation";
import { connect } from 'react-redux';
import { logoutUser } from '../../actions/auth';
import BookForm from "../books/BookForm";
import Books from "../books/Books";

const Profile = ({ auth: {user,loading}, logoutUser}) => {
    return (
        <Fragment>
            <Navigation />
            { !loading && (
            <Fragment>
                <section className={"row br-content-container"}>
                    <section className={"col-12 bg-grey vh-100"}>
                        <section className={"row"}>
                            <section className={"col-12 order-first content-header"}>
                                <h1 className={"float-left text-uppercase font-weight-bolder main-text-color header-border"}>Profile</h1>
                                <a href="#" className={"float-right btn btn-dark my-1 md-3 log-out-btn"} onClick={logoutUser}>Log Out</a>
                            </section>
                            <section className={"col-12 order-3 order-md-2 col-md-9 content-panel"}>
                                <BookForm />
                                <div className={"row books-container p-2"}>
                                    <div className={"col-12 book-container bg-white left-blue-border shadow"}>
                                        <Books bookType={"owner"}/>
                                    </div>
                                </div>
                                <div className={"row books-container p-2"}>
                                    <div className={"col-12 book-container bg-white left-blue-border shadow"}>
                                        <Books bookType={"wishlist"}/>
                                    </div>
                                </div>
                            </section>
                            <section className={"col-12 order-2 order-md-3 col-md-3 content-panel"}>
                                <div className={"card bg-blue text-white"}>
                                    <p className={"h4"}>Welcome {user.name}</p>
                                    <p>{user.email}</p>
                                </div>
                            </section>
                        </section>
                    </section>

                </section>
            </Fragment>
            )}
        </Fragment>
    )
};

Profile.propTypes = {
    logoutUser: PropTypes.func.isRequired,
    auth: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
    auth: state.auth
});

export default connect(mapStateToProps,{ logoutUser })(Profile);