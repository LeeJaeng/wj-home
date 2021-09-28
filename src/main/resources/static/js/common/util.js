define([], function(){
    function Util() {
        this.setDatePicker()
    }

    Util.prototype.setDatePicker = function () {
        $('.date-picker').datepicker({
            dateFormat: "yy-mm-dd"
            , monthNamesShort: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'] //달력의 월 부분 텍스트
            , monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'] //달력의 월 부분 Tooltip 텍스트
            , dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'] //달력의 요일 부분 텍스트
            , dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'] //달력의 요일 부분 Tooltip 텍스트
            , prevText: '이전 달'	// 마우스 오버시 이전달 텍스트
            , nextText: '다음 달'	// 마우스 오버시 다음달 텍스트
            , closeText: '닫기' // 닫기 버튼 텍스트 변경
            , currentText: '오늘' // 오늘 텍스트 변경
            , changeYear: true // 년을 바꿀 수 있는 셀렉트 박스를 표시한다.
            , changeMonth: true // 월을 바꿀 수 있는 셀렉트 박스를 표시한다.
            , onSelect: function (d) {
            }
        });
    }

    Util.prototype.decodeJwt = function (token) {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    }

    Util.prototype.getCookie = function (cname) {
        const name = cname + "=";
        const decodedCookie = decodeURIComponent(document.cookie);
        const ca = decodedCookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) === 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    };
    Util.prototype.setCookie = function(cname, cvalue, exhour) {
        const d = new Date();
        d.setTime(d.getTime() + (exhour*60*60*1000));
        const expires = "expires="+ d.toUTCString();
        if (window.location.href.search("127.0.0.1") > -1 || window.location.href.search("localhost") > -1)  {
            document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/;";
        } else {
            document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/;domain=.woojeong.or.kr;";
        }
    };


    Util.prototype.getWorshipName = function (worship_type) {
        switch (worship_type) {
            case 'sun1':
                return '주일 1부예배'
            case 'sun2':
                return '주일 2부예배'
            case 'sun3':
                return '주일 3부예배'
            case 'sun4':
                return '주일 4부예배'
            case 'fri':
                return '금요성령집회'
            case 'wed1':
                return '수요예배 오전'
            case 'wed2':
                return '수요예배 오후 '
            case 'dawn':
                return '새벽예배'
            case 'etc':
                return '기타예배'
        }
        return ''
    }



    return new Util();
});
