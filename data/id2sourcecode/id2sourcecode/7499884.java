    public Object parseJSON(java.net.URL url) {
        String json;
        java.io.Reader reader = null;
        java.io.BufferedReader br = null;
        try {
            reader = new java.io.InputStreamReader(url.openStream());
            java.io.StringWriter sw = new java.io.StringWriter();
            br = new java.io.BufferedReader(reader);
            int read;
            CommentState state = CommentState.NONE;
            int buffer = -1;
            while ((read = br.read()) >= 0) {
                switch(state) {
                    case NONE:
                        if (read == '/') {
                            if (buffer == '/') {
                                buffer = -1;
                                state = CommentState.LINE;
                                continue;
                            }
                            if (buffer >= 0) sw.write(buffer);
                            buffer = read;
                            continue;
                        } else if (read == '*' && buffer == '/') {
                            state = CommentState.BLOCK;
                            buffer = -1;
                            continue;
                        } else {
                            if (buffer >= 0) {
                                sw.write(buffer);
                                buffer = -1;
                            }
                            sw.write(read);
                        }
                        break;
                    case LINE:
                        if (read == '\n' || read == '\r') state = CommentState.NONE;
                        break;
                    case BLOCK:
                        if (read == '*') buffer = read; else if (read == '/') {
                            if (buffer == '*') state = CommentState.NONE;
                            buffer = -1;
                        } else buffer = -1;
                        break;
                }
            }
            json = sw.toString();
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Could not find JSON at " + url, e);
        } finally {
            if (br != null) try {
                br.close();
            } catch (java.io.IOException e) {
                log.error("Could not close stream", e);
            }
        }
        JSONObject schema;
        try {
            schema = (JSONObject) theParser.parse(new java.io.StringReader(json), new SAJParser.DefaultHandler());
        } catch (Throwable e) {
            throw new IllegalStateException("Could not parse JSON at " + url + ":\n" + json, e);
        }
        if (schema == null) throw new IllegalStateException("Could not parse JSON at " + url + ":\n" + json);
        return schema;
    }
