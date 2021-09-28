require.config({
    paths: {
        'common': '/js/common',
        'worshipMain': '/js/worship/worshipMain'
    }
});
require(['common/init', 'common/userInfo', 'worshipMain'] , function(init, userInfo, worshipMain){
    init.init({
        menu: 'worship',
        callback: function(){
            worshipMain.init()
        }
    });
});



