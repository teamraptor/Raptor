define(["AnguRaptor"],function(a){a.filter("newlines",["$sanitize",function(a){var b=/xhtml/i.test(document.doctype)?"<br />":"<br>";return function(c){return c=(c+"").replace(/(\r\n|\n\r|\r|\n|&#10;&#13;|&#13;&#10;|&#10;|&#13;)/g,b+"$1"),a(c)}}]),a.filter("myLinky",[function(){var a=/((https?:\/\/)|(www\.)[A-Za-z0-9._%+-]*)\S*[^\s.;,(){}<>"\u201d\u2019]/i;return function(b,c){function d(a){a&&j.push(a)}function e(a,b){j.push("<a "),angular.isDefined(c)&&j.push('target="',c,'" '),j.push('href="',a.replace(/"/g,"&quot;"),'">'),d(b),j.push("</a>")}if(!b)return b;for(var f,g,h,i=b,j=[];f=i.match(a);)g=f[0],f[2]||f[4]||!f[3]||(g="http://"+g),h=f.index,d(i.substr(0,h)),e(g,f[0]),i=i.substring(h+f[0].length);return d(i),j.join("")}}]),a.filter("noImages",function(){return function(a){var b=/((https?:\/\/)|(www\.)[A-Za-z0-9._%+-]*)\S*[^\s.;,(){}<>"\u201d\u2019]\.(jpeg|jpg|gif|png)/gim;return a.replace(b,"")}}),a.filter("hashtags",function(){return function(a){var b=/(^|\s)#(\w*[a-zA-Z_]+\w*)/gim;return a.replace(b,' <a href="#/search?term=%23$2">#$2</a>')}}),a.filter("mentions",function(){return function(a){var b=/(^|\s)\@(\w*[a-zA-Z_]+\w*)/gim;return a.replace(b,' <a href="#/profile/$2">@$2</a>')}})});