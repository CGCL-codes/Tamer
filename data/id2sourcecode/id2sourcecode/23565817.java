    private Long doWebPost(BeanWrapper bean, String url, XPathExpression trackingIdXPath, String xpath, Map<String, ?> attributes) {
        String respXml = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection hConn = (HttpURLConnection) conn;
                hConn.setRequestMethod("POST");
            }
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            conn.setRequestProperty("Accept", "text/*");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(this.connectionTimeout);
            conn.setReadTimeout(connectionTimeout);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            PropertyDescriptor[] props = bean.getPropertyDescriptors();
            Object theNodeId = this.nodeId;
            if (attributes != null && attributes.containsKey("node-id")) {
                theNodeId = attributes.get("node-id");
            }
            out.write("nodeId=" + theNodeId);
            for (int i = 0; i < props.length; i++) {
                PropertyDescriptor prop = props[i];
                if (prop.getReadMethod() == null) {
                    continue;
                }
                String propName = prop.getName();
                if ("class".equals(propName)) {
                    continue;
                }
                Object propValue = null;
                if (attributes != null && attributes.containsKey(propName)) {
                    propValue = attributes.get(propName);
                } else {
                    PropertyEditor editor = bean.findCustomEditor(prop.getPropertyType(), prop.getName());
                    if (editor != null) {
                        editor.setValue(bean.getPropertyValue(propName));
                        propValue = editor.getAsText();
                    } else {
                        propValue = bean.getPropertyValue(propName);
                    }
                }
                if (propValue == null) {
                    continue;
                }
                out.write("&");
                out.write(propName);
                out.write("=");
                out.write(URLEncoder.encode(propValue.toString(), "UTF-8"));
            }
            out.flush();
            out.close();
            BufferedReader resp = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String str;
            while ((str = resp.readLine()) != null) {
                buf.append(str);
            }
            respXml = buf.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (log.isLoggable(Level.FINER)) {
            log.finer("Response: " + respXml);
        }
        return extractTrackingId(respXml, trackingIdXPath, xpath);
    }
