import React from 'react';
import { Grid, Row, Col, Panel, Button, ButtonToolbar, Input, ButtonInput, FormControls } from 'react-bootstrap';
import ViewpointList from './ViewpointList.react'
import ViewpointEdit from './ViewpointEdit.react'
import viewpointsApi from '../../util/viewpointsApi.js';

export default class SubjectEdit extends React.Component {

  constructor(props) {
    super(props);
    this.state = {modifiedSubject: {}, selectedViewpoint: undefined};

  }

  componentDidMount() {
    if(this.props.subjectId) {
      this.loadSubject(this.props.subjectId);
    }
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
    viewpointsApi.publishSubject(this.state.modifiedSubject.id).then(res => {
      this.setState({modifiedSubject: res});
    });
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

  selectViewpoint(viewpoint) {
    this.setState({selectedViewpoint: viewpoint});
  }

  showNewViewpointForm() {
    this.setState({selectedViewpoint: {}});
  }

  saveSelectedViewpoint(viewpoint) {
    viewpointsApi.saveViewpoint(this.state.modifiedSubject.id, viewpoint).then(res => {
      this.setState({modifiedSubject: res});
      this.setState({selectedViewpoint: undefined});
    });
  }

  cancelViewpointEdit() {
    this.setState({selectedViewpoint: undefined});
  }

  previewLink() {
    return '/preview/' + this.state.modifiedSubject.id;
  }

  render () {

    //if (!this.props.subjectId) {
    //  return false;
    //}

    var statusElement;
    if (this.state.modifiedSubject.id) {
      statusElement = (
        <FormControls.Static label="State" labelClassName="col-xs-2" wrapperClassName="col-xs-10">{this.state.modifiedSubject.state}</FormControls.Static>
      );
    } else {
      statusElement = (
        <FormControls.Static label="State" labelClassName="col-xs-2" wrapperClassName="col-xs-10">Creating</FormControls.Static>
      );
    }

    var embedElement;
    if (this.state.modifiedSubject.id) {
      embedElement = (
        <FormControls.Static label="Embed string" labelClassName="col-xs-2" wrapperClassName="col-xs-10">/atom/viewpoints/{this.state.modifiedSubject.id}</FormControls.Static>
      );
    } else {
      embedElement = (
        <FormControls.Static label="Embed string" labelClassName="col-xs-2" wrapperClassName="col-xs-10">Creating</FormControls.Static>
      );
    }

    var viewpointEditForm;

    if (this.state.selectedViewpoint) {
      viewpointEditForm = (
        <ViewpointEdit viewpoint={this.state.selectedViewpoint}
                       saveViewpoint={this.saveSelectedViewpoint.bind(this)}
                       cancelViewpointEdit={this.cancelViewpointEdit.bind(this)}/>
      );
    } else {
      viewpointEditForm = (
        <div>
          <p>Select a viewpoint to edit, or add a new one</p>
          <Button bsStyle="primary" onClick={this.showNewViewpointForm.bind(this)} >Add viewpoint</Button>
        </div>
      );
    }

    return (
      <Grid>
        <Row>
          <Panel header="General information">
            <Col xs={3} md={3}>
              <ButtonToolbar>
                <Button onClick={this.props.cancelSubjectEdit} >Back</Button>
                <Button href={this.previewLink()} target="_blank">Preview</Button>
              </ButtonToolbar>
              <ButtonToolbar>
                <Button bsStyle="primary" onClick={this.saveGeneralInformation.bind(this)} >Save</Button>
                <Button bsStyle="success" onClick={this.publishSubject.bind(this)} disabled={this.disablePublish()}>Publish</Button>
              </ButtonToolbar>
            </Col>
            <Col xs={9} md={9}>
              <form className="form-horizontal">
                {statusElement}
                {embedElement}
                <Input type="text" label="Name" value={this.state.modifiedSubject.name} labelClassName="col-xs-2" wrapperClassName="col-xs-10" onChange={this.updateName.bind(this)} />
                <Input type="text" label="Link" value={this.state.modifiedSubject.link} labelClassName="col-xs-2" wrapperClassName="col-xs-10" onChange={this.updateLink.bind(this)} />
              </form>
            </Col>
          </Panel>
        </Row>
        <Row>
          <Col xs={3} md={3}>
            <ViewpointList viewpoints={this.state.modifiedSubject.viewpoints} viewpointSelected={this.selectViewpoint.bind(this)} />
          </Col>
          <Col xs={9} md={9}>
            <Panel>{viewpointEditForm}</Panel>
          </Col>
        </Row>

      </Grid>
    );
  }
}
