define(['common/ajax'], function(ajax){
    // 공통부분 한꺼번에 initialize
    function AjaxRequest() {
    }

    
    AjaxRequest.prototype.registerWorshipBoard = function(params, callback) {
        ajax.ajaxCall({
            method: 'POST',
            uri: '/user/board/worship',
            data: JSON.stringify(params),
            onSuccessCallback: function(json) {
                callback(json)
            },
            onErrorCallback: function(json) {
            }
        })
    }

    AjaxRequest.prototype.editWorshipBoard = function(params, callback) {
        ajax.ajaxCall({
            method: 'PUT',
            uri: '/user/board/worship',
            data: JSON.stringify(params),
            onSuccessCallback: function(json) {
                callback(json)
            },
            onErrorCallback: function(json) {
            }
        })
    }

    AjaxRequest.prototype.deleteWorshipBoard = function({idx, callback}) {
        ajax.ajaxCall({
            method: 'DELETE',
            uri: '/user/board/worship/' + idx,
            onSuccessCallback: function(json) {
                callback(json)
            },
            onErrorCallback: function(json) {
            }
        })
    }

    AjaxRequest.prototype.getWorshipBoardList = function({type, page, init, callback}) {
        ajax.ajaxCall({
            method: 'GET',
            uri: '/user/board/worship?type=' + type + "&page=" + page + "&init=" + (init ? 'true' : 'false'),
            onSuccessCallback: function(json) {
                callback(json)
            },
            onErrorCallback: function(json) {
            }
        })
    }

    AjaxRequest.prototype.getWorshipBoard = function({idx, callback}) {
        ajax.ajaxCall({
            method: 'GET',
            uri: '/user/board/worship/' + idx,
            onSuccessCallback: function(json) {
                callback(json)
            },
            onErrorCallback: function(json) {

            }
        })
    }

    AjaxRequest.prototype.registerCommunityBoard = function(formData, callback) {
        ajax.ajaxFormDataCall({
            uri: '/user/board/community',
            data: formData,
            onSuccessCallback: function(json) {
                callback(json)
            },
            onErrorCallback: function(json) {
            }
        })
    }

    AjaxRequest.prototype.editCommunityBoard = function(formData, callback) {
        ajax.ajaxFormDataCall({
            uri: '/user/board/community/edit',
            data: formData,
            onSuccessCallback: function(json) {
                callback(json)
            },
            onErrorCallback: function(json) {
            }
        })
    }

    AjaxRequest.prototype.deleteCommunityBoard = function({idx, callback}) {
        ajax.ajaxCall({
            method: 'DELETE',
            uri: '/user/board/community/' + idx,
            onSuccessCallback: function(json) {
                callback(json)
            },
            onErrorCallback: function(json) {
            }
        })
    }

    AjaxRequest.prototype.getCommunityBoardList = function({type, page, init, callback}) {
        ajax.ajaxCall({
            method: 'GET',
            uri: '/user/board/community?type=' + type + "&page=" + page + "&init=" + (init ? 'true' : 'false'),
            onSuccessCallback: function(json) {
                callback(json)
            },
            onErrorCallback: function(json) {
            }
        })
    }

    AjaxRequest.prototype.getCommunityBoard = function({idx, callback}) {
        ajax.ajaxCall({
            method: 'GET',
            uri: '/user/board/community/' + idx,
            onSuccessCallback: function(json) {
                callback(json)
            },
            onErrorCallback: function(json) {
            }
        })
    }


    // hidden

    AjaxRequest.prototype.getPrayerList = function({groupKey, callback}) {
        ajax.ajaxCall({
            method: 'GET',
            uri: '/user/hidden/prayer/' + groupKey,
            onSuccessCallback: function(json) {
                callback(json)
            },
            onErrorCallback: function(json) {

            }
        })
    }
    AjaxRequest.prototype.registerPrayer = function({params, callback}) {
        ajax.ajaxCall({
            method: 'post',
            data: JSON.stringify(params),
            uri: '/user/hidden/prayer/content',
            onSuccessCallback: function(json) {
                callback(json)
            },
            onErrorCallback: function(json) {
            }
        })
    }

    return new AjaxRequest();
});
