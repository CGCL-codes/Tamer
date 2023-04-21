    public void execute(HttpServletRequest request, HttpServletResponse response, HttpMethodBase method) throws Exception {
        if (_debug) System.out.println(this.toString() + " : executing.");
        _ps.execute(_client, method, _params, request, response);
        if (_debug) System.out.println(this.toString() + " : returning data...");
        try {
            byte[] buffer = new byte[8000];
            OutputStream os = response.getOutputStream();
            InputStream is = _params.getResponseBodyInputStream();
            int bytesread = 0;
            while ((bytesread = is.read(buffer)) > 0) {
                os.write(buffer, 0, bytesread);
            }
        } catch (PortletStreamException err) {
        }
    }
