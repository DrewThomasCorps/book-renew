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
                    <section className={"col-12"}>
                        <section className={"row br-grey"}>
                            <section className={"col-12 content-header"}>
                                <h1 className={"float-left text-uppercase font-weight-bolder main-text-color header-border"}>Profile</h1>
                                <a href="#" className={"float-right btn btn-dark my-1 md-3 add-book-btn"} onClick={logoutUser}>Log Out</a>
                            </section>
                            <section className={"col-12 col-md-9 content-panel"}>
                                <BookForm />
                                <div className={"row books-container"}>
                                    <div className={"col-10 book-container"}>
                                        <Books bookType={"library"}/>
                                    </div>
                                </div>
                                <div className={"row books-container"}>
                                    <div className={"col-10 book-container"}>
                                        <Books bookType={"wishlist"}/>
                                    </div>
                                </div>
                            </section>
                            <section className={"col-12 col-md-3 content-panel br-bg-blue ice-white-text"}>
                                <h2>Welcome {user.name}</h2>
                                <h3>{user.email}</h3>
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