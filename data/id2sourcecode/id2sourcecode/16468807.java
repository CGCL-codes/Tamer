    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean force = false;
        String sForce = request.getParameter("force");
        if (sForce != null && sForce.equals("true")) force = true;
        boolean isLibrary = false;
        String sIsLibrary = request.getParameter("islibrary");
        if (sIsLibrary != null && sIsLibrary.equals("true")) isLibrary = true;
        boolean virtual = false;
        String name = "";
        String content = "";
        String sVirtual = request.getParameter("virtual");
        if (sVirtual != null && sVirtual.equals("true")) {
            virtual = true;
            if (request.getParameter("name") != null) name = request.getParameter("name");
            if (request.getParameter("content") != null) content = request.getParameter("content");
        }
        String extension = "";
        WebCompilerType type;
        if (isLibrary) {
            extension = WebCompiler.LIBRARY_EXTENSION;
            type = WebCompilerType.library;
        } else {
            extension = WebCompiler.APPLICATION_EXTENSION;
            type = WebCompilerType.application;
        }
        File swfFile = null;
        if (!virtual) {
            File mxmlFile = new File(servletConfig.getServletContext().getRealPath(request.getRequestURI().substring(request.getContextPath().length() + 2)));
            String mxmlPath = mxmlFile.getCanonicalPath();
            swfFile = new File(mxmlPath.substring(0, (mxmlPath.length() - 4)) + extension);
            try {
                swfFile = webCompiler.compileMxmlFile(mxmlFile, swfFile, force, type);
            } catch (WebCompilerException e) {
                response.getWriter().append(e.getMessage());
                return;
            }
        } else {
            VirtualLocalFileSystem vFileSystem = new VirtualLocalFileSystem();
            VirtualLocalFile vFile = vFileSystem.create(servletConfig.getServletContext().getRealPath("/") + name, content, new File(servletConfig.getServletContext().getRealPath("/")), (new Date()).getTime());
            String mxmlPath = vFile.getName();
            swfFile = new File(mxmlPath.substring(0, (mxmlPath.length() - 4)) + extension);
            try {
                swfFile = webCompiler.compileMxmlVirtualFile(vFile, swfFile, force, type);
            } catch (WebCompilerException e) {
                response.getWriter().append(e.getMessage());
                return;
            }
        }
        response.setContentType("application/x-shockwave-flash");
        response.setContentLength((int) swfFile.length());
        response.setBufferSize((int) swfFile.length());
        response.setDateHeader("Expires", 0);
        OutputStream os = null;
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(swfFile));
            os = response.getOutputStream();
            for (int b = is.read(); b != -1; b = is.read()) os.write(b);
        } finally {
            if (is != null) try {
                is.close();
            } finally {
                if (os != null) os.close();
            }
        }
    }
