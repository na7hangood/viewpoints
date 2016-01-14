import React from 'react';
import { ListGroupItem, Button, ButtonToolbar } from 'react-bootstrap';

export default class SubjectDisplay extends React.Component {

  constructor(props) {
    super(props);
  }

  selectThisSubject() {
    this.props.subjectSelected(this.props.subject);
  }

  previewLink() {
    return '/preview/' + this.props.subject.id;
  }

  render () {

    return (
      <ListGroupItem header={this.props.subject.name} >
        <ButtonToolbar>
          <Button href={this.previewLink()} target="_blank">Preview</Button>
          <Button onClick={this.selectThisSubject.bind(this)} bsStyle="primary">Edit</Button>
        </ButtonToolbar>
        {this.props.subject.viewpoints.length} viewpoints
      </ListGroupItem>
    );
  }
}