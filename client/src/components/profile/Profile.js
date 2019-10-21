import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import Navigation from "../layout/Navigation";

const Profile = props => {
    return (
        <Fragment>
            <Navigation />
            <section className={"pl-54 row"}>
                <h1 className={""}>Profile</h1>
            </section>
        </Fragment>
    )
};

Profile.propTypes = {

};

export default Profile;