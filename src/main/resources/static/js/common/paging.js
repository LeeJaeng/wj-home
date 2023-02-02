define(['common/gVar'], function(gVar){
    function Paging() {
        this.totalCnt = 0
        this.curPage = 0
        this.pageCnt = 0
        this.cntPerPage = 0
        this.endPage = 0
        this.curPageSetList = []

        this.$topNode = null
        this.pageEventFunction = null
    }

    Paging.prototype.init = function() {
        this.totalCnt = 0
        this.curPage = 0
        this.pageCnt = 0
        this.cntPerPage = 0
        this.endPage = 0
        this.curPageSetList = []

        this.$topNode.find("div").remove()
    }
    Paging.prototype.setTopNode = function($topNode) {
        this.$topNode = $topNode
    }
    Paging.prototype.setPageEventFunction = function(eventFunction) {
        this.pageEventFunction = eventFunction
    }
    Paging.prototype.setPagingInfo = function({curPage, cntPerPage, pageCnt, totalCnt}) {
        this.totalCnt = totalCnt
        if (gVar.isMobile) {
            this.pageCnt = 5
        } else {
            this.pageCnt = 10
        }
        this.curPage = curPage
        this.cntPerPage = cntPerPage
        this.endPage = parseInt(totalCnt / cntPerPage) + 1
    }
    Paging.prototype.drawPaging = function() {
        const _this = this
        const $prev = $('<div class="prev col-md-auto"><i class="fas fa-chevron-left"></i></div>')
        const $num = $('<div class="num col-md-auto"></div>')
        const $next = $('<div class="next col-md-auto"><i class="fas fa-chevron-right"></i></div>')

        if (this.curPage === 1) {
            $prev.addClass("disabled")
        }
        if (this.curPage + this.pageCnt >= this.endPage) {
            $next.addClass("disabled")
        }
        this.$topNode.append($prev)
        this.$topNode.append($num)

        for (let i = 1 ; i < this.pageCnt + 1 ; i++) {
            if (i > _this.endPage) {
                break
            }

            _this.curPageSetList.push(i);
            const $page = $("<div class='page col-md-auto'>"+ i +"</div>")

            if (i===1) {
                $page.addClass("selected")
            }


            $num.append($page)
            _this.pageEvent($page, i)
        }

        this.$topNode.append($next)

        this.prevEvent($prev)
        this.nextEvent($next)
    }
    Paging.prototype.prevEvent = function($prev) {
        const _this = this
        $prev.click(function(){
            if ($prev.hasClass("disabled")) {
                return false
            }

            _this.$topNode.find(".page").remove()

            const $num = $prev.siblings('.num')

            const curFirstPage = _this.curPageSetList[0]
            const newFirstPage = curFirstPage - _this.pageCnt

            _this.curPageSetList = []
            if (newFirstPage === 1) {
                _this.$topNode.find(".prev").addClass("disabled")
            }
            _this.$topNode.find(".next").removeClass("disabled")


            for (let i = newFirstPage ; i < newFirstPage + _this.pageCnt ; i++) {
                _this.curPageSetList.push(i);
                const $page = $("<div class='page col-md-auto'>"+ i +"</div>")
                if (i === newFirstPage + _this.pageCnt - 1) {
                    $page.addClass("selected")
                }
                $num.append($page)
                _this.pageEvent($page, i)
            }

            _this.pageEventFunction(newFirstPage)
        })
    }
    Paging.prototype.nextEvent = function($next) {
        const _this = this
        const $num = $next.siblings('.num')
        $next.click(function(){
            // console.log("click")
            if ($next.hasClass("disabled")) {
                return false
            }

            _this.$topNode.find(".page").remove()

            const curEndPage = _this.curPageSetList.pop()
            const newFirstPage = curEndPage + 1

            _this.curPageSetList = []

            if (newFirstPage + _this.pageCnt >= _this.endPage) {
                _this.$topNode.find(".next").addClass("disabled")
            }
            _this.$topNode.find(".prev").removeClass("disabled")


            for (let i = newFirstPage ; i < newFirstPage +_this.pageCnt ; i++) {
                if (i > _this.endPage) {
                    break
                }

                _this.curPageSetList.push(i);
                const $page = $("<div class='page'>"+ i +"</div>")
                if (i === newFirstPage) {
                    $page.addClass("selected")
                }
                $num.append($page)
                _this.pageEvent($page, i)
            }
            _this.pageEventFunction(newFirstPage)
        })
    }
    Paging.prototype.pageEvent = function($page, page) {
        const _this = this
        $page.click(function(){
            _this.$topNode.find(".page.selected").removeClass("selected")
            $page.addClass("selected")

            // getList
            _this.pageEventFunction(page)
        })
    }




    return new Paging();
});
