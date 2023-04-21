    void appendObject(String name, Object o, StringBuffer sb) throws JSExn {
        if (o instanceof Number) {
            if ((double) ((Number) o).intValue() == ((Number) o).doubleValue()) {
                sb.append("                <" + name + " xsi:type=\"xsd:int\">");
                sb.append(((Number) o).intValue());
                sb.append("</" + name + ">\r\n");
            } else {
                sb.append("                <" + name + " xsi:type=\"xsd:double\">");
                sb.append(o);
                sb.append("</" + name + ">\r\n");
            }
        } else if (o instanceof Boolean) {
            sb.append("                <" + name + " xsi:type=\"xsd:boolean\">");
            sb.append(((Boolean) o).booleanValue() ? "true" : "false");
            sb.append("</" + name + ">\r\n");
        } else if (o instanceof Fountain) {
            try {
                sb.append("                <" + name + " xsi:type=\"SOAP-ENC:base64\">\r\n");
                InputStream is = ((Fountain) o).getInputStream();
                byte[] buf = new byte[54];
                while (true) {
                    int numread = is.read(buf, 0, 54);
                    if (numread == -1) break;
                    byte[] writebuf = buf;
                    if (numread < buf.length) {
                        writebuf = new byte[numread];
                        System.arraycopy(buf, 0, writebuf, 0, numread);
                    }
                    sb.append("              ");
                    sb.append(new String(Encode.toBase64(writebuf)));
                    sb.append("\r\n");
                }
                sb.append(((Boolean) o).booleanValue() ? "1" : "0");
                sb.append("</" + name + ">\r\n");
            } catch (IOException e) {
                logger.info(this, "caught IOException while attempting to send a Fountain via SOAP");
                logger.info(this, e);
                throw new JSExn("caught IOException while attempting to send a Fountain via SOAP");
            }
        } else if (o instanceof String) {
            sb.append("                <" + name + " xsi:type=\"xsd:string\">");
            String s = (String) o;
            if (s.indexOf('<') == -1 && s.indexOf('&') == -1) {
                sb.append(s);
            } else {
                char[] cbuf = s.toCharArray();
                while (true) {
                    int oldi = 0, i = 0;
                    while (i < cbuf.length && cbuf[i] != '<' && cbuf[i] != '&') i++;
                    sb.append(cbuf, oldi, i);
                    if (i == cbuf.length) break;
                    if (cbuf[i] == '<') sb.append("&lt;"); else if (cbuf[i] == '&') sb.append("&amp;");
                    i = oldi = i + 1;
                }
            }
            sb.append("</" + name + ">\r\n");
        } else if (o instanceof JSArray) {
            JSArray a = (JSArray) o;
            sb.append("                <" + name + " SOAP-ENC:arrayType=\"xsd:ur-type[" + a.size() + "]\">");
            for (int i = 0; i < a.size(); i++) appendObject("item", a.get(i), sb);
            sb.append("</" + name + ">\r\n");
        } else if (o instanceof JS) {
            JS j = (JS) o;
            sb.append("                <" + name + ">");
            JS.Enumeration e = j.keys().iterator();
            while (e.hasNext()) {
                Object key = e.next();
                appendObject((String) key, j.get((JS) key), sb);
            }
            sb.append("</" + name + ">\r\n");
        }
    }
