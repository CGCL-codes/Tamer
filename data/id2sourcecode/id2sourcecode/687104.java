    public boolean copyFile(String srcName, String dstName) {
        try {
            FileOutputStream dstFOS = new FileOutputStream(new File(dstName));
            FileInputStream srcFIS = null;
            int bufSize = 20000, nBytesRead = 0, nBytesWritten = 0;
            byte buf[] = new byte[bufSize];
            boolean isURL = (srcName.startsWith("http://"));
            if (isURL) {
                URL url = new URL(srcName);
                InputStream urlIS = url.openStream();
                while (true) {
                    nBytesRead = urlIS.read(buf);
                    if (nBytesRead == -1) break; else {
                        dstFOS.write(buf, 0, nBytesRead);
                        nBytesWritten += nBytesRead;
                    }
                }
                dstFOS.close();
            } else {
                srcFIS = new FileInputStream(new File(srcName));
                while (true) {
                    nBytesRead = srcFIS.read(buf);
                    if (nBytesRead == -1) break; else {
                        dstFOS.write(buf, 0, nBytesRead);
                        nBytesWritten += nBytesRead;
                    }
                }
                srcFIS.close();
                dstFOS.close();
            }
        } catch (Exception e1) {
            errMsgLog += "Can't copy '" + srcName + "' to '" + dstName + "'  " + e1 + "\n";
            lastErrMsgLog = errMsgLog;
            return (false);
        }
        return (true);
    }
