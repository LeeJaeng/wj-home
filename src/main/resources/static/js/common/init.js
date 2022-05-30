define(['common/userInfo', 'common/header'], function(userInfo, header){
    // 공통부분 한꺼번에 initialize
    function Init() {
    }

    Init.prototype.init = function({menu, callback}) {
        // 유저 정보 먼저 받아오고
        userInfo.getInitTokenFromCookie(function(){
            header.init(menu)
            if (!userInfo.isAdmin) {
                header.var.$menuList.find(".only-admin").remove()
            }
            if (!userInfo.loggedIn) {
                header.var.$menuList.find(".only-login").remove()
            }
            callback()
        });
    }


    return new Init();
});
