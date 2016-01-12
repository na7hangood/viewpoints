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
      url = '/api/commenter/' + commenter.id
    } else {
      url = '/api/commenter'
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
  }
}