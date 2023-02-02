require.config({
    paths: {
        'common': '/js/common',
        'worshipMain': '/js/worship/worshipMain'
    }
});
require(['common/init', 'common/userInfo', 'worshipMain', 'common/gVar'] , function(init, userInfo, worshipMain, gVar){
    if (gVar.isMobile) {
        if(window.location.pathname.search('/m/worship') < 0){
            window.location.href = '/m' + window.location.pathname + window.location.search
        }
        $("#title-name").text('말씀과 찬양')
    }
    init.init({
        menu: 'worship',
        callback: function(){
            worshipMain.init()
        }
    });
});



