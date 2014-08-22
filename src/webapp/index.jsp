<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html  
      xmlns="http://www.w3.org/1999/xhtml"  
      xml:lang="en"  
      lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/library/js/headscripts.js"></script>
<script type="text/javascript">
function setHeight() {
	//alert(window.frameElement.id);
	setMainFrameHeight(window.frameElement.id);
}
</script>
</head>
<!--body onload="setMainFrameHeight('Main<%=(String)request.getAttribute("toolId")%>');setFocus(focus_path);"-->
<body>
<div class="portletBody">
<iframe src="<%= (String) request.getAttribute("aspireURL") %>" width="100%" height="10000" frameborder="0" onload="setHeight();"/>
</div> <!-- /portletBody-->
</body>
</html>
