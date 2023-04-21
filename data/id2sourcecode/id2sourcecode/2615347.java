    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File mxml = new File(config.getServletContext().getRealPath(request.getRequestURI().substring(request.getContextPath().length())));
        String mxmlPath = mxml.getCanonicalPath();
        if (!mxml.exists() || !mxml.isFile()) throw new ServletException("Invalid MXML file: " + mxmlPath);
        File swf = new File(mxmlPath.substring(0, (mxmlPath.length() - 4)) + "swf");
        boolean compile = (!swf.exists() || swf.lastModified() < mxml.lastModified());
        if (!compile) {
            for (File dependency : findDependencies(config.getServletContext().getRealPath("/"), mxml)) {
                if (!mxml.equals(dependency) && swf.lastModified() < dependency.lastModified()) {
                    compile = true;
                    break;
                }
            }
        }
        if (compile) {
            String FS = System.getProperty("file.separator");
            List<String> command = new ArrayList<String>();
            command.add("java");
            command.add("-Dapplication.home=" + compilerHome.getAbsolutePath());
            command.add("-Xmx384M");
            command.add("-jar");
            command.add(compilerHome.getAbsolutePath() + FS + "lib" + FS + "mxmlc.jar");
            for (Map.Entry<String, String> entry : compilerOptions.entrySet()) {
                command.add(entry.getKey());
                command.add(entry.getValue());
            }
            command.add("-file-specs");
            command.add(mxmlPath);
            command.add("-context-root");
            command.add(request.getContextPath());
            command.add("-output");
            command.add(swf.getCanonicalPath());
            log.info("Executing: " + command);
            int status = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                processBuilder.directory(new File(config.getServletContext().getRealPath("/")));
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                StreamGobbler gobbler = new StreamGobbler(new BufferedInputStream(process.getInputStream()), baos);
                gobbler.start();
                status = process.waitFor();
            } catch (Exception e) {
                log.error("Could not call flex compiler: " + command, e);
                throw new ServletException("Could not call flex compiler: " + command, e);
            }
            log.info(baos.toString());
            if (status != 0) throw new ServletException("Error while compiling " + mxmlPath + ": " + baos.toString());
            swf = new File(swf.getCanonicalPath());
        }
        response.setContentType("application/x-shockwave-flash");
        response.setContentLength((int) swf.length());
        response.setBufferSize((int) swf.length());
        response.setDateHeader("Expires", 0);
        OutputStream os = null;
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(swf));
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
