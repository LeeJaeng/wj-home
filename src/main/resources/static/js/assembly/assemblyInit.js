require.config({
    paths: {
        'common': '/js/common',
    }
});
require(['common/init', 'common/userInfo', 'common/gVar'] , function(init, userInfo, gVar){
    // if (gVar.isMobile) {
    //     if(window.location.pathname !== '/m') {
    //         window.location.href = '/m'
    //     }
    // $("#title-name").text('총회관련 안내')
    // }
    init.init({
        callback: function(){
            const $tab = $('.wrapper .tab');
            const $content = $('.wrapper .content');
            $('.menu-list .menu').removeClass('selected');
            $('.menu-list .assembly').addClass('selected');
            $('.menu-list-detail').addClass('hide');

            $tab.find('.hotel').addClass('selected');
            $content.find('.hotel').show();


            $tab.find('.menu').click(function(){
                const $this = $(this);
                $tab.find('.selected').removeClass('selected')
                $content.find('.item').hide();
                if($this.hasClass('hotel')) {
                    $this.addClass('selected')
                    $content.find('.hotel').show();
                } else if($this.hasClass('restaurant')) {
                    $this.addClass('selected')
                    $content.find('.restaurant').show();
                } else if($this.hasClass('way')) {
                    $this.addClass('selected')
                    $content.find('.way').show();
                }
            });

            $content.find('.way').find('.station').click(function(){
                // 새창으로 열기
                window.open('https://map.naver.com/p/directions/14375574.153408017,4239069.541688124,%EC%9A%B8%EC%82%B0%EC%97%AD%20(%EA%B3%A0%EC%86%8D%EC%B2%A0%EB%8F%84),19542483,PLACE_POI/14399922.709406856,4240213.78124073,%EB%8C%80%ED%95%9C%EC%98%88%EC%88%98%EA%B5%90%EC%9E%A5%EB%A1%9C%ED%9A%8C%EC%9A%B0%EC%A0%95%EA%B5%90%ED%9A%8C,1787943369,PLACE_POI/-/transit?c=12.00,0,0,0,dh', '_blank');
            });
            $content.find('.way').find('.airport').click(function(){
                // 새창으로 열기
                window.open('https://map.naver.com/p/directions/14399815.2749663,4244752.6722234,%EC%9A%B8%EC%82%B0%EA%B3%B5%ED%95%AD,11576970,PLACE_POI/14399922.709406856,4240213.78124073,%EB%8C%80%ED%95%9C%EC%98%88%EC%88%98%EA%B5%90%EC%9E%A5%EB%A1%9C%ED%9A%8C%EC%9A%B0%EC%A0%95%EA%B5%90%ED%9A%8C,1787943369,PLACE_POI/-/transit?c=13.00,0,0,0,dh', '_blank');
            });
        }
    });
});



