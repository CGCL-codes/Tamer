    public void read(Map env, OutputStream out, HeaderParser headerParser) throws IOException {
        InputStream natIn = null;
        try {
            byte[] buf = new byte[Util.BUF_SIZE];
            for (int i = 0; i < IScriptReader.HEADER.length; i++) {
                String key = IScriptReader.HEADER[i];
                String val = (String) env.get(key);
                if (val != null) conn.setRequestProperty(key, val);
            }
            String overrideHosts = (String) env.get(Util.X_JAVABRIDGE_OVERRIDE_HOSTS);
            if (overrideHosts != null) {
                conn.setRequestProperty(Util.X_JAVABRIDGE_OVERRIDE_HOSTS, overrideHosts);
                conn.setRequestProperty(Util.X_JAVABRIDGE_OVERRIDE_HOSTS_REDIRECT, overrideHosts);
            }
            natIn = conn.getInputStream();
            if (headerParser != HeaderParser.DEFAULT_HEADER_PARSER) {
                StringBuffer sbuf = new StringBuffer();
                for (Iterator ii = conn.getHeaderFields().entrySet().iterator(); ii.hasNext(); ) {
                    Map.Entry e = (Entry) ii.next();
                    List list = (List) e.getValue();
                    if (list.size() == 1) {
                        headerParser.addHeader(String.valueOf(e.getKey()), String.valueOf(list.get(0)));
                    } else {
                        appendListValues(sbuf, list);
                        headerParser.addHeader(String.valueOf(e.getKey()), sbuf.toString());
                        sbuf.setLength(0);
                    }
                }
            }
            int count;
            while ((count = natIn.read(buf)) > 0) out.write(buf, 0, count);
        } catch (IOException x) {
            Util.printStackTrace(x);
            throw x;
        } finally {
            if (natIn != null) try {
                natIn.close();
            } catch (IOException e) {
            }
        }
    }
