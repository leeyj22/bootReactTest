<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<head>
	<script src="/js/jquery-1.11.3.min.js?version=20210409"></script>
	<script src="/js/jquery-ui.js?version=20210409"></script>
	<script src="/js/jquery.js?version=20210409"></script>
	<script src="/js/storage.js?version=20210409"></script>
</head>
	<script type="text/javascript">
	$(document).ready(function(){
		var returnUrl 	= '${returnUrl}';		// returnUrl을 직접 받은 경우
		var preUrl 		= document.referrer;	// 이전 유입 경로(URL) 
		
		console.log("returnUrl : ", returnUrl)
		console.log("preUrl    : ", preUrl)
		
		// localStorage Save
		// 로컬 DEV
		if (preUrl.length > 0 ) {
			localStorage.setItem("requsetUrl", preUrl==null ? "/" : preUrl);
		} else {
			// 직링크로 이동한 경우
			localStorage.setItem("requsetUrl", "/");
		}
		
		if (returnUrl != "" && returnUrl != null) {
			// 입력받은 returnUrl이 있을때
			localStorage.setItem("requsetUrl", returnUrl);
		}
		
		location.href = "${authURL}/auth/common/login?client_id=${authClient}&redirect_uri=https://${hostName}/member/login_return";
	});
</script>