    public String UpLoadfile(FormFile formfile, HttpServletRequest request, ActionServlet servlet) throws Exception {
        Pattern p = Pattern.compile(".html|.jsp|.exe|.bat");
        Matcher m = p.matcher(formfile.getFileName());
        if (m.matches()) {
            throw new RuntimeException("非法的文件名!");
        }
        String startPath = "";
        File file = new File(servlet.getServletContext().getRealPath(startPath));
        if (!file.exists()) {
            file.mkdirs();
        }
        String DatabasePath = StringTool.getAutoFileName(formfile.getFileName());
        String fullPath = servlet.getServletContext().getRealPath(startPath + "/" + DatabasePath);
        BufferedInputStream in = null;
        BufferedOutputStream fos = null;
        try {
            in = new BufferedInputStream(formfile.getInputStream());
            fos = new BufferedOutputStream(new FileOutputStream(new File(fullPath)), diskBufferSize);
            int read = 0;
            byte buffer[] = new byte[diskBufferSize];
            while ((read = in.read(buffer, 0, diskBufferSize)) > 0) {
                fos.write(buffer, 0, read);
            }
            buffer = null;
            fos.flush();
            in.close();
            fos.close();
        } finally {
            in = null;
            fos = null;
        }
        return DatabasePath;
    }
