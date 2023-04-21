    private void renderFile(ResponseWriter out, String file) {
        String host = (String) FacesUtils.getFc().getExternalContext().getRequestHeaderMap().get("Host");
        String context = FacesUtils.getFc().getExternalContext().getRequestContextPath();
        try {
            URL url = new URL("http://" + host + context + "/pages/" + file);
            InputStream in = url.openStream();
            int b = in.read();
            while (b > -1) {
                out.write(b);
                b = in.read();
            }
        } catch (Exception e) {
            throw new FacesRuntimeException(logger, "Error importing " + file + ": " + e.getMessage(), e);
        }
    }
