    public void downloadFile(String sourceFilePathName, String contentType, String destFileName, int blockSize) {
        if (sourceFilePathName == null || sourceFilePathName.trim().equals("")) {
            logger.error("文件 '" + sourceFilePathName + "' 没有找到.");
            throw new IllegalArgumentException("文件 '" + sourceFilePathName + "' 没有找到.");
        }
        if (!isVirtual(sourceFilePathName) && denyPhysicalPath) {
            logger.error("物理路径被拒绝.");
            throw new SecurityException("Physical path is denied.");
        }
        ServletOutputStream servletoutputstream = null;
        BufferedOutputStream bufferedoutputstream = null;
        FileInputStream fileIn = null;
        try {
            if (isVirtual(sourceFilePathName)) {
                sourceFilePathName = application.getRealPath(sourceFilePathName);
            }
            File file = new File(sourceFilePathName);
            fileIn = new FileInputStream(file);
            long fileLen = file.length();
            int readBytes = 0;
            int totalRead = 0;
            byte b[] = new byte[blockSize];
            if (contentType == null || contentType.trim().length() == 0) {
                response.setContentType("application/x-msdownload");
            } else {
                response.setContentType(contentType);
            }
            contentDisposition = contentDisposition != null ? contentDisposition : "attachment;";
            if (destFileName == null || destFileName.trim().length() == 0) {
                response.setHeader("Content-Disposition", contentDisposition + " filename=" + toUtf8String(getFileName(sourceFilePathName)));
            } else {
                response.setHeader("Content-Disposition", String.valueOf((new StringBuffer(String.valueOf(contentDisposition))).append(" filename=").append(toUtf8String(destFileName))));
            }
            servletoutputstream = response.getOutputStream();
            bufferedoutputstream = new BufferedOutputStream(servletoutputstream);
            while ((long) totalRead < fileLen) {
                readBytes = fileIn.read(b, 0, blockSize);
                totalRead += readBytes;
                bufferedoutputstream.write(b, 0, readBytes);
            }
            fileIn.close();
        } catch (JThinkRuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("下载文件时发生异常.", e);
            throw new JThinkRuntimeException(e);
        } finally {
            if (bufferedoutputstream != null) {
                try {
                    bufferedoutputstream.close();
                } catch (IOException e1) {
                    logger.error("关闭BufferedOutputStream时发生异常.", e1);
                    e1.printStackTrace();
                }
            }
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e1) {
                    logger.error("关闭FileInputStream时发生异常.", e1);
                    e1.printStackTrace();
                }
            }
        }
    }
