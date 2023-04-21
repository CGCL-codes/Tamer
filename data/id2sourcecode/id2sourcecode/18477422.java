    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html; charset=UTF-8");
        ServletOutputStream out = res.getOutputStream();
        String uploadFilesFolder = dms_home + FS + "www" + FS + "datafiles";
        boolean update = false;
        boolean uploaded = false;
        String serverFileName = "";
        UniqueFileName ufn = new UniqueFileName();
        try {
            MultipartRequest parser = new ServletMultipartRequest(req, MultipartRequest.MAX_READ_BYTES, MultipartRequest.IGNORE_FILES_IF_MAX_BYES_EXCEEDED, null);
            for (Enumeration e = parser.getFileParameterNames(); e.hasMoreElements(); ) {
                String name = (String) e.nextElement();
                InputStream in = parser.getFileContents(name);
                if (in != null) {
                    BufferedInputStream input = new BufferedInputStream(in);
                    FileOutputStream file = new FileOutputStream(new File(uploadFilesFolder, parser.getBaseFilename(name)));
                    int read;
                    byte[] buffer = new byte[4096];
                    while ((read = input.read(buffer)) != -1) {
                        file.write(buffer, 0, read);
                    }
                    file.close();
                    input.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.println("done");
    }
