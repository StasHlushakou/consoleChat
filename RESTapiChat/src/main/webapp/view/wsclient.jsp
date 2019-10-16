<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Security-Policy"  content="connect-src * 'unsafe-inline';">

    <link href="${pageContext.request.contextPath}/view/wsclient.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/view/wsclient.js"></script>



</head>
<body>
<h1>WebSocket Client</h1>
<!-- WebSocket Connection Parameters Table -->
<table>
    <tr>
        <td></td>
        <td>
            <input id="btnConnect"    type="button" value="Connect"    onclick="onConnectClick()">&nbsp;&nbsp;
            <input id="btnDisconnect" type="button" value="Disconnect" onclick="onDisconnectClick()" disabled="disabled">
        </td>
    </tr>
</table><br/>
<textarea id="incomingMsgOutput" rows="10" cols="20" disabled="disabled"></textarea>

<!-- Send Message Table -->
<table>
    <tr>
        <td width="200px">Message</td>
        <td><input type="text" id="message"/></td>
    </tr>
    <tr>
        <td></td>
        <td>
            <input id="btnSend" type="button" value="Send Message" disabled="disabled" onclick="onSendClick()">
        </td>
    </tr>
</table><br/>

</body>
</html>