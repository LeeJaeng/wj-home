define(['common/util'], function(util){
    function GVariable() {

        this.accessToken = util.getCookie("wj_gid");
        this.refreshToken = util.getCookie("wj_gid_r")


        this.data = {
            pastorList: [
                '예동열 담임목사',
                '김동훈 목사',
                '문지원 목사',
                '유수창 목사',
                '전윤광 목사',
                '정광복 목사',
                '김재준 전도사'
            ]
        }

    }

    return new GVariable();
});
