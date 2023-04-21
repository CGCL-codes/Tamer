    public void downloadFile(String sourceFilePathName, String contentType, String destFileName, int blockSize) throws SmartUploadException, IOException, ServletException {
        String tempFilename = destFileName;
        destFileName = java.net.URLEncoder.encode(tempFilename, "UTF-8");
        if (destFileName.length() > 150) {
            String guessCharset = "gb2312";
            destFileName = new String(tempFilename.getBytes(guessCharset), "ISO8859-1");
        }
        if (sourceFilePathName == null) throw new IllegalArgumentException(String.valueOf((new StringBuffer("File '")).append(sourceFilePathName).append("' not found (1040).")));
        if (sourceFilePathName.equals("")) throw new IllegalArgumentException(String.valueOf((new StringBuffer("File '")).append(sourceFilePathName).append("' not found (1040).")));
        if (!isVirtual(sourceFilePathName) && m_denyPhysicalPath) throw new SecurityException("Physical path is denied (1035).");
        if (isVirtual(sourceFilePathName)) sourceFilePathName = m_application.getRealPath(sourceFilePathName);
        java.io.File file = new java.io.File(sourceFilePathName);
        FileInputStream fileIn = new FileInputStream(file);
        long fileLen = file.length();
        int readBytes = 0;
        int totalRead = 0;
        byte b[] = new byte[blockSize];
        if (contentType == null) m_response.setContentType("application/x-msdownload"); else if (contentType.length() == 0) m_response.setContentType("application/x-msdownload"); else m_response.setContentType(contentType);
        m_response.setContentLength((int) fileLen);
        m_contentDisposition = m_contentDisposition != null ? m_contentDisposition : "attachment;";
        if (destFileName == null) m_response.setHeader("Content-Disposition", String.valueOf((new StringBuffer(String.valueOf(m_contentDisposition))).append(" filename=").append(getFileName(sourceFilePathName)))); else if (destFileName.length() == 0) m_response.setHeader("Content-Disposition", m_contentDisposition); else m_response.setHeader("Content-Disposition", String.valueOf((new StringBuffer(String.valueOf(m_contentDisposition))).append(" filename=").append(destFileName)));
        while ((long) totalRead < fileLen) {
            readBytes = fileIn.read(b, 0, blockSize);
            totalRead += readBytes;
            m_response.getOutputStream().write(b, 0, readBytes);
        }
        fileIn.close();
    }
