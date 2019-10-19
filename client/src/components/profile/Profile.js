import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import Navigation from "../layout/Navigation";

const Profile = props => {
    return (
        <Fragment>
            <section className={"row"}>
                <div className={"container-fluid br-container-fluid"}>
                    <section className={"row"}>
                        <h1 className={"location-header"}>Profile</h1>
                    </section>
                    <section className={"row"}>
                        <Navigation />
                    </section>
                </div>
            </section>
        </Fragment>
    )
};

Profile.propTypes = {

};

export default Profile;