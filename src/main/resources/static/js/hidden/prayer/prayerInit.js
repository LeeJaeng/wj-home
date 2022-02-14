require.config({
    paths: {
        'common': '/js/common',
        'prayerMain': '/js/hidden/prayer/prayerMain'
    }
});
require(['prayerMain'] , function(prayerMain){
    prayerMain.init()
});



