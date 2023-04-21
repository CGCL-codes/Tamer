    public void render(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("JSONRPCView.render");
        System.out.println("JSONRPCView.render - existe kinase? = " + jsonRpcBridge.lookupObject("KinaseService"));
        response.setContentType("text/plain;charset=utf-8");
        OutputStream out = response.getOutputStream();
        String charset = request.getCharacterEncoding();
        if (charset == null) charset = "UTF-8";
        BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), charset));
        CharArrayWriter data = new CharArrayWriter();
        char buf[] = new char[buf_size];
        int ret;
        while ((ret = in.read(buf, 0, buf_size)) != -1) data.write(buf, 0, ret);
        JSONObject json_req = null;
        JSONRPCResult json_res = null;
        try {
            json_req = new JSONObject(data.toString());
            json_res = jsonRpcBridge.call(new Object[] { request }, json_req);
        } catch (ParseException e) {
            json_res = new JSONRPCResult(JSONRPCResult.CODE_ERR_PARSE, null, JSONRPCResult.MSG_ERR_PARSE);
        }
        byte[] bout = json_res.toString().getBytes("UTF-8");
        if (keepalive) {
            response.setIntHeader("Content-Length", bout.length);
        }
        System.out.println("JSONRPCView.render writing output");
        out.write(bout);
        out.flush();
        out.close();
    }
