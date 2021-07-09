define(['common/userInfo', 'common/util'], function(userInfo, util){
    function LoginPopup() {
        this.var = {
            $popup: $("#login-popup"),   // header.html
            $userId: $("#user_id"),
            $password: $("#password"),
            $loginBtn: $("#login-btn")
        }


        // event 정의
        this.loginBtnClickEvent();
        this.closePopupEvent();
    }

    LoginPopup.prototype.openPopup = function(){
        this.var.$userId.val('');
        this.var.$password.val('');
        this.var.$popup.show();
    }

    LoginPopup.prototype.loginBtnClickEvent = function(){
        const _this = this;
        this.var.$loginBtn.click(function(){
            const id = _this.var.$userId.val();
            const pw = _this.var.$password.val();
            if ( id === '' || pw === '') {
                alert("아이디와 비밀번호를 입력해주세요");
            } else {
                userInfo.login(id, pw);
            }

        });
        this.var.$password.keyup(function(e){
            if (e.keyCode === 13) {
                _this.var.$loginBtn.click();
            }
        });
    }
    LoginPopup.prototype.closePopupEvent = function(){
        const _this = this;
        this.var.$popup.click(function(){
            _this.var.$popup.hide();
        });
        this.var.$popup.find(".window").find(".close").click(function(){
            _this.var.$popup.hide();
        });
        this.var.$popup.find(".window").click(function(e){
            e.stopPropagation()
        });
    }



    return new LoginPopup();
});
