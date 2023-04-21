    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        URL url = ContentServlet.class.getResource(format("/web%s", request.getRequestURI()));
        if (url != null) {
            FileSystemManager fsManager = VFS.getManager();
            FileObject file = fsManager.resolveFile(url.toExternalForm());
            if (file.getType() == FileType.FOLDER) {
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader("Location", format("%sindex.html", request.getRequestURI()));
                return;
            }
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            final PrintWriter out = response.getWriter();
            Reader in = new InputStreamReader(url.openStream());
            char[] buffer = new char[8192];
            int read;
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            out.flush();
            in.close();
            return;
        }
        error404(request, response);
    }
