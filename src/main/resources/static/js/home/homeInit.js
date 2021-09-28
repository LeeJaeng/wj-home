require.config({
    paths: {
        'common': '/js/common',
        'homeMain': '/js/home/homeMain'
    }
});
require(['common/init', 'common/userInfo', 'homeMain'] , function(init, userInfo, homeMain){
    init.init({
        callback: function(){
            homeMain.init()
        }
    });
});



