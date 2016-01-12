import React from 'react';
import { Button } from 'react-bootstrap';
import SubjectList from './fragments/SubjectList.react'
import viewpointsApi from '../util/viewpointsApi.js';

export default class Subjects extends React.Component {

  constructor(props) {
    super(props);
    this.state = {subjects: [], selectedSubject: undefined};
  }

  componentDidMount() {
    this.fetchSubjects();
  }

  fetchSubjects() {
    viewpointsApi.getAllSubjects().then(res => {
      this.setState({subjects: res});
    });
  }

  subjectSelected(subject) {
    this.setState({selectedSubject: subject});
  }

  showNewSubjectForm() {
    this.setState({selectedSubject: {}});
  }

  cancelSubjectEdit() {
    this.setState({selectedSubject: undefined});
  }

  render () {

    if (this.state.selectedSubject) {
      return (
        <div>
          <p>form goes here</p>
          <Button bsStyle="primary" onClick={this.cancelSubjectEdit.bind(this)} >cancel</Button>
        </div>
      );
    } else {
      return (
        <div>
          <p>Select a subject to edit, or add a new one</p>
          <Button bsStyle="primary" onClick={this.showNewSubjectForm.bind(this)} >Add subject</Button>
          <SubjectList subjects={this.state.subjects} subjectSelected={this.subjectSelected.bind(this)} />
        </div>
      )
    }
  }
}