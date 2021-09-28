define(
    ['common/loginPopup', 'common/ajaxRequests', 'common/util'],
    function(loginPopup, ajaxRequests, util){
        function HomeMain() {
            this.var = {
                $mainPreach: $(".main-preach")
            }
        }

        HomeMain.prototype.init = function() {
            this.getHeadPastorSermon()
            this.getPaper()
            this.getWorship()
            this.getPhoto()
        }

        HomeMain.prototype.getHeadPastorSermon = function() {
            const _this = this
            const $preachList = this.var.$mainPreach.find(".preach-list").find(".li")
            // 첫 데이터 받기
            ajaxRequests.getWorshipBoardList({
                type: 'head-pastor',
                page: 1,
                init: true,
                callback: function(json) {
                    $.each(json.list, function(i, v){
                        const $row = $(
                            '<div class="l-row">' +
                            '   <div><span class="date">'+ v.date  +'</span><span class="type">'+ util.getWorshipName(v.worship_type) +'</span></div>' +
                            '   <div><span class="title">'+ v.title  +'</span></div>' +
                            '   <div><span class="verse">'+ v.verse  +'</span></div>' +
                            '</div>'
                        )
                        $preachList.append($row)

                        if (i === 0) {
                            $row.addClass("selected")
                            setMainSermon(v)
                        }

                        $row.click(function(){
                            if ($row.hasClass("selected")){
                                return false
                            }
                            $preachList.find('.selected').removeClass("selected")
                            $row.addClass("selected")
                            setMainSermon(v)
                        })
                    })
                }

            });

            function setMainSermon (data) {
                const $preachPlayer = _this.var.$mainPreach.find(".preach-player")

                let date = ''
                let preachTitle = ''
                let preachVerse = ''

                if (data.date)
                    date = data.date + " " + util.getWorshipName(data.worship_type)

                if (data.title)
                    preachTitle = data.title

                if (data.verse)
                    preachVerse = data.verse

                $preachPlayer.find(".date").text(date)
                $preachPlayer.find(".preach-title").text(preachTitle)
                $preachPlayer.find(".preach-verse").text(preachVerse)

                $preachPlayer.find("iframe").remove()
                if (data.content) {
                    $preachPlayer.find(".player").append(
                        data.content
                    )
                }
            }
        }

        HomeMain.prototype.getPaper = function() {
            const $paperBoardList = $(".board.paper").find(".list")

            ajaxRequests.getCommunityBoardList({
                type: 'paper',
                page: 1,
                init: true,
                callback: function(json) {
                    $.each(json.list, function(i, v){
                        const $row = $(
                            '<div class="l-row">' +
                            '   <div class="title">'+ v.title +'</div>' +
                            '   <div class="date">'+ v.date +'</div>' +
                            '</div>'
                        )
                        $row.click(function(){
                            window.location.href = '/community?type=paper&idx=' + v.board_idx
                        })
                        $paperBoardList.append($row)
                    })
                }
            });
        }

        HomeMain.prototype.getWorship = function() {
            const $boardList = $(".board.worship").find(".list")
            ajaxRequests.getWorshipBoardList({
                type: 'home-worship',
                page: 1,
                init: true,
                callback: function(json) {
                    console.log(json)
                    $.each(json.list, function(i, v){
                        const $row = $(
                            '<div class="l-row">' +
                            '   <div class="title">'+ v.title +'</div>' +
                            '   <div class="date">'+ v.date +'</div>' +
                            '</div>'
                        )
                        /*
                                    <div class="menu" data-value="head-pastor">담임목사 설교</div>
            <div class="menu" data-value="sub-pastor">부교역자 설교</div>
            <div class="menu" data-value="sunday">주일예배</div>
            <div class="menu" data-value="friday">금요성령집회</div>
            <div class="menu" data-value="wednesday">수요예배</div>
            <div class="menu" data-value="dawn">새벽예배</div>
            <div class="menu" data-value="praise">찬양</div>
            <div class="menu" data-value="etc">기타예배</div>
                         */
                        let type = ''
                        switch (v.category) {
                            case 1:
                                type = 'head-pastor'
                                break
                            case 2:
                                type = 'sub-pastor'
                                break
                            case 4:
                                type = 'praise'
                                break
                            case 3:
                                switch (v.worship_type) {
                                    case 'fri':
                                        type = 'friday'
                                        break
                                    case 'wed1':
                                    case 'wed2':
                                        type = 'wednesday'
                                        break
                                    case 'dawn':
                                        type = 'dawn'
                                        break
                                    case 'etc':
                                        type = 'etc'
                                        break
                                    default:
                                        type = 'sunday'
                                }
                                break

                        }


                        $row.click(function(){
                            window.location.href = '/worship?type='+ type +'&idx=' + v.board_idx
                        })

                        $boardList.append($row)
                    })
                }
            });

        }

        HomeMain.prototype.getPhoto = function() {
            const $boardList = $(".board.picture").find(".pic-area")

            ajaxRequests.getCommunityBoardList({
                type: 'photo',
                page: 1,
                init: true,
                callback: function(json) {
                    $.each(json.list, function(i, v){
                        if (i === 3)
                            return false

                        const $row = $(
                            '<div>' +
                            '   <div class="title">'+ v.title +'</div>' +
                            '   <div class="pic"><img src="'+ v.thumb +'" alt=""></div>' +
                            '</div>'
                        )
                        $row.click(function(){
                            window.location.href = '/community?type=photo&idx=' + v.board_idx
                        })

                        $boardList.append($row)
                    })
                }
            });
        }


        return new HomeMain();
    });
