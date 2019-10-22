import React, {Fragment} from 'react';
import PropTypes from 'prop-types';

const Book = props => {
    return (
        <Fragment>
            <article className={"container-fluid book-container py-3"}>
                <div className={"row book"}>
                    <div className={"col-12 col-sm-2 book-cover px-2"}>
                        <img className="img-fluid" src={"https://via.placeholder.com/150"} />
                    </div>
                    <div className={"col-12 col-sm-9 book-title px-2"}>
                        <h3>Book Title</h3>
                        <small>ISBN</small>
                    </div>
                    <div className={"col-12 col-sm-1 book-btn"}>
                        <button className={"btn btn-secondary"}>Button</button>
                    </div>
                </div>


            </article>
        </Fragment>
    )
};

Book.propTypes = {

};

export default Book;