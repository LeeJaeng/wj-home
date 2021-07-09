require.config({
    paths: {
        'common': '/js/common',
        'introduceMain': '/js/introduce/introduceMain'
    }
});
require(['common/init', 'common/userInfo', 'introduceMain'] , function(init, userInfo, introduceMain){
    init.init(function(){
        console.log(userInfo)
        introduceMain.init()
    });
});



