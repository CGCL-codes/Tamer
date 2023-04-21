    private static GenericResponse eseguiFormGenerica(GenericForm form, ProxyConfig proxy) throws IOException {
        URL url;
        URLConnection urlConn;
        DataOutputStream printout;
        DataInputStream input;
        if (proxy != null && proxy.getUseProxy()) {
            Properties sysProps = System.getProperties();
            sysProps.put("proxySet", "true");
            sysProps.put("proxyHost", proxy.getProxyHost());
            sysProps.put("proxyPort", proxy.getProxyPort());
        }
        url = new URL(form.getAction());
        urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", form.getContentType());
        printout = new DataOutputStream(urlConn.getOutputStream());
        StringBuffer buf = new StringBuffer();
        Iterator<Map.Entry<String, String>> iterator = form.getParametriRequest().entrySet().iterator();
        Map.Entry<String, String> ele;
        while (iterator.hasNext()) {
            ele = iterator.next();
            if (buf.length() > 0) buf.append("&");
            buf.append(ele.getKey());
            buf.append("=");
            buf.append(ele.getValue());
        }
        String content = buf.toString();
        printout.writeBytes(content);
        printout.flush();
        printout.close();
        input = new DataInputStream(urlConn.getInputStream());
        String str;
        StringBuffer res = new StringBuffer();
        while (null != ((str = input.readLine()))) {
            res.append(str);
        }
        input.close();
        GenericResponse response = new GenericResponse();
        String[] stra = res.toString().split("&");
        String[] s;
        if (stra != null) {
            for (int i = 0; i < stra.length; i++) {
                s = stra[i].split("=");
                response.getParametri().put(s[0], s[1]);
            }
        }
        return response;
    }
