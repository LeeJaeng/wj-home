define(
    ['common/gVar', 'common/userInfo', 'common/ajaxRequests', "common/paging", 'common/util'],
    function (gVar, userInfo, ajaxRequests, paging, util) {

        function CommunityMain() {
            const $content = $("#content");

            this.var = {
                $content: $content,
                $detail: $content.find(".detail"),
                $boardDetail: $content.find(".board-detail"),
                $menuList: $content.find(".menu-list"),
                $register: $("#register"),
                $registerPopup: $("#registerPopup"),
                menuSelected: 'paper',
                uploading: false,
            }


            this.var.menuSelected = $('[name=board_type]').val()
            this.var.selectedIdx = $('[name=board_idx]').val()
            $('.init-val-remove').remove()

            this.menuSelectEvent()


        }

        CommunityMain.prototype.init = function () {
            const _this = this

            if (userInfo.isAdmin) {
                this.registerInit()
            }
            else if (userInfo.isPraise) {
                this.registerInit()
                $("#info-edit").remove();
                $("#r-opt-paper").remove();
                $("#r-opt-photo").remove();
                $("#r-opt-info").remove();
                if (this.var.menuSelected !== 'file') {
                    this.var.$register.hide()
                }
            }
            else {
                this.var.$register.remove()
                // 정보수정 요청, 방송실 협조 안보이게
                $("#info-edit").remove()
                $("#file-board").remove()
            }

            if (gVar.isMobile) {
                // $("#info-edit").remove()
                // $("#file-board").remove()
            }

            // 초기 세팅
            const $selected = this.var.$menuList.find(".menu[data-value="+ this.var.menuSelected +"]")
            $selected.addClass("selected")
            this.var.$detail.find('.sub-title').text($selected.text())


            // 첫 데이터 받기
            ajaxRequests.getCommunityBoardList({
                type: this.var.menuSelected,
                page: 1,
                init: true,
                callback: function(json) {
                    paging.setTopNode(_this.var.$detail.find(".paging"))
                    paging.setPagingInfo(json.paging)
                    paging.drawPaging()
                    paging.setPageEventFunction(function(page){
                        ajaxRequests.getCommunityBoardList(
                            {
                                type: _this.var.menuSelected,
                                page: page,
                                init: false,
                                callback: function(json){
                                    _this.clearList()
                                    _this.setList(json.list)
                                    _this.var.$detail.scrollTop(0);
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
        CommunityMain.prototype.menuSelectEvent = function () {
            const _this = this;

            this.var.$menuList.find(".menu").click(function () {
                const menu = $(this).data('value')
                if (_this.var.menuSelected === menu) {
                    return false
                }
                _this.var.$menuList.find(".selected").removeClass("selected")
                $(this).addClass("selected")

                _this.var.$boardDetail.find(".back").click();


                _this.var.menuSelected = menu
                _this.var.$detail.find(".sub-title").text($(this).text())
                // if (_this.var.menuSelected === 'file') {
                //     _this.var.$register.show()
                // } else {
                //     _this.var.$register.hide()
                // }

                _this.clearList()
                paging.init()
                ajaxRequests.getCommunityBoardList({
                    type: menu,
                    page: 1,
                    init: true,
                    callback: function(json) {
                        paging.setPagingInfo(json.paging)
                        paging.drawPaging()
                        paging.setPageEventFunction(function(page){
                            ajaxRequests.getCommunityBoardList(
                                {
                                    type: menu,
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
            });
        }


        // 등록페이지 세팅
        CommunityMain.prototype.registerInit = function () {
            //
            // const $hostSel = this.var.$registerPopup.find("[name=preacher]")
            // $.each(gVar.data.pastorList, function (i, v) {
            //     $hostSel.append('<option value="' + v + '">' + v + '</option>')
            // });
            // $hostSel.append('<option value="etc">직접입력</option>')
            //
            this.registerEvent();

        }
        CommunityMain.prototype.registerEvent = function () {
            const _this = this
            const $registerPopup = this.var.$registerPopup
            const $contentTitle = $registerPopup.find(".content-title")
            $("#register").click(function () {
                _this.openRegisterPopup({menu: _this.var.menuSelected})
            });
            $registerPopup.find(".cancel").click(function () {
                $registerPopup.hide()
                $registerPopup.find("input").val('')
                $registerPopup.find("textarea").val('')
                $registerPopup.find("select").val('')
                $contentTitle.find(".label").text("게시물 제목")
            })
            $registerPopup.find(".close").click(function () {
                $registerPopup.find(".cancel").click()
            })
            $registerPopup.find(".register").click(function () {
                if (_this.var.uploading) {
                    alert("등록 중입니다.")
                    return false;
                }

                _this.var.uploading = true

                _this.setRegisterParams(function (params) {
                    const files = _this.var.$registerPopup.find('[name=files]')[0].files
                    const filesArr = Array.prototype.slice.call(files)
                    let formData = new FormData()
                    $.each(filesArr, function(i, v){
                        formData.append("files", v);
                    });
                    formData.append("category", params.category);
                    formData.append("title", params.title);
                    formData.append("content", params.content);
                    $("#loading").show();

                    ajaxRequests.registerCommunityBoard(formData, function(json){
                        _this.clearList()
                        $("#loading").hide();
                        ajaxRequests.getCommunityBoardList({
                            type: _this.var.menuSelected,
                            page: 1,
                            init: false,
                            callback: function(json) {
                                _this.setList(json.list)
                            }
                        });
                        _this.var.uploading = false
                        $registerPopup.find(".close").click();
                    });
                });
            })
        }
        CommunityMain.prototype.openRegisterPopup = function ({menu, isEdit}) {
            const $registerPopup = this.var.$registerPopup
            const $category = this.var.$registerPopup.find("[name=category]")
            switch (menu) {
                case "notice":
                    $category.val("1")
                    break
                case "paper":
                    $category.val("2")
                    break;
                case "photo":
                    $category.val("3")
                    break;
                case "file":
                    $category.val("4")
                    break;
                case "edit":
                    $category.val("5")
                    break;
            }

            if (isEdit) {
                $registerPopup.find('.register').hide()
                $registerPopup.find('.edit').show()
            } else {
                $registerPopup.find('.edit').hide()
                $registerPopup.find('.register').show()
            }
            $registerPopup.show()

        }
        CommunityMain.prototype.openEditPopup = function (idx) {
            const _this = this
            const $registerPopup = this.var.$registerPopup
            ajaxRequests.getCommunityBoard({
                idx,
                callback: function(json){
                    const data = json.data
                    _this.categorySelect(data.category)

                    $registerPopup.find('[name=category]').val(data.category)
                    $registerPopup.find('[name=title]').val(data.title)
                    $registerPopup.find('[name=content]').val(data.content)


                    $registerPopup.find(".edit").off()
                    $registerPopup.find(".edit").click(function(){
                        _this.setRegisterParams(function (params) {
                            const files = _this.var.$registerPopup.find('[name=files]')[0].files
                            const filesArr = Array.prototype.slice.call(files)
                            let formData = new FormData()
                            $.each(filesArr, function(i, v){
                                formData.append("files", v);
                            });
                            formData.append("board_idx", idx);
                            formData.append("category", params.category);
                            formData.append("title", params.title);
                            formData.append("content", params.content);

                            ajaxRequests.editCommunityBoard(formData, function(json){
                                alert("수정이 완료되었습니다.")
                                $registerPopup.find(".close").click();
                                _this.openDetail(params)
                            });
                        });
                    })
                    _this.openRegisterPopup({isEdit: true})
                }
            })
            this.openRegisterPopup({isEdit: true})
        }
        CommunityMain.prototype.setRegisterParams = function (callback) {
            const params = {}
            const $registerPopup = this.var.$registerPopup

            params.category = $registerPopup.find('[name=category]').val()
            params.title = $registerPopup.find('[name=title]').val()
            params.content = $registerPopup.find('[name=content]').val()

            //
            // if (params.category === '') {
            //     alert("게시물 구분을 입력해주세요")
            //     return false
            // }
            // if (params.title === '') {
            //     alert("제목을 입력해주세요")
            //     return false
            // }

            callback(params)
        }


        // 게시판 리스트 세팅
        CommunityMain.prototype.clearList = function () {
            const $list = this.var.$detail.find(".list");
            $list.find("table").find("tr").remove()
            $list.find(".thumb-list").find(".row").remove()
        }
        CommunityMain.prototype.setList = function (data) {
            if(this.var.menuSelected === 'paper' || this.var.menuSelected === 'photo') {
                this.setListThumb(data)
            } else {
                this.setListBoard(data)
            }
        }

        CommunityMain.prototype.setListBoard = function (data) {
            const _this = this
            const $list = this.var.$detail.find(".list").find("table")
            const $thumb = this.var.$detail.find(".list").find(".thumb-list")

            $list.show();
            $thumb.hide();

            $.each(data, function(i, v){
                const $row = $("<tr></tr>")

                // switch (v.category) {
                //     case 1:
                $row.append(
                    '<td style="text-align: left; padding-left: 5px;">'+
                    '   <div style="font-size: 1.05rem;">'+ v.title +'</div>'+
                    '</td>'+
                    '<td class="date">'+ v.date +'</td>'
                )
                // }

                $list.append($row)
                $row.click(function(){
                    _this.openDetail(v)
                })
            })
        }
        CommunityMain.prototype.setListThumb = function (data) {
            const _this = this
            const $list = this.var.$detail.find(".list").find("table")
            const $thumb = this.var.$detail.find(".list").find(".thumb-list")
            $list.hide();
            $thumb.show();
            $.each(data, function(i, v){
                if (gVar.isMobile) {
                    if (i % 1 === 0) {
                        $thumb.append("<div class='row'></div>")
                    }
                } else {
                    if (i % 3 === 0) {
                        $thumb.append("<div class='row'></div>")
                    }
                }

                const $row = $(
                    "<div class='thumb'>" +
                    "   <div class='img'><img src='"+ ( v.thumb_url ? v.thumb_url : v.thumb) +"' alt=''></div>" +
                    "   <div class='header'>"+ v.title +"</div>" +
                    "   <div class='date'>"+ (v.created_date.split(" ")[0]) +"</div>" +
                    "</div>"
                )
                $thumb.find("div.row:last-child").append($row)
                $row.click(function(){
                    _this.openDetail(v)
                })

            })
        }


        // 상세화면 세팅

        CommunityMain.prototype.detailEvent = function () {
            const _this = this
            this.var.$boardDetail.find(".back").click(function(){
                _this.var.$detail.removeClass("hide")
                _this.var.$boardDetail.addClass("hide")
                if (gVar.isMobile) {
                    _this.var.$menuList.show();
                }
            })
            // this.var.$boardDetail.find(".edit").click(function(){
            //     alert("현재 수정기능은 준비중입니다. 기존 게시글 삭제 후 다시 등록해주세요.")
            // });
        }
        CommunityMain.prototype.openDetail = function (info) {
            const _this = this
            this.var.$detail.addClass("hide")
            this.var.$boardDetail.removeClass("hide")

            const $boardDetail = this.var.$boardDetail
            const $photos = $boardDetail.find(".photos")
            const $files = $boardDetail.find(".files")
            if (gVar.isMobile) {
                this.var.$menuList.hide();
            }
            $boardDetail.find(".buttons").find("button").remove()
            $files.hide();
            $files.find('div.file').remove()
            $photos.hide();
            $photos.find('div').remove()

            ajaxRequests.getCommunityBoard({
                idx: info.board_idx,
                callback: function(json){
                    const data = json.data
                    $boardDetail.find(".title").text(data.title)
                    $boardDetail.find(".date").text(data.created_date)
                    $boardDetail.find(".desc").html(
                        data.content.replace(/\n/gi, "<br>")
                    )
                    if (data.category === 3 || data.category === 2 ) {
                        if (json.files.length > 0) {
                            $photos.show()
                            $.each(json.files, function(i, v){
                                $photos.append(
                                    '<div class="img">' +
                                    '   <img src="'+ v.url +'" alt="">' +
                                    '</div>'
                                )
                            })
                        }
                    }
                    else {
                        if (json.files.length > 0) {
                            $files.show()
                            $.each(json.files, function(i, v){
                                $files.find(".list").append(
                                    '<div class="file"><a href="'+ v.url +'"><i class="fas fa-download"></i> '+ v.origin_file_name +'</a></div>'
                                )
                            })
                        }
                    }

                    // 수정, 삭제 버튼 열기
                    if (data.is_mine) {
                        const $edit = $('<button class="edit">수정</button>')
                        const $delete = $('<button class="delete">삭제</button>')
                        $boardDetail.find(".buttons").append($edit)
                        $boardDetail.find(".buttons").append($delete)

                        $edit.click(function (){
                            alert("현재 수정기능은 준비중입니다. 기존 게시글 삭제 후 다시 등록해주세요.")
                        })
                        $delete.click(function (){
                            if (confirm('정말 삭제하시겠습니까?')) {
                                ajaxRequests.deleteCommunityBoard({
                                    idx: data.board_idx,
                                    callback: function(){
                                        _this.var.$boardDetail.find(".back").click()
                                        _this.clearList()
                                        ajaxRequests.getCommunityBoardList({
                                            type: _this.var.menuSelected,
                                            page: 1,
                                            init: false,
                                            callback: function(json) {
                                                _this.setList(json.list)
                                            }
                                        })
                                    }
                                })
                            }
                        })
                    }
                }
            })


        }




        return new CommunityMain();
});
