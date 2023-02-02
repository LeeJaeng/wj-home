require.config({
    paths: {
        'common': '/js/common',
        'departmentMain': '/js/department/departmentMain'
    }
});
require(['common/init', 'common/userInfo', 'departmentMain', 'common/gVar'] , function(init, userInfo, departmentMain, gVar){
    if (gVar.isMobile) {
        if(window.location.pathname.search('/m/department') < 0){
            window.location.href = '/m' + window.location.pathname + window.location.search
        }
        $("#title-name").text('교육부서')
    }
    init.init({
        menu: 'department',
        callback: function(){
            departmentMain.init()
        }
    });
});



