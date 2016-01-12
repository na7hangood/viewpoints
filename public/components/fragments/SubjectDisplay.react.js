import React from 'react';
import { ListGroupItem } from 'react-bootstrap';

export default class SubjectDisplay extends React.Component {

  constructor(props) {
    super(props);
  }

  selectThisSubject() {
    this.props.subjectSelected(this.props.subject);
  }

  render () {

    return (
      <ListGroupItem header={this.props.subject.name} onClick={this.selectThisSubject.bind(this)}>
        {this.props.subject.viewpoints.length} viewpoints
      </ListGroupItem>
    );
  }
}

/*

 id: Long,
 name: String,
 imageUrl: Option[String],
 description: Option[String],
 party: Option[String],
 category: Option[String]
 */