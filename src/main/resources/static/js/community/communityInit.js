require.config({
    paths: {
        'common': '/js/common',
        'communityMain': '/js/community/communityMain'
    }
});
require(['common/init', 'common/userInfo', 'communityMain'] , function(init, userInfo, communityMain){
    init.init({
        menu: 'community',
        callback: function(){
            communityMain.init()
        }
    });
});



