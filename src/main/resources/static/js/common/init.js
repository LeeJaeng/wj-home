define(['common/userInfo', 'common/header'], function(userInfo, header){
    // 공통부분 한꺼번에 initialize
    function Init() {
    }

    Init.prototype.init = function(callback) {
        // 유저 정보 먼저 받아오고
        userInfo.getInitTokenFromCookie(function(){
            header.init()
            callback()
        });
    }


    return new Init();
});
