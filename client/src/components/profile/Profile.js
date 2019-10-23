import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import Navigation from "../layout/Navigation";
import { connect } from 'react-redux';
import { logoutUser } from '../../actions/auth';

const Profile = ({ auth: {loading}, logoutUser}) => {
    return (
        <Fragment>
            <Navigation />
            <section className={"row br-content-container"}>
                <div className={"col-12"}>
                    <img src={"https://via.placeholder.com/250"} />
                    <h2>Name</h2>
                </div>
                <div className={"col-12"}>
                    <h3>Rating</h3>
                    <h3>Email</h3>
                    { !loading && (<a href="#" onClick={logoutUser}>Log Out</a>)}
                </div>
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
})

export default connect(mapStateToProps,{ logoutUser })(Profile);