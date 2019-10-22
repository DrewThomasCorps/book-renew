import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import Navigation from "../layout/Navigation";

const Profile = props => {
    return (
        <Fragment>
            <Navigation />
            <section className={"row br-content-container"}>
                <div className={"col-12"}>
                    <h1>Profile</h1>
                </div>
            </section>
        </Fragment>
    )
};

Profile.propTypes = {

};

export default Profile;