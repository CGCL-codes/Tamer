    protected void outputHtml(HttpServletRequest request, HttpServletResponse response, String servletUrl, String url) throws IOException {
        try {
            response.getWriter().print(new Trimmer(servletUrl, url).digest());
        } catch (Exception e) {
            reportError(response, "digest failed. Exception: " + e);
            return;
        }
    }
