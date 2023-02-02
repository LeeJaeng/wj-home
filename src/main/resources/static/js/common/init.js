define(['common/userInfo', 'common/header', 'common/gVar'], function(userInfo, header, gVar){
    // 공통부분 한꺼번에 initialize
    function Init() {
    }

    Init.prototype.init = function({menu, callback}) {
        // 유저 정보 먼저 받아오고
        userInfo.getInitTokenFromCookie(function(){
            if (gVar.isMobile){
                header.mobileInit()
                // $(".only-admin").remove()
                // $(".only-login").remove()
            } else {
                header.init(menu)
                if (!userInfo.isAdmin) {
                    header.var.$menuList.find(".only-admin").remove()
                }
                if (!userInfo.loggedIn) {
                    header.var.$menuList.find(".only-login").remove()
                }
            }
            callback()
        });

        if (gVar.isMobile) {
            this.topMenuEvent();
        }
    }

    Init.prototype.topMenuEvent = function() {
        var $mainMenu = $("#main-menu")
        $("#header").find('.menu-hamburger').click(function(){
            $("#main-menu").show();
            $("#main-menu-background").show();
        })

        $("#main-menu-background").click(function(){
            $("#main-menu").hide();
            $("#main-menu-background").hide();
        });

        $mainMenu.find(".menu-list").find(".menu").click(function(){
            var value = $(this).data('value')

            $(this).siblings().removeClass('selected')
            $(this).addClass("selected")

            $mainMenu.find(".menu-list-detail").find('.column').addClass('hide')
            $mainMenu.find(".menu-list-detail").find('.column.' + value).removeClass('hide')
        });
        $mainMenu.find(".menu-list-detail").find(".column").find('div').click(function(){
            window.location.href = $(this).data('url')
        })
    }


    return new Init();
});
