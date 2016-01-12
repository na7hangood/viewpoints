import React from 'react';
import { Grid, Row, Col, Panel, Button, ButtonToolbar, Input, ButtonInput } from 'react-bootstrap';
import viewpointsApi from '../../util/viewpointsApi.js';

export default class SubjectEdit extends React.Component {

  constructor(props) {
    super(props);
    if (props.subject) {
      this.state = {modifiedSubject: Object.assign({}, props.subject)};
    } else {
      this.state = {modifiedSubject: {}};
    }
  }

  //componentWillReceiveProps(nextProps) {
  //  if (this.props.commenter !== nextProps.commenter) {
  //    if (nextProps.commenter) {
  //      this.state = {modifiedCommenter: Object.assign({}, nextProps.commenter)};
  //    } else {
  //      this.state = {modifiedCommenter: {}};
  //    }
  //  }
  //}

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

  render () {

    if (!this.props.subject) {
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
                <Button bsStyle="success" onClick={this.publishSubject.bind(this)} >Publish</Button>
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
        <Row></Row>

      </Grid>
    );
  }
}
