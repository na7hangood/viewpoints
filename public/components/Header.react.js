import React from 'react';
import { Link } from 'react-router';
import { Navbar } from 'react-bootstrap';

export default class Header extends React.Component {

  constructor(props) {
    super(props);
  }

  render () {

    return (
      <Navbar>
        <Navbar.Header>
          <Navbar.Brand>
            <Link to="/">Viewpoints</Link>
          </Navbar.Brand>
        </Navbar.Header>
        <Navbar.Collapse>
          <Navbar.Text><Link to="/commenters">Commenters</Link></Navbar.Text>
          <Navbar.Text><Link to="/subjects">Subjects</Link></Navbar.Text>
          <Navbar.Text><Link to="/categories">Categories</Link></Navbar.Text>
        </Navbar.Collapse>
      </Navbar>
    );
  }
}