define(
    ['common/ajaxRequests', 'common/util'],
    function (ajaxRequests, util) {
        function PrayerMain() {
            this.groupKey = $('[name=group_key]').val()
            this.var = {
                $list: $(".list"),
                $registerPopup: $("#registerPopup")
            }

            this.registerParams = {
                group_idx: null,
                prayer_idx: null,
                content: ''
            }

            $('.init-val-remove').remove()
        }


        PrayerMain.prototype.init = function () {
            this.closeRegisterPopupEvent()
            this.registerPopupEvent()
            this.getList();
        }

        PrayerMain.prototype.getList = function () {
            const _this = this;
            ajaxRequests.getPrayerList({
                groupKey: _this.groupKey,
                callback: function(data) {
                    _this.fillList(data.data);
                }
            })
        }
        PrayerMain.prototype.clearList = function () {
            this.var.$list.find('.l-row').remove();
        }
        PrayerMain.prototype.fillList = function (list) {
            const _this = this;
            $.each(list, function(i, v){
                const $row = $("<div class='l-row'></div>")
                const content = v.content ? v.content.replace(/\n/gi, "<br>") : "";
                $row.append(
                    '<div class="label">'+ v.prayer_name +'</div>' +
                    '<div class="content '+ (content === '' ? 'empty': '') +'">'+ (content === '' ? '눌러서 기도제목을 등록해주세요' : content )+'</div>'
                )
                $row.find(".content").click(function(){
                    _this.openRegisterPopup(v);
                });
                _this.var.$list.append($row);
            });
        }
        PrayerMain.prototype.openRegisterPopup = function (data) {
            this.var.$registerPopup.removeClass("hide");
            this.registerParams.group_idx = data.group_idx;
            this.registerParams.prayer_idx = data.prayer_idx;
            this.var.$registerPopup.find("textarea").val(data.content);
            this.var.$registerPopup.find("textarea").focus()
        }
        PrayerMain.prototype.closeRegisterPopupEvent = function () {
            const _this = this;
            this.var.$registerPopup.find(".cancel").click(function(){
                _this.var.$registerPopup.addClass("hide");
            });
        }
        PrayerMain.prototype.registerPopupEvent = function () {
            const _this = this;
            this.var.$registerPopup.find(".register").click(function(){
                _this.registerParams.content = _this.var.$registerPopup.find("textarea").val()
                ajaxRequests.registerPrayer(
                    {
                        params: _this.registerParams,
                        callback: function() {
                            _this.clearList();
                            _this.getList();
                            _this.var.$registerPopup.addClass("hide");
                        }
                    }
                )
            });
        }

        return new PrayerMain();
});
