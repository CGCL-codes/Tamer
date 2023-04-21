    public void read(Model model, java.lang.String url) {
        try {
            URLConnection conn = new URL(url).openConnection();
            String encoding = conn.getContentEncoding();
            if (encoding == null) read(model, new InputStreamReader(conn.getInputStream()), url, url); else read(model, new InputStreamReader(conn.getInputStream(), encoding), url, url);
        } catch (JenaException e) {
            if (errorHandler == null) throw e;
            errorHandler.error(e);
        } catch (Exception ex) {
            if (errorHandler == null) throw new JenaException(ex);
            errorHandler.error(ex);
        }
    }
