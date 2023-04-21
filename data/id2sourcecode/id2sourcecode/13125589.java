    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClassLoader cl = this.getClass().getClassLoader();
        String uri = request.getRequestURI();
        String path = uri.substring(uri.indexOf(Utils.FCK_FACES_RESOURCE_PREFIX) + Utils.FCK_FACES_RESOURCE_PREFIX.length() + 1);
        if (getCustomResourcePath() != null) {
            this.getServletContext().getRequestDispatcher(getCustomResourcePath() + path).forward(request, response);
        } else {
            if (uri.endsWith(".jsf")) {
                response.setContentType("text/html;");
            } else {
                response.setHeader("Cache-Control", "public");
                response.setHeader("Last-Modified", modify);
            }
            if (uri.endsWith(".css")) {
                response.setContentType("text/css;");
            } else if (uri.endsWith(".js")) {
                response.setContentType("text/javascript;");
            } else if (uri.endsWith(".gif")) {
                response.setContentType("image/gif;");
            }
            InputStream is = cl.getResourceAsStream(path);
            if (is == null) return;
            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[2048];
            BufferedInputStream bis = new BufferedInputStream(is);
            int read = 0;
            read = bis.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = bis.read(buffer);
            }
            bis.close();
            out.flush();
            out.close();
        }
    }
