require.config({
    paths: {
        'common': '/js/common',
        'communityMain': '/js/community/communityMain'
    }
});
require(['common/init', 'common/userInfo', 'communityMain', 'common/gVar'] , function(init, userInfo, communityMain, gVar){
    if (gVar.isMobile) {
        if(window.location.pathname.search('/m/community') < 0){
            window.location.href = '/m' + window.location.pathname + window.location.search
        }
        $("#title-name").text('커뮤니티')
    }
    init.init({
        menu: 'community',
        callback: function(){
            communityMain.init()
        }
    });
});



