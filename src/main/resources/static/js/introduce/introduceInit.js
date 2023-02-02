require.config({
    paths: {
        'common': '/js/common',
        'introduceMain': '/js/introduce/introduceMain'
    }
});
require(['common/init', 'common/userInfo', 'introduceMain', 'common/gVar'] , function(init, userInfo, introduceMain, gVar) {
    if (gVar.isMobile) {
        if(window.location.pathname.search('/m/introduce') < 0){
            window.location.href = '/m' + window.location.pathname + window.location.search
        }
        $("#title-name").text('교회소개')
    }
    init.init({
        menu: 'introduce',
        callback: function(){
            introduceMain.init()
        }
    });
});



