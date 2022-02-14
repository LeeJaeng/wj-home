define([], function(){
    function DepartmentMain() {
        const $content = $("#content");
        this.var = {
            $content: $content,
            $menuList: $content.find(".menu-list"),
        }



        this.menuSelectEvent()
    }

    DepartmentMain.prototype.init = function() {
        this.var.menuSelected = $('[name=board_type]').val()
        this.var.selectedIdx = $('[name=board_idx]').val()
        $('.init-val-remove').remove()

        const $selected = this.var.$menuList.find(".menu[data-value="+ this.var.menuSelected +"]")
        $selected.addClass("selected")
        this.var.$content.find(".detail").addClass("hide")
        this.var.$content.find(".detail").filter("." + this.var.menuSelected).removeClass("hide")
    }

    DepartmentMain.prototype.menuSelectEvent = function(){
        const _this = this;

        this.var.$menuList.find(".menu").click(function(){
            const menu = $(this).data('value')
            // window.location.href = '/department?type=' + menu

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
