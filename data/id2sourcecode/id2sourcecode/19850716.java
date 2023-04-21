    private void response() throws Exception {
        try {
            out.println("[RESPONSE]");
            if (verbose) {
                out.println("defaultUseCaches = " + connection.getDefaultUseCaches());
                out.println("useCaches = " + connection.getUseCaches());
                String field;
                for (int fieldId = 1; (field = connection.getHeaderFieldKey(fieldId)) != null; fieldId++) out.println("Header '" + field + "'=" + connection.getHeaderField(field));
            }
            out.println("Response Code= " + connection.getResponseCode());
            int len;
            char cbuf[] = new char[512];
            StringBuffer sbuf = new StringBuffer();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            while ((len = reader.read(cbuf)) != -1) sbuf.append(cbuf, 0, len);
            if (formatJson) {
                String str = sbuf.toString();
                if (str.charAt(0) == '(' && str.lastIndexOf(')') > -1) {
                    str = str.substring(1, str.lastIndexOf(')'));
                    String jsonResultStr = (str.charAt(0) == '[') ? (new JSONArray(str)).toString(4) : (new JSONObject(str)).toString(4);
                    out.println(jsonResultStr);
                } else out.println(sbuf);
            } else out.println(sbuf);
            out.println();
        } catch (Exception ex) {
            out.println("[Exception] " + ex);
            InputStream errorStream = connection.getErrorStream();
            if (errorStream == null) {
                throw ex;
            } else {
                char buf[] = new char[256];
                InputStreamReader reader = new InputStreamReader(errorStream);
                PrintWriter writer = new PrintWriter(out);
                int count;
                while ((count = reader.read(buf)) != -1) writer.write(buf, 0, count);
                writer.close();
            }
        } finally {
            connection.disconnect();
        }
    }
