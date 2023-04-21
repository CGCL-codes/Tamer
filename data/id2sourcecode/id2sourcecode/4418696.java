    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassCastException {
        HttpSession session = request.getSession();
        JSONRPCBridge json_bridge = null;
        json_bridge = (JSONRPCBridge) session.getAttribute("JSONRPCBridge");
        if (json_bridge == null) {
            if (!auto_session_bridge) {
                json_bridge = JSONRPCBridge.getGlobalBridge();
                if (json_bridge.isDebug()) log.info("Using global bridge.");
            } else {
                json_bridge = new JSONRPCBridge();
                session.setAttribute("JSONRPCBridge", json_bridge);
                if (json_bridge.isDebug()) log.info("Created a bridge for this session.");
            }
        }
        response.setContentType("text/plain;charset=utf-8");
        OutputStream out = response.getOutputStream();
        String charset = request.getCharacterEncoding();
        if (charset == null) charset = "UTF-8";
        BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), charset));
        CharArrayWriter data = new CharArrayWriter();
        char buf[] = new char[buf_size];
        int ret;
        while ((ret = in.read(buf, 0, buf_size)) != -1) data.write(buf, 0, ret);
        if (json_bridge.isDebug()) log.fine("recieve: " + data.toString());
        JSONObject json_req = null;
        JSONRPCResult json_res = null;
        try {
            json_req = new JSONObject(data.toString());
            json_res = json_bridge.call(new Object[] { request }, json_req);
        } catch (ParseException e) {
            log.severe("can't parse call: " + data);
            json_res = new JSONRPCResult(JSONRPCResult.CODE_ERR_PARSE, null, JSONRPCResult.MSG_ERR_PARSE);
        }
        if (json_bridge.isDebug()) log.fine("send: " + json_res.toString());
        byte[] bout = json_res.toString().getBytes("UTF-8");
        if (keepalive) {
            response.setIntHeader("Content-Length", bout.length);
        }
        out.write(bout);
        out.flush();
        out.close();
    }
