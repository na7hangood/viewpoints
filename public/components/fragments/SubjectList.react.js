import React from 'react';
import { ListGroup } from 'react-bootstrap';
import SubjectDisplay from './SubjectDisplay.react';

export default class SubjectList extends React.Component {

  constructor(props) {
    super(props);
  }

  render () {

    const self = this;
    const subjectNodes = this.props.subjects.map(function(subject) {
      return (
        <SubjectDisplay subject={subject} subjectSelected={self.props.subjectSelected} key={subject.id} />
      );
    });

    if (!this.props.subjects || !this.props.subjects.length) {
      return (
        <p>There are no subjects defined</p>
      )
    }

    return (
      <div>
        <ListGroup>
          {subjectNodes}
        </ListGroup>
      </div>
    );
  }
}