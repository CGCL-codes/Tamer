    public void init(FilterConfig filterConfig) throws ServletException {
        ClassLoader loader = getClass().getClassLoader();
        URL rsrc_url = loader.getResource("jrp-internal.properties");
        jrpVersion = "unknown";
        if (rsrc_url != null) {
            try {
                Properties props = new Properties();
                props.load(rsrc_url.openStream());
                jrpVersion = props.getProperty("jrp.version");
            } catch (IOException e) {
            }
        }
        URL client_url = loader.getResource("jrp-client.js");
        if (client_url != null) {
            try {
                InputStream is = client_url.openStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int count;
                byte[] buf = new byte[1024];
                while ((count = is.read(buf)) != -1) {
                    if (count == 0) continue;
                    baos.write(buf, 0, count);
                }
                jrpClientJS = baos.toString("UTF-8");
                is.close();
                baos.close();
            } catch (IOException e) {
            }
        }
        if (jrpClientJS == null) {
        }
    }
