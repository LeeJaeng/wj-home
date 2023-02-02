define(
    ['common/loginPopup', 'common/ajaxRequests', 'common/util', 'common/gVar'],
    function(loginPopup, ajaxRequests, util, gVar){
        function HomeMain() {
            this.var = {
                $mainPreach: $(".list.sermon"),
                $bannerSlide: $(".swiper-wrapper"),
            }
        }

        HomeMain.prototype.init = function() {
            const _this = this;
            ajaxRequests.getMainBanners({
                callback: function(json) {
                    json.data.push(
                        {url: 'https://kor-wj-bucket.s3.ap-northeast-2.amazonaws.com/images/banner/wj_church.png'},
                    )
                    if (!gVar.isMobile) {
                        json.data.push(
                            {url: 'https://kor-wj-bucket.s3.ap-northeast-2.amazonaws.com/images/banner/wj.png'},
                        )
                    }
                    $.each(json.data, function(i, v){
                        const $img = $(
                            '<div class="swiper-slide"></div>'
                        )
                        $img.css('background-image', "url('"+ v.url +"')")
                        _this.var.$bannerSlide.append($img)
                    });



                    const swiper = new Swiper(".mySwiper", {
                        navigation: {
                            nextEl: ".swiper-button-next",
                            prevEl: ".swiper-button-prev",
                        },
                        pagination: {
                            el: ".swiper-pagination",
                            clickable: true,
                            renderBullet: function (index, className) {
                                return '<span class="' + className + '">' + "</span>";
                            },
                        },
                        autoplay: {
                            delay: 6000,
                            disableOnInteraction: false,
                        },
                        // scrollbar: {
                        //     el: ".swiper-scrollbar",
                        //     hide: true,
                        // },
                    });
                }
            })
            this.getSermon()
            this.getPaper()
            this.getWorship()
            this.getPhoto()
        }

        HomeMain.prototype.getSermon = function() {
            const _this = this
            const $preachList = this.var.$mainPreach.find(".preach-list").find(".li")
            // 첫 데이터 받기
            ajaxRequests.getWorshipBoardList({
                type: 'head-pastor-main',
                page: 1,
                init: true,
                callback: function (json) {
                    // console.log(json);
                    $.each(json.list, function (i, v) {
                        setSermon('main', v)
                        if (i === 1)
                            return false;
                    })
                }
            });

            function setSermon (type, data) {
                const $preachPlayer = _this.var.$mainPreach;

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
                $preachPlayer.find(".preach-verse").find(".verse").text(preachVerse)
                if (data.content) {
                    const div = $('<div></div>');
                    const iframe = $(data.content)
                    if (gVar.isMobile) {
                        const width = gVar.deviceWidth - 40
                        const height = Math.floor(width / 1.6)
                        iframe.attr('width', width + '');
                        iframe.attr('height', height + '');
                    } else {
                        iframe.attr('width', '400');
                        iframe.attr('height', '250');
                    }
                    div.append(iframe);
                    $preachPlayer.append(div)
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
                        if (gVar.isMobile) {
                            $row.click(function(){
                                window.location.href = '/m/community?type=paper&idx=' + v.board_idx
                            })
                        } else {
                            $row.click(function(){
                                window.location.href = '/community?type=paper&idx=' + v.board_idx
                            })
                        }
                        $paperBoardList.append($row)

                        if (gVar.isMobile && i === 4) {
                            return false;
                        }
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
                    $.each(json.list, function(i, v){
                        const $row = $(
                            '<div class="l-row">' +
                            '   <div class="title">'+ v.title +'</div>' +
                            '   <div class="date">'+ v.date +'</div>' +
                            '</div>'
                        )
                        /*
                                    <div class="menu" data-value="head-pastor">담임목사 설교</div>
            <div class="menu" data-value="sub-pastor">설교</div>
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
                        if (gVar.isMobile) {
                            if (i === 8)
                                return false
                        } else {
                            if (i === 4)
                                return false
                        }

                        const $row = $(
                            '<div>' +
                            // '<div style="background-image: url(' + v.thumb + ')">' +
                            '   <div class="pic">' +
                            '       <img src="'+ v.thumb +'" alt="">' +
                            '       <div class="title">'+ v.title +'</div>' +
                            '   </div>' +
                            '</div>'
                        )
                        $row.click(function(){
                            if (gVar.isMobile) {
                                window.location.href = '/m/community?type=photo&idx=' + v.board_idx
                            } else {
                                window.location.href = '/community?type=photo&idx=' + v.board_idx
                            }
                        })

                        $boardList.append($row)
                    })
                }
            });
        }


        return new HomeMain();
    });
