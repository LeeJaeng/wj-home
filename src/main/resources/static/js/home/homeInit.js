require.config({
    paths: {
        'common': '/js/common',
        'homeMain': '/js/home/homeMain'
    }
});
require(['common/init', 'common/userInfo', 'homeMain', 'common/gVar'] , function(init, userInfo, homeMain, gVar){
    if (gVar.isMobile) {
        if(window.location.pathname !== '/m') {
            window.location.href = '/m'
        }
        $("#title-name").text('우정교회 모바일 홈')
    }
    init.init({
        callback: function(){
            homeMain.init()
        }
    });
});



