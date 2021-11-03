define(
    ['common/gVar', 'common/userInfo', 'common/ajaxRequests', "common/paging", 'common/util'],
    function (gVar, userInfo, ajaxRequests, paging, util) {

        function WorshipMain() {
            const $content = $("#content");
            this.var = {
                $content: $content,
                $detail: $content.find(".detail"),
                $boardDetail: $content.find(".board-detail"),
                $menuList: $content.find(".menu-list"),
                $register: $("#register"),
                $registerPopup: $("#registerPopup"),
                menuSelected: 'head-pastor'
            }


            this.var.menuSelected = $('[name=board_type]').val()
            this.var.selectedIdx = $('[name=board_idx]').val()
            $('.init-val-remove').remove()

            this.menuSelectEvent()
        }

        WorshipMain.prototype.init = function () {
            const _this = this
            if (!userInfo.isAdmin) {
                this.var.$register.remove()
            } else {
                this.registerInit()
            }

            // 초기 세팅
            const $selected = this.var.$menuList.find(".menu[data-value="+ this.var.menuSelected +"]")
            $selected.addClass("selected")
            this.var.$detail.find('.sub-title').text($selected.text())


            // 첫 데이터 받기
            ajaxRequests.getWorshipBoardList({
                type: this.var.menuSelected,
                page: 1,
                init: true,
                callback: function(json) {
                    paging.setTopNode(_this.var.$detail.find(".paging"))
                    paging.setPagingInfo(json.paging)
                    paging.drawPaging()
                    paging.setPageEventFunction(function(page){
                        ajaxRequests.getWorshipBoardList(
                            {
                                type: _this.var.menuSelected,
                                page: page,
                                init: false,
                                callback: function(json){
                                    _this.clearList()
                                    _this.setList(json.list)
                                }
                            }
                        )
                    })
                    _this.setList(json.list)
                }
            });

            _this.detailEvent()

            if (this.var.selectedIdx !== '') {
                this.openDetail({board_idx: this.var.selectedIdx})
            }

        }


        // 좌측 메뉴 이벤트
        WorshipMain.prototype.menuSelectEvent = function () {
            const _this = this;

            this.var.$menuList.find(".menu").click(function () {
                const menu = $(this).data('value')
                window.location.href = '/worship?type=' + menu
                // if (_this.var.menuSelected === menu) {
                //     return false
                // }
                // _this.var.$menuList.find(".selected").removeClass("selected")
                // $(this).addClass("selected")
                //
                // _this.var.$boardDetail.find(".back").click();
                //
                //
                //     _this.var.menuSelected = menu
                // _this.var.$detail.find(".sub-title").text($(this).text())
                //
                // _this.clearList()
                // paging.init()
                // ajaxRequests.getWorshipBoardList({
                //     type: menu,
                //     page: 1,
                //     init: true,
                //     callback: function(json) {
                //         paging.setPagingInfo(json.paging)
                //         paging.drawPaging()
                //         paging.setPageEventFunction(function(page){
                //             ajaxRequests.getWorshipBoardList(
                //                 {
                //                     type: menu,
                //                     page: page,
                //                     init: false,
                //                     callback: function(json){
                //                         _this.clearList()
                //                         _this.setList(json.list)
                //                     }
                //                 }
                //             )
                //         })
                //         _this.setList(json.list)
                //     }
                // });
            });
        }


        // 등록페이지 세팅
        WorshipMain.prototype.registerInit = function () {

            const $hostSel = this.var.$registerPopup.find("[name=preacher]")
            $.each(gVar.data.pastorList, function (i, v) {
                $hostSel.append('<option value="' + v + '">' + v + '</option>')
            });
            $hostSel.append('<option value="etc">직접입력</option>')

            this.registerEvent();

        }
        WorshipMain.prototype.categorySelect = function (value) {
            const _this = this
            const $registerPopup = this.var.$registerPopup
            const $preachData = $registerPopup.find(".preach")
            const $praiseData = $registerPopup.find(".praise")
            const $contentTitle = $registerPopup.find(".content-title")

            $preachData.addClass("hide")
            $preachData.find("input").val("")

            $praiseData.addClass("hide")
            $praiseData.find("input").val("")


            if (value === -1) {
                $contentTitle.find(".label").text("게시물 제목")
            }
            // 예배실황
            else if (value === 3) {
                $contentTitle.find(".label").text("게시물 제목")
            }
            // 찬양
            else if (value === 4) {
                $contentTitle.find(".label").text("찬양 제목")
                $praiseData.removeClass("hide")
            } else {
                $contentTitle.find(".label").text("설교 제목")
                // 담목 설교
                if (value === 1) {
                    $preachData.removeClass("hide")
                    $preachData.filter('.preacher').addClass("hide")
                    $registerPopup.find('[name=preacher]').val(gVar.data.pastorList[0])
                } else {
                    $registerPopup.find('[name=preacher]').val('')
                    $preachData.removeClass("hide")
                }

            }
        }
        WorshipMain.prototype.registerEvent = function () {
            const _this = this
            const $registerPopup = this.var.$registerPopup
            const $preachData = $registerPopup.find(".preach")
            const $praiseData = $registerPopup.find(".praise")
            const $contentTitle = $registerPopup.find(".content-title")
            $("#register").click(function () {
                _this.openRegisterPopup(_this.var.menuSelected)
            });
            $registerPopup.find("[name=category]").change(function () {
                _this.categorySelect($(this).val() === '' ? -1 : Number($(this).val()))
                // $preachData.addClass("hide")
                // $preachData.find("input").val("")
                //
                // $praiseData.addClass("hide")
                // $praiseData.find("input").val("")
                //
                //
                // if (value === '') {
                //     $contentTitle.find(".label").text("게시물 제목")
                // }
                // // 예배실황
                // else if (value === '3') {
                //     $contentTitle.find(".label").text("게시물 제목")
                // }
                // // 찬양
                // else if (value === '4') {
                //     $contentTitle.find(".label").text("찬양 제목")
                //     $praiseData.removeClass("hide")
                // } else {
                //     $contentTitle.find(".label").text("설교 제목")
                //     // 담목 설교
                //     if (value === '1') {
                //         $preachData.removeClass("hide")
                //         $preachData.filter('.preacher').addClass("hide")
                //         $registerPopup.find('[name=preacher]').val(gVar.data.pastorList[0])
                //     } else {
                //         $registerPopup.find('[name=preacher]').val('')
                //         $preachData.removeClass("hide")
                //     }
                // }
            })
            // $registerPopup.find("[name=preacher]").change(function () {
            //     const value = $(this).val()
            //     if (value === 'etc') {
            //         $registerPopup.find("[name=host_etc]").show()
            //     } else {
            //         $registerPopup.find("[name=host_etc]").hide()
            //     }
            // })
            $registerPopup.find("[name=preacher]").change(function () {
                const $preachEtc = $preachData.find("[name=preacher_etc]")
                if ($(this).val() === 'etc') {
                    $preachEtc.val('')
                    $preachEtc.show()
                } else {
                    $preachEtc.hide()
                }
            })
            $registerPopup.find(".cancel").click(function () {
                $registerPopup.hide()
                $registerPopup.find("input").val('')
                $registerPopup.find("textarea").val('')
                $registerPopup.find("select").val('')
                $preachData.addClass("hide")
                $praiseData.addClass("hide")
                $contentTitle.find(".label").text("게시물 제목")
            })
            $registerPopup.find(".close").click(function () {
                $registerPopup.find(".cancel").click()
            })
            $registerPopup.find(".register").click(function () {
                _this.setRegisterParams(function (params) {
                    ajaxRequests.registerWorshipBoard(params, function(json){
                        _this.clearList()
                        ajaxRequests.getWorshipBoardList({
                            type: _this.var.menuSelected,
                            page: 1,
                            init: false,
                            callback: function(json) {
                                _this.setList(json.list)
                            }
                        });
                        alert("등록이 완료되었습니다.")
                        $registerPopup.find(".close").click()
                    });
                });
            })
        }
        WorshipMain.prototype.openRegisterPopup = function ({isEdit}) {
            const $registerPopup = this.var.$registerPopup

            if (isEdit) {
                $registerPopup.find('.register').hide()
                $registerPopup.find('.edit').show()
            } else {
                $registerPopup.find('.edit').hide()
                $registerPopup.find('.register').show()
            }

            this.var.$registerPopup.show()
        }
        WorshipMain.prototype.openEditPopup = function (idx) {
            const _this = this
            const $registerPopup = this.var.$registerPopup
            ajaxRequests.getWorshipBoard({
                idx,
                callback: function(json){
                    const data = json.data
                    _this.categorySelect(data.category)

                    $registerPopup.find('[name=category]').val(data.category)
                    $registerPopup.find('[name=date]').val(data.date)
                    if (data.worship_type) {
                        $registerPopup.find('[name=worship_type]').val(data.worship_type)
                    }

                    if (data.category === 4) {
                        if (data.host_name)
                            $registerPopup.find('[name=host_name]').val(data.host_name)
                    }

                    if (data.category === 2) {
                        if (data.host_name) {
                            const searchHost = gVar.data.pastorList.findIndex((n) => n === data.host_name)
                            const $preachEtc = $registerPopup.find("[name=preacher_etc]")

                            if (searchHost === -1) {
                                $registerPopup.find('[name=preacher]').val('etc')
                                $preachEtc.show()
                                $preachEtc.val(data.host_name)
                            } else {
                                $registerPopup.find('[name=preacher]').val(data.host_name)
                                $preachEtc.hide()
                            }
                        }
                    }

                    if(data.title){
                        $registerPopup.find('[name=title]').val(data.title)
                    }
                    if(data.verse){
                        $registerPopup.find('[name=verse]').val(data.verse)
                    }
                    if(data.content){
                        $registerPopup.find('[name=content]').val(data.content)
                    }

                    $registerPopup.find(".edit").off()
                    $registerPopup.find(".edit").click(function(){
                        _this.setRegisterParams(function (params) {
                            params.board_idx = idx
                            ajaxRequests.editWorshipBoard(params, function(json){
                                $registerPopup.find(".close").click()
                                // _this.clearList()
                                // ajaxRequests.getWorshipBoardList({
                                //     type: _this.var.menuSelected,
                                //     page: 1,
                                //     init: false,
                                //     callback: function(json) {
                                //         _this.setList(json.list)
                                //     }
                                // });
                                alert("수정이 완료되었습니다.")
                                _this.openDetail(params)
                            });
                        });
                    })

                    _this.openRegisterPopup({isEdit: true})
                }
            })
        }
        WorshipMain.prototype.setRegisterParams = function (callback) {
            const params = {}
            const $registerPopup = this.var.$registerPopup

            params.category = $registerPopup.find('[name=category]').val()
            params.date = $registerPopup.find('[name=date]').val()
            params.worship_type = $registerPopup.find('[name=worship_type]').val()
            params.praise_team = $registerPopup.find('[name=praise_team]').val()
            params.preacher = $registerPopup.find('[name=preacher]').val()
            params.preacher_etc = $registerPopup.find('[name=preacher_etc]').val()
            params.title = $registerPopup.find('[name=title]').val()
            params.verse = $registerPopup.find('[name=verse]').val()
            params.content = $registerPopup.find('[name=content]').val()

            if (params.category === '4') {
                params.host_name = params.praise_team
            } else if (params.category === '3') {
                params.host_name = ''
            } else {
                if (params.preacher === 'etc') {
                    params.host_name = params.preacher_etc
                } else {
                    params.host_name = params.preacher
                }
            }

            if (params.category === '') {
                alert("게시물 구분을 입력해주세요")
                return false
            }
            if (params.date === '') {
                alert("날짜를 입력해주세요")
                return false
            }
            if (params.worship_type === '') {
                alert("예배 구분을 입력해주세요")
                return false
            }
            if (params.category !== '3' && params.host_name === '') {
                alert("담당자를 입력해주세요")
                return false
            }
            if (params.title === '') {
                alert("제목을 입력해주세요")
                return false
            }

            callback(params)
        }


        // 게시판 리스트 세팅
        WorshipMain.prototype.clearList = function () {
            const $list = this.var.$detail.find(".list").find("table")
            $list.find("tr").remove()
        }
        WorshipMain.prototype.setList = function (data) {
            const _this = this
            const $list = this.var.$detail.find(".list").find("table")
            $.each(data, function(i, v){
                const $row = $("<tr></tr>")

                switch (v.category) {
                    case 1:
                        $row.append(
                            '<td style="text-align: left; padding-left: 5px;">'+
                            '   <div style="font-size: 1.05rem;"> '+ v.title +'</div>'+
                            '   <div style="font-size: .9rem; color: grey"><i class="fas fa-bible"></i> ' + v.verse +'</div>' +
                            '</td>'+
                            '<td class="pastor" style="width: 120px;">예동열 담임목사</td>' +
                            '<td class="date" style="width: 90px;">'+ v.date +'</td>'+
                            '<td class="worship" style="width: 120px;">' + util.getWorshipName(v.worship_type) + '</td>'
                        )
                        break
                    case 2:
                        $row.append(
                            '<td style="text-align: left; padding-left: 5px;">'+
                            '   <div style="font-size: 1.05rem;">'+ v.title +'</div>'+
                            '   <div style="font-size: .9rem; color: grey"><i class="fas fa-bible"></i> ' + v.verse +'</div>' +
                            '</td>'+
                            '<td class="pastor" style="width: 120px;">'+ v.host_name +'</td>' +
                            '<td class="date" style="width: 90px;">'+ v.date +'</td>'+
                            '<td class="worship" style="width: 120px;">' + util.getWorshipName(v.worship_type) + '</td>'
                        )
                        break
                    case 3:
                        $row.append(
                            '<td style="text-align: left; padding-left: 5px;">'+
                            '   <div style="font-size: 1.05rem;">'+ v.title +'</div>'+
                            '</td>'+
                            '<td class="date" style="width: 90px;">'+ v.date +'</td>'+
                            '<td class="worship" style="width: 120px;">' + util.getWorshipName(v.worship_type) + '</td>'
                        )
                        break
                    case 4:
                        $row.append(
                            '<td style="text-align: left; padding-left: 5px;">'+
                            '   <div style="font-size: 1.05rem;">'+ v.title +'</div>'+
                            '</td>'+
                            '<td class="pastor" style="width: 120px;">'+ v.host_name +'</td>' +
                            '<td class="date" style="width: 90px;">'+ v.date +'</td>'+
                            '<td class="worship" style="width: 120px;">' + util.getWorshipName(v.worship_type) + '</td>'
                        )
                        break
                }

                $list.append($row)
                $row.click(function(){
                    _this.openDetail(v)
                })

            })
        }


        // 상세화면 세팅

        WorshipMain.prototype.detailEvent = function () {
            const _this = this
            this.var.$boardDetail.find(".back").click(function(){
                _this.var.$detail.removeClass("hide")
                _this.var.$boardDetail.addClass("hide")
            })
        }
        WorshipMain.prototype.openDetail = function (v) {
            const _this = this
            const $boardDetail = this.var.$boardDetail
            this.var.$detail.addClass("hide")
            $boardDetail.removeClass("hide")
            $boardDetail.find(".title").text('')
            $boardDetail.find(".verse").text('')
            $boardDetail.find(".date").text('')
            $boardDetail.find(".worship-type").text('')
            $boardDetail.find(".host-name").text('')

            $boardDetail.find(".buttons").find("button").remove()

            ajaxRequests.getWorshipBoard({
                idx: v.board_idx,
                callback: function(json){
                    const data = json.data

                    if (data.is_mine) {
                        const $edit = $('<button class="edit">수정</button>')
                        const $delete = $('<button class="delete">삭제</button>')
                        $boardDetail.find(".buttons").append($edit)
                        $boardDetail.find(".buttons").append($delete)

                        $edit.click(function () {
                            _this.openEditPopup(data.board_idx)
                        })
                        $delete.click(function () {
                            if (confirm('정말 삭제하시겠습니까?')) {

                            }
                        })
                    }

                    $boardDetail.find(".title").text(data.title)
                    $boardDetail.find(".verse").text(data.verse)
                    $boardDetail.find(".date").text(data.date)
                    $boardDetail.find(".worship-type").text(util.getWorshipName(data.worship_type))

                    if (data.category === 3) {
                        $boardDetail.find(".host-name").addClass("hide")
                    } else {
                        $boardDetail.find(".host-name").removeClass("hide").text(data.host_name)
                    }

                    const $iframe = $(data.content)

                    $iframe
                        .attr("width", "900")
                        .attr("height", "500")

                    $boardDetail.find("iframe").remove()
                    $boardDetail.find(".desc").append($iframe)

                }
            })

        }




        return new WorshipMain();
});
