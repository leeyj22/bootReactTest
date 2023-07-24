(self.webpackChunk_N_E=self.webpackChunk_N_E||[]).push([[405],{5557:function(e,t,n){(window.__NEXT_P=window.__NEXT_P||[]).push(["/",function(){return n(6861)}])},3991:function(e,t){"use strict";var n,r;Object.defineProperty(t,"__esModule",{value:!0}),function(e,t){for(var n in t)Object.defineProperty(e,n,{enumerable:!0,get:t[n]})}(t,{PrefetchKind:function(){return n},ACTION_REFRESH:function(){return o},ACTION_NAVIGATE:function(){return l},ACTION_RESTORE:function(){return u},ACTION_SERVER_PATCH:function(){return f},ACTION_PREFETCH:function(){return i},ACTION_FAST_REFRESH:function(){return a},ACTION_SERVER_ACTION:function(){return c}});let o="refresh",l="navigate",u="restore",f="server-patch",i="prefetch",a="fast-refresh",c="server-action";(r=n||(n={})).AUTO="auto",r.FULL="full",r.TEMPORARY="temporary",("function"==typeof t.default||"object"==typeof t.default&&null!==t.default)&&void 0===t.default.__esModule&&(Object.defineProperty(t.default,"__esModule",{value:!0}),Object.assign(t.default,t),e.exports=t.default)},1516:function(e,t,n){"use strict";function r(e,t,n,r){return!1}Object.defineProperty(t,"__esModule",{value:!0}),Object.defineProperty(t,"getDomainLocale",{enumerable:!0,get:function(){return r}}),n(2387),("function"==typeof t.default||"object"==typeof t.default&&null!==t.default)&&void 0===t.default.__esModule&&(Object.defineProperty(t.default,"__esModule",{value:!0}),Object.assign(t.default,t),e.exports=t.default)},5569:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),Object.defineProperty(t,"default",{enumerable:!0,get:function(){return O}});let r=n(8754),o=r._(n(7294)),l=n(4532),u=n(3353),f=n(1410),i=n(9064),a=n(370),c=n(9955),s=n(4224),d=n(508),p=n(1516),h=n(4266),_=n(3991),y=new Set;function b(e,t,n,r,o,l){if(!l&&!(0,u.isLocalURL)(t))return;if(!r.bypassPrefetchedCheck){let o=void 0!==r.locale?r.locale:"locale"in e?e.locale:void 0,l=t+"%"+n+"%"+o;if(y.has(l))return;y.add(l)}let f=l?e.prefetch(t,o):e.prefetch(t,n,r);Promise.resolve(f).catch(e=>{})}function v(e){return"string"==typeof e?e:(0,f.formatUrl)(e)}let g=o.default.forwardRef(function(e,t){let n,r;let{href:f,as:y,children:g,prefetch:O=null,passHref:E,replace:j,shallow:C,scroll:P,locale:T,onClick:m,onMouseEnter:R,onTouchStart:M,legacyBehavior:k=!1,...A}=e;n=g,k&&("string"==typeof n||"number"==typeof n)&&(n=o.default.createElement("a",null,n));let x=o.default.useContext(c.RouterContext),I=o.default.useContext(s.AppRouterContext),N=null!=x?x:I,L=!x,S=!1!==O,w=null===O?_.PrefetchKind.AUTO:_.PrefetchKind.FULL,{href:U,as:K}=o.default.useMemo(()=>{if(!x){let e=v(f);return{href:e,as:y?v(y):e}}let[e,t]=(0,l.resolveHref)(x,f,!0);return{href:e,as:y?(0,l.resolveHref)(x,y):t||e}},[x,f,y]),F=o.default.useRef(U),H=o.default.useRef(K);k&&(r=o.default.Children.only(n));let D=k?r&&"object"==typeof r&&r.ref:t,[V,q,X]=(0,d.useIntersection)({rootMargin:"200px"}),z=o.default.useCallback(e=>{(H.current!==K||F.current!==U)&&(X(),H.current=K,F.current=U),V(e),D&&("function"==typeof D?D(e):"object"==typeof D&&(D.current=e))},[K,D,U,X,V]);o.default.useEffect(()=>{N&&q&&S&&b(N,U,K,{locale:T},{kind:w},L)},[K,U,q,T,S,null==x?void 0:x.locale,N,L,w]);let B={ref:z,onClick(e){k||"function"!=typeof m||m(e),k&&r.props&&"function"==typeof r.props.onClick&&r.props.onClick(e),N&&!e.defaultPrevented&&function(e,t,n,r,l,f,i,a,c,s){let{nodeName:d}=e.currentTarget,p="A"===d.toUpperCase();if(p&&(function(e){let t=e.currentTarget,n=t.getAttribute("target");return n&&"_self"!==n||e.metaKey||e.ctrlKey||e.shiftKey||e.altKey||e.nativeEvent&&2===e.nativeEvent.which}(e)||!c&&!(0,u.isLocalURL)(n)))return;e.preventDefault();let h=()=>{let e=null==i||i;"beforePopState"in t?t[l?"replace":"push"](n,r,{shallow:f,locale:a,scroll:e}):t[l?"replace":"push"](r||n,{forceOptimisticNavigation:!s,scroll:e})};c?o.default.startTransition(h):h()}(e,N,U,K,j,C,P,T,L,S)},onMouseEnter(e){k||"function"!=typeof R||R(e),k&&r.props&&"function"==typeof r.props.onMouseEnter&&r.props.onMouseEnter(e),N&&(S||!L)&&b(N,U,K,{locale:T,priority:!0,bypassPrefetchedCheck:!0},{kind:w},L)},onTouchStart(e){k||"function"!=typeof M||M(e),k&&r.props&&"function"==typeof r.props.onTouchStart&&r.props.onTouchStart(e),N&&(S||!L)&&b(N,U,K,{locale:T,priority:!0,bypassPrefetchedCheck:!0},{kind:w},L)}};if((0,i.isAbsoluteUrl)(K))B.href=K;else if(!k||E||"a"===r.type&&!("href"in r.props)){let e=void 0!==T?T:null==x?void 0:x.locale,t=(null==x?void 0:x.isLocaleDomain)&&(0,p.getDomainLocale)(K,e,null==x?void 0:x.locales,null==x?void 0:x.domainLocales);B.href=t||(0,h.addBasePath)((0,a.addLocale)(K,e,null==x?void 0:x.defaultLocale))}return k?o.default.cloneElement(r,B):o.default.createElement("a",{...A,...B},n)}),O=g;("function"==typeof t.default||"object"==typeof t.default&&null!==t.default)&&void 0===t.default.__esModule&&(Object.defineProperty(t.default,"__esModule",{value:!0}),Object.assign(t.default,t),e.exports=t.default)},508:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),Object.defineProperty(t,"useIntersection",{enumerable:!0,get:function(){return i}});let r=n(7294),o=n(29),l="function"==typeof IntersectionObserver,u=new Map,f=[];function i(e){let{rootRef:t,rootMargin:n,disabled:i}=e,a=i||!l,[c,s]=(0,r.useState)(!1),d=(0,r.useRef)(null),p=(0,r.useCallback)(e=>{d.current=e},[]);(0,r.useEffect)(()=>{if(l){if(a||c)return;let e=d.current;if(e&&e.tagName){let r=function(e,t,n){let{id:r,observer:o,elements:l}=function(e){let t;let n={root:e.root||null,margin:e.rootMargin||""},r=f.find(e=>e.root===n.root&&e.margin===n.margin);if(r&&(t=u.get(r)))return t;let o=new Map,l=new IntersectionObserver(e=>{e.forEach(e=>{let t=o.get(e.target),n=e.isIntersecting||e.intersectionRatio>0;t&&n&&t(n)})},e);return t={id:n,observer:l,elements:o},f.push(n),u.set(n,t),t}(n);return l.set(e,t),o.observe(e),function(){if(l.delete(e),o.unobserve(e),0===l.size){o.disconnect(),u.delete(r);let e=f.findIndex(e=>e.root===r.root&&e.margin===r.margin);e>-1&&f.splice(e,1)}}}(e,e=>e&&s(e),{root:null==t?void 0:t.current,rootMargin:n});return r}}else if(!c){let e=(0,o.requestIdleCallback)(()=>s(!0));return()=>(0,o.cancelIdleCallback)(e)}},[a,n,t,c,d.current]);let h=(0,r.useCallback)(()=>{s(!1)},[]);return[p,c,h]}("function"==typeof t.default||"object"==typeof t.default&&null!==t.default)&&void 0===t.default.__esModule&&(Object.defineProperty(t.default,"__esModule",{value:!0}),Object.assign(t.default,t),e.exports=t.default)},6861:function(e,t,n){"use strict";n.r(t),n.d(t,{default:function(){return s}});var r=n(5893);n(7294);var o=n(1894),l=n(5697),u=n.n(l),f=n(1664),i=n.n(f);let a=e=>{let{children:t}=e;return(0,r.jsxs)("div",{children:[(0,r.jsx)("ul",{children:(0,r.jsx)("li",{children:(0,r.jsx)(i(),{href:"/page2",target:"_blank",rel:"noopener noreferrer",children:"page2"})})}),t]})};a.propTypes={children:u().node.isRequired};var c=n(4707),s=()=>{let e=(0,o.I0)(),t=()=>{console.log("asdsadasd"),e({type:c.ZV,data:"jm91"})};return(0,r.jsxs)(a,{children:[(0,r.jsx)("button",{onClick:()=>t(),children:"버튼"}),(0,r.jsx)("h3",{children:"리스트"})]})}},1664:function(e,t,n){e.exports=n(5569)}},function(e){e.O(0,[774,888,179],function(){return e(e.s=5557)}),_N_E=e.O()}]);