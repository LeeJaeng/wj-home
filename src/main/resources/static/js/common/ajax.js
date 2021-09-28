define(['common/gVar'], function(gVar){
    function Ajax(){
        this.defautOption = {
            method: 'GET',
            data: '',
            headers: {
                'Authorization':  "Bearer " + gVar.accessToken,
                'Content-Type': 'application/json'
            }
        };
        this.headers = {};
    }

    Ajax.prototype.init = function() {
    };
    Ajax.prototype.setHeaders = function(headers){
        const _this = this;
        this.headers = {};
        $.each(this.defautOption.headers, function(k, v){
            if (v === 'Bearer ')
                return true;
            _this.headers[k] = v;
        });
        if (headers !== undefined){
            $.each(headers, function(k, v){
                _this.headers[k] = v;
            });
        }
    };
    Ajax.prototype.ajaxCall = function(option){
        const _this = this;
        this.setHeaders(option.header);
        // var host = option.host === undefined ? _var.apiHost : option.host;
        let host = option.host === undefined ? '/api/v1' : option.host;
        let method = option.method === undefined ? this.defautOption.method : option.method;
        let data = option.data === undefined ? this.defautOption.data :option.data;
        let dataType = option.dataType === undefined ? 'json' :option.dataType;
        let uri = option.uri;
        let onSuccess = option.onSuccessCallback;
        let onError = option.onErrorCallback;
        let onComplete = option.onCompleteCallback;

        $.ajax({
            type: method,
            url: host + uri,
            dataType: 'json',
            data: data,
            beforeSend: function(xhr){
                $.each(_this.headers, function(k, v){
                    xhr.setRequestHeader(k, v);
                });
            },
            complete: function(){
                if (typeof onComplete === 'function')
                    onComplete();
            },
            error: function(xhr){
                //Custom Code Action
                if (xhr.status === 901) {
                    if(xhr.responseJSON.redirection_url){
                        window.location.href = xhr.responseJSON.redirection_url;
                    }else{
                    }
                }else if( xhr.status === 902) {
                    window.location = "/logout";
                }


                if (onError === undefined){
                    if (xhr.status === 400){
                        console.log('400 ERROR');
                        console.log(xhr.responseJSON.message);
                    }
                    else if (xhr.status === 401){
                        console.log('401 ERROR');
                        console.log(xhr);
                        // window.location = "";
                    }
                    else if (xhr.status === 403) {
                        if (xhr.responseText !== undefined)
                            alert(xhr.responseText.replace("<br/>", "\n"));
                    }
                    else if (xhr.status === 500) {
                        alert("오류가 발생했습니다. 문의 부탁드립니다.");
                        // console.log('500 ERROR');
                        // console.log(xhr.responseJSON.message);
                    }
                }
                else {
                    if (typeof onError === 'function'){
                        onError(xhr);
                    }
                }
            },
            success: function(json){
                if (typeof onSuccess === 'function') {
                    onSuccess(json);
                }
            }
        });
    };

    Ajax.prototype.ajaxFormDataCall = function(option) {
        const _this = this;
        this.setHeaders(option.header);
        // var host = option.host === undefined ? _var.apiHost : option.host;
        let host = option.host === undefined ? '/api/v1' : option.host;
        let method = option.method === undefined ? this.defautOption.method : option.method;
        let data = option.data === undefined ? this.defautOption.data :option.data;
        let dataType = option.dataType === undefined ? 'json' :option.dataType;
        let uri = option.uri;
        let onSuccess = option.onSuccessCallback;
        let onError = option.onErrorCallback;
        let onComplete = option.onCompleteCallback;
        $.ajax({
            url: host + uri,
            type: 'post',
            contentType: false,
            processData: false,
            data: data,
            beforeSend: function(xhr){
                xhr.setRequestHeader('Authorization', "Bearer " + gVar.accessToken)
            },
            complete: function(){
                if (typeof onComplete === 'function')
                    onComplete();
            },
            error: function(xhr){
                //Custom Code Action
                if (xhr.status === 901) {
                    if(xhr.responseJSON.redirection_url){
                        window.location.href = xhr.responseJSON.redirection_url;
                    }else{
                    }
                }else if( xhr.status === 902) {
                    window.location = "/logout";
                }


                if (onError === undefined){
                    if (xhr.status === 400){
                        console.log('400 ERROR');
                        console.log(xhr.responseJSON.message);
                    }
                    else if (xhr.status === 401){
                        console.log('401 ERROR');
                        console.log(xhr);
                        // window.location = "";
                    }
                    else if (xhr.status === 403) {
                        if (xhr.responseText !== undefined)
                            alert(xhr.responseText.replace("<br/>", "\n"));
                    }
                    else if (xhr.status === 500) {
                        alert("오류가 발생했습니다. 문의 부탁드립니다.");
                        // console.log('500 ERROR');
                        // console.log(xhr.responseJSON.message);
                    }
                }
                else {
                    if (typeof onError === 'function'){
                        onError(xhr);
                    }
                }
            },
            success: function(json){
                if (typeof onSuccess === 'function') {
                    onSuccess(json);
                }
            }
        });
    }


    const ajax = new Ajax();
    ajax.init();
    return {
        ajaxCall: function(option){
            ajax.ajaxCall(option)
        },
        ajaxFormDataCall: function(option){
            ajax.ajaxFormDataCall(option)
        }
    };
});
