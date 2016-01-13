import React from 'react';
import { Panel, Row, Col, Input, ButtonInput, FormControls } from 'react-bootstrap';
import CommenterList from './CommenterList.react'
import viewpointsApi from '../../util/viewpointsApi.js';

export default class ViewpointEdit extends React.Component {

  constructor(props) {
    super(props);
    if (props.viewpoint) {
      this.state = {modifiedViewpoint: Object.assign({}, props.viewpoint), showCommenterPicker: false, commenters: []};
    } else {
      this.state = {modifiedViewpoint: {}, showCommenterPicker: false, commenters: []};
    }
  }

  componentWillReceiveProps(nextProps) {
    console.log('viewpoint editor getting props', this.props.viewpoint, nextProps.viewpoint);
    if (this.props.viewpoint !== nextProps.viewpoint) {
      if (nextProps.viewpoint) {
        this.setState({modifiedViewpoint: Object.assign({}, nextProps.viewpoint), showCommenterPicker: false});
      } else {
        this.setState({modifiedViewpoint: {}, showCommenterPicker: false});
      }
    }
  }

  componentDidMount() {
    this.fetchCommenters();
  }

  fetchCommenters() {
    viewpointsApi.getAllCommenters().then(res => {
      this.setState({commenters: res});
    });
  }

  updateLink(e) {
    const updated = this.state.modifiedViewpoint;
    updated.link = e.target.value;
    this.setState({modifiedViewpoint: updated});
  }

  updateQuote(e) {
    const updated = this.state.modifiedViewpoint;
    updated.quote = e.target.value;
    this.setState({modifiedViewpoint: updated});
  }

  showCommenterPicker() {
    this.setState({showCommenterPicker: true});
  }

  hideCommenterPicker() {
    this.setState({showCommenterPicker: false});
  }

  commenterPicked(commenter) {
    const updated = this.state.modifiedViewpoint;
    updated.commenter = commenter;
    this.setState({modifiedViewpoint: updated});

    this.hideCommenterPicker();
  }

  saveModifications() {
    this.props.saveViewpoint(this.state.modifiedViewpoint);
  }

  render() {

    if (!this.props.viewpoint) {
      return false;
    }

    var commenterNode;

    if(this.state.modifiedViewpoint.commenter) {
        commenterNode = (
          <div>
            <FormControls.Static label="Commenter" labelClassName="col-xs-2" wrapperClassName="col-xs-10">{this.state.modifiedViewpoint.commenter.name}</FormControls.Static>
            <ButtonInput bsStyle="primary" onClick={this.showCommenterPicker.bind(this)} wrapperClassName="col-xs-offset-2 col-xs-10" >Update commenter</ButtonInput>
          </div>
        );
    } else {
      commenterNode = (
        <ButtonInput bsStyle="primary" onClick={this.showCommenterPicker.bind(this)} wrapperClassName="col-xs-offset-2 col-xs-10" >Add commenter</ButtonInput>
      );
    }

    var commenterPicker;

    if(this.state.showCommenterPicker) {
      commenterPicker= (<CommenterList commenters={this.state.commenters} commenterSelected={this.commenterPicked.bind(this)} />);
    } else {
      commenterPicker = (<div></div>);
    }
    return (

        <Row className="show-grid">
          <Col xs={8} md={8}>
            <form className="form-horizontal">
              {commenterNode}
              <Input type="textarea" label="Quote" value={this.state.modifiedViewpoint.quote} labelClassName="col-xs-2" wrapperClassName="col-xs-10" onChange={this.updateQuote.bind(this)}/>
              <Input type="text" label="Link" value={this.state.modifiedViewpoint.link} labelClassName="col-xs-2" wrapperClassName="col-xs-10" onChange={this.updateLink.bind(this)}/>
              <ButtonInput bsStyle="primary" onClick={this.saveModifications.bind(this)} wrapperClassName="col-xs-offset-2 col-xs-10">Save viewpoint</ButtonInput>
              <ButtonInput bsStyle="primary" onClick={this.props.cancelViewpointEdit} wrapperClassName="col-xs-offset-2 col-xs-10">Cancel</ButtonInput>
            </form>
          </Col>
          <Col xs={4} md={4}>{commenterPicker}</Col>
        </Row>
    );
  }

}