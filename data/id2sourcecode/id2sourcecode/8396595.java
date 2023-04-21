    private InputSource attemptResolution(String public_id, String system_id) {
        if (system_id != null && !system_id.endsWith("/.")) {
            try {
                URL url = new URL(system_id);
                URLConnection conn = url.openConnection();
                conn.connect();
                InputStream stream = conn.getInputStream();
                InputSource src = new InputSource(system_id);
                src.setPublicId(public_id);
                src.setByteStream(stream);
                return src;
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            } catch (java.security.AccessControlException e) {
            }
        }
        return new InputSource(new java.io.StringReader(""));
    }
