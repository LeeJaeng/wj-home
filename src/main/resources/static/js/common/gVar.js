define(['common/util'], function(util){
    function GVariable() {

        this.accessToken = util.getCookie("wj_gid");
        this.refreshToken = util.getCookie("wj_gid_r")


        this.data = {
            pastorList: [
                '예동열 담임목사',
                '변재훈 원로목사',
                '문지원 목사',
                '김정길 목사',
                '정지수 목사',
                '조성필 목사',
                '이세형 전도사'
            ],
        }

        this.isMobile = util.isMobile()
        this.deviceWidth = $('body').width()
    }

    return new GVariable();
});
