    @SuppressWarnings("unchecked")
    @Override
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resource = getUrl();
        ComponentInputStream in = m2ccService.getComponentAsStream(resource);
        if (in == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            response.setContentType(in.getContentType());
            OutputStream out = response.getOutputStream();
            while (in.available() > 0) {
                out.write(in.read());
            }
            in.close();
            out.flush();
        }
    }
