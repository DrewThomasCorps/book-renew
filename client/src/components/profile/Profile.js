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
                        <section className={"row"}>
                            <section className={"col-12 content-header"}>
                                <h1>Profile</h1>
                            </section>
                            <section className={"col-12 col-md-9 content-panel"}>
                                <BookForm />
                                <div className={"row books-container"}>
                                    <div className={"col-12 book-container"}>
                                        <Books bookType={"library"}/>
                                    </div>
                                </div>
                                <div className={"row books-container"}>
                                    <div className={"col-12 book-container"}>
                                        <Books bookType={"wishlist"}/>
                                    </div>
                                </div>
                            </section>
                            <section className={"col-12 col-md-3 content-panel"}>
                                <h2>{user.name}</h2>
                                <h3>{user.email}</h3>
                                <a href="#" onClick={logoutUser}>Log Out</a>
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