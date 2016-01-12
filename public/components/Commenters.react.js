import React from 'react';
import { Grid, Row, Col, Button } from 'react-bootstrap';
import CommenterList from './fragments/CommenterList.react'
import CommenterEdit from './fragments/CommenterEdit.react'
import viewpointsApi from '../util/viewpointsApi.js';

export default class Commenters extends React.Component {

  constructor(props) {
    super(props);
    this.state = {commenters: [], selectedCommenter: undefined};
  }

  componentDidMount() {
    this.fetchCommenters();
  }

  fetchCommenters() {
    viewpointsApi.getAllCommenters().then(res => {
      this.setState({commenters: res});
    });
  }

  commenterSelected(commenter) {
    this.setState({selectedCommenter: commenter});
  }

  showNewCommenterForm() {
    this.setState({selectedCommenter: {}});
  }

  saveSelectedCommenter(updated) {
    viewpointsApi.saveCommenter(updated).then(res => {
      this.setState({selectedCommenter: undefined});
      this.fetchCommenters();
    });
  }

  cancelCommenterEdit() {
    this.setState({selectedCommenter: undefined});
  }

  render () {
    var editForm;

    if (this.state.selectedCommenter) {
      editForm = (
        <CommenterEdit commenter={this.state.selectedCommenter}
                       saveCommenter={this.saveSelectedCommenter.bind(this)}
                       cancelCommenterEdit={this.cancelCommenterEdit.bind(this)}/>
      );
    } else {
      editForm = (
        <div>
          <p>Select a commenter to edit, or add a new one</p>
          <Button bsStyle="primary" onClick={this.showNewCommenterForm.bind(this)} >Add commenter</Button>
        </div>
      )
    }

    return (
      <Grid>
        <Row className="show-grid">
          <Col xs={12} md={4}>
            <CommenterList commenters={this.state.commenters} commenterSelected={this.commenterSelected.bind(this)} />
            <Button bsStyle="primary" onClick={this.showNewCommenterForm.bind(this)} >Add commenter</Button>
          </Col>
          <Col xs={12} md={8}>
            {editForm}
          </Col>
        </Row>
      </Grid>
    );
  }
}