import React from 'react';
import { Link } from 'react-router';

export default class Header extends React.Component {

  constructor(props) {
    super(props);
  }



  render () {

    return (
      <header className="top-toolbar">

        <div className="header__children">
          <ul className="links">
            <li className="links__item top-toolbar__item--highlight">
              <Link to="/">Home</Link>
            </li>
            <li className="links__item top-toolbar__item--highlight">
              <Link to="/commenters">Commenters</Link>
            </li>
            <li className="links__item top-toolbar__item--highlight">
              <Link to="/subjects">Subjects</Link>
            </li>
            <li className="links__item top-toolbar__item--highlight">
              <Link to="/categories">Categories</Link>
            </li>
          </ul>
        </div>
      </header>
    );
  }
}