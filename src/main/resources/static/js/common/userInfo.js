define(['common/util', 'common/ajax'], function(util, ajax){
    function UserInfo() {
        this.loggedIn = false
        this.isAdmin = false
        this.isPraise = false
        this.userName = ""
        this.userId = ""

        this.refreshToken = {}
    }

    UserInfo.prototype.init = function(callback){
        this.getInitTokenFromCookie(callback)
    }
    UserInfo.prototype.getInitTokenFromCookie = function(callback) {
        const token = util.getCookie('wj_gid')
        const refreshToken = util.getCookie('wj_gid')

        if (token === '') {
            if (typeof callback === 'function')
                callback()
            return false;
        }
        const tokenObj = util.decodeJwt(token);
        const refreshTokenObj = util.decodeJwt(refreshToken);

        if (Date.now() >= tokenObj.exp * 1000) {
            console.log("token expired");
            if (Date.now() >= refreshTokenObj.exp * 1000) {
                this.logout()
            } else {
                // token refresh g
            }
        } else {
            this.loggedIn = true;
            this.isAdmin = tokenObj.scopes.find(ele => ele === 'ADMIN') !== undefined
            this.isPraise = tokenObj.scopes.find(ele => ele === 'PRAISE') !== undefined
            this.userName = tokenObj.user_name
            if (typeof callback === 'function')
                callback()
        }


    }

    UserInfo.prototype.login = function (id, pw) {
        ajax.ajaxCall({
            method: 'POST',
            host: '/auth/login',
            uri: '',
            data: JSON.stringify({
                user_id: id,
                password: pw
            }),
            onSuccessCallback: function(json) {
                // console.log(json)
                util.setCookie("wj_gid", json.token, 240)
                util.setCookie("wj_gid_r", json.refresh_token, 240)
                window.location.reload()
            },
            onErrorCallback: function(json) {
                alert("아이디 또는 비밀번호를 확인해주세요.");
            }
        })
    }
    UserInfo.prototype.logout = function () {
        util.setCookie('wj_gid', null, 0)
        util.setCookie('wj_gid_r', null, 0)
        window.location.reload()
    }

    return new UserInfo();
});
