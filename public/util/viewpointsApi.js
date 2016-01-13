import Reqwest from 'reqwest';

export default {
  getAllCommenters: () => {
    return Reqwest({
      url: '/api/commenters',
      method: 'get'
    });
  },

  saveCommenter: (commenter) => {
    var url;

    if (commenter.id) {
      url = '/api/commenter/' + commenter.id;
    } else {
      url = '/api/commenter';
    }
    return Reqwest({
      url: url,
      data: JSON.stringify(commenter),
      contentType: 'application/json',
      method: 'put'
    });
  },

  getAllSubjects: () => {
    return Reqwest({
      url: '/api/subjects',
      method: 'get'
    });
  },

  getSubject: (id) => {
    return Reqwest({
      url: '/api/subject/' + id,
      method: 'get'
    });
  },

  saveSubjectGeneralInformation: (subject) => {
    var url;
    var command;
    if (subject.id) {
      url = '/api/subject/' + subject.id;
      command = {id: subject.id, name: subject.name, link: subject.link}
    } else {
      url = '/api/subject';
      command = {name: subject.name, link: subject.link};
    }

    return Reqwest({
      url: url,
      data: JSON.stringify(command),
      contentType: 'application/json',
      method: 'put'
    });
  },

  saveViewpoint: (subjectId, viewpoint) => {
    var url;
    var command;
    if (viewpoint.id) {
      url = '/api/subject/' + subjectId + '/viewpoint/' + viewpoint.id;
      command = {subjectId: subjectId, viewpointId: viewpoint.id, commenterId: viewpoint.commenter.id, quote: viewpoint.quote, link: viewpoint.link};
    } else {
      url = '/api/subject/' + subjectId + '/viewpoint';
      command = {subjectId: subjectId, commenterId: viewpoint.commenter.id, quote: viewpoint.quote, link: viewpoint.link};
    }

    return Reqwest({
      url: url,
      data: JSON.stringify(command),
      contentType: 'application/json',
      method: 'put'
    });
  }
}