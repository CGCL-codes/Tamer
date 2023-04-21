    public static String streamFile(HttpServletResponse response, File file) {
        if (file == null) return "No File";
        if (!file.exists()) return "File not found: " + file.getAbsolutePath();
        MimeType mimeType = MimeType.get(file.getAbsolutePath());
        try {
            int bufferSize = 2048;
            int fileLength = (int) file.length();
            response.setContentType(mimeType.getMimeType());
            response.setBufferSize(bufferSize);
            response.setContentLength(fileLength);
            log.fine(file.toString());
            long time = System.currentTimeMillis();
            FileInputStream in = new FileInputStream(file);
            ServletOutputStream out = response.getOutputStream();
            int c = 0;
            while ((c = in.read()) != -1) out.write(c);
            out.flush();
            out.close();
            in.close();
            time = System.currentTimeMillis() - time;
            double speed = (fileLength / 1024) / ((double) time / 1000);
            log.info("Length=" + fileLength + " - " + time + " ms - " + speed + " kB/sec - " + mimeType);
        } catch (IOException ex) {
            log.log(Level.SEVERE, ex.toString());
            return "Streaming error - " + ex;
        }
        return null;
    }
