import React from 'react';
import { Grid, Row, Col, Panel, Button, ButtonToolbar, Input, ButtonInput } from 'react-bootstrap';
import ViewpointList from './ViewpointList.react'
import viewpointsApi from '../../util/viewpointsApi.js';

export default class SubjectEdit extends React.Component {

  constructor(props) {
    super(props);
    this.state = {modifiedSubject: {}};

  }

  componentDidMount() {
    this.loadSubject(this.props.subjectId);
  }

  loadSubject(id) {
    viewpointsApi.getSubject(id).then(res => {
      this.setState({modifiedSubject: res});
    });
  }

  saveGeneralInformation() {
    viewpointsApi.saveSubjectGeneralInformation(this.state.modifiedSubject).then(res => {
      this.setState({modifiedSubject: res});
    });
  }

  publishSubject() {
    console.log("publish");
  }

  updateName(e) {
    const updated = this.state.modifiedSubject;
    updated.name = e.target.value;
    this.setState({modifiedSubject: updated});
  }

  updateLink(e) {
    const updated = this.state.modifiedSubject;
    updated.name = e.target.value;
    this.setState({modifiedSubject: updated});
  }

  disablePublish() {
    return !(this.state.modifiedSubject.id && (this.state.modifiedSubject.viewpoints.length > 0));
  }

  selectViewpoint() {
    console.log('viewpoint selected');
  }

  render () {

    if (!this.props.subjectId) {
      return false;
    }

    return (
      <Grid>
        <Row>
          <Panel header="General information">
            <Col xs={3} md={3}>
              <ButtonToolbar>
                <Button onClick={this.props.cancelSubjectEdit} >Back</Button>
                <Button bsStyle="primary" onClick={this.saveGeneralInformation.bind(this)} >Save</Button>
                <Button bsStyle="success" onClick={this.publishSubject.bind(this)} disabled={this.disablePublish()}>Publish</Button>
              </ButtonToolbar>
            </Col>
            <Col xs={9} md={9}>
              <form className="form-horizontal">
                <Input type="text" label="Name" value={this.state.modifiedSubject.name} labelClassName="col-xs-2" wrapperClassName="col-xs-10" onChange={this.updateName.bind(this)} />
                <Input type="text" label="Link" value={this.state.modifiedSubject.link} labelClassName="col-xs-2" wrapperClassName="col-xs-10" onChange={this.updateLink.bind(this)} />
              </form>
            </Col>
          </Panel>
        </Row>
        <Row>
          <Col xs={4} md={4}>
            <ViewpointList viewpoints={this.state.modifiedSubject.viewpoints} viewpointSelected={this.selectViewpoint.bind(this)} />
          </Col>
          <Col xs={6} md={6}>
            <p>viewpoint list</p>
          </Col>
          <Col xs={2} md={2}>
            <p>commenter picker</p>
          </Col>


        </Row>

      </Grid>
    );
  }
}
