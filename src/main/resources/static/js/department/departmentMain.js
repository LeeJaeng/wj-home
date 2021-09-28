define([], function(){
    function DepartmentMain() {
        const $content = $("#content");
        this.var = {
            $content: $content,
            $menuList: $content.find(".menu-list"),
            menuSelected: 'infant'
        }

        this.menuSelectEvent()
    }

    DepartmentMain.prototype.init = function() {
    }

    DepartmentMain.prototype.menuSelectEvent = function(){
        const _this = this;

        this.var.$menuList.find(".menu").click(function(){
            const menu = $(this).data('value')
            if (_this.var.menuSelected === menu) {
                return false
            }
            _this.var.$menuList.find(".selected").removeClass("selected")
            $(this).addClass("selected")

            _this.var.menuSelected = menu
            _this.var.$content.find(".detail").addClass("hide")
            _this.var.$content.find(".detail").filter("." + menu).removeClass("hide")
        });
    }



    return new DepartmentMain();
});
