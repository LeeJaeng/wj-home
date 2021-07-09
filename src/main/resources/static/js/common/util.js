define([], function(){
    function Util() {
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



    return new Util();
});
