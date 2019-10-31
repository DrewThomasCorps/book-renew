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
                            <div className={"col-12"}>
                            <h1>Profile</h1>
                            <img src={"https://via.placeholder.com/250"} alt={user.name+" Profile Image"}/>
                            <h2>{user.name}</h2>
                            <h3>{user.email}</h3>
                            <a href="#" onClick={logoutUser}>Log Out</a>
                            </div>
                        </section>
                        <section className={"row br-content-container"}>
                            <BookForm />
                        </section>
                        <section className={"row br-content-container"}>
                            <Books bookType={"library"}/>
                        </section>
                        <section className={"row br-content-container"}>
                            <Books bookType={"wishlist"}/>
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