import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import Navigation from "../layout/Navigation";
import { connect } from 'react-redux';
import { logoutUser } from '../../actions/auth';

const Profile = ({ auth: {user,loading}, logoutUser}) => {
    return (
        <Fragment>
            <Navigation />
            <section className={"row br-content-container"}>
                { !loading && (
                    <div className={"col-12 main-text-color"}>
                        <h1>Profile</h1>
                    <hr/>
                        <img src={"https://via.placeholder.com/250"} alt={user.name+" Profile Image"}/>
                        <h2>{user.name}</h2>
                        <h3>{user.email}</h3>
                        <a href="#" onClick={logoutUser}>Log Out</a>
                    </div>
                )}
            </section>
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