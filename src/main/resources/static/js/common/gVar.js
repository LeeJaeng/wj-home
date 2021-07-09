define(['common/util'], function(util){
    function GVariable() {

        this.accessToken = util.getCookie("wid");
        this.refreshToken = util.getCookie("wid_r")

    }

    return new GVariable();
});
