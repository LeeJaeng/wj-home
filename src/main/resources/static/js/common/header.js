define(['common/loginPopup', 'common/userInfo'], function(loginPopup, userInfo){
    // 홈페이지 상단
    function Header() {
        this.var = {
            $mainMenu: $("#main-menu"),
            $userInfo: $("#user-info"),
        }
        this.var.$login = this.var.$userInfo.find(".login")
        this.var.$user = this.var.$userInfo.find(".user")
    }

    Header.prototype.init = function(menu) {
        if (userInfo.loggedIn) {
            this.userInit()
        }
        else {
            this.guestInit()
        }
        if (menu) {
            this.var.$mainMenu.find(".menu." + menu).addClass("selected")
        }
    }
    Header.prototype.userInit = function(){
        this.var.$login.remove()
        this.var.$user.find(".user-name").text(userInfo.userName + '님')
        this.var.$user.show()

        this.logoutBtnEvent()
    }
    Header.prototype.guestInit = function(){
        this.var.$user.remove()
        this.var.$login.show()
        this.loginBtnEvent()
    }

    Header.prototype.loginBtnEvent = function() {
        const _this = this;
        this.var.$login.click(function(){
            loginPopup.openPopup()
        });
    }
    Header.prototype.logoutBtnEvent = function() {
        const _this = this
        this.var.$user.find(".logout").click(function(){
            userInfo.logout()
        });
    }

    return new Header();
});
