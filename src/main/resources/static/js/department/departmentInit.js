require.config({
    paths: {
        'common': '/js/common',
        'departmentMain': '/js/department/departmentMain'
    }
});
require(['common/init', 'common/userInfo', 'departmentMain'] , function(init, userInfo, departmentMain){
        init.init({
            menu: 'department',
            callback: function(){
                departmentMain.init()
            }
        });
});



