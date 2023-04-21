    private void dumpFile(String file, HttpServletResponse response) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        PrintWriter responseOut = response.getWriter();
        byte[] buff = new byte[10240];
        int read;
        while ((read = fis.read(buff)) > 0) {
            responseOut.write(new String(buff), 0, read);
        }
        fis.close();
    }
