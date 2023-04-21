    public byte[] readBytesFromURL(String srcName, String optUpdateMsg) {
        if (!srcName.startsWith("http://")) return (null);
        int bufSize = 20000, nBytesRead = 0, nBytesWritten = 0, oByteSize = bufSize;
        byte buf[] = null, oBuf[] = null;
        try {
            buf = new byte[bufSize];
            oBuf = new byte[bufSize];
            URL url = new URL(srcName);
            InputStream urlIS = url.openStream();
            while (true) {
                nBytesRead = urlIS.read(buf);
                if (nBytesRead == -1) break; else {
                    if (nBytesRead + nBytesWritten > oByteSize) {
                        byte tmp[] = new byte[oByteSize + bufSize];
                        for (int i = 0; i < nBytesWritten; i++) tmp[i] = oBuf[i];
                        oBuf = tmp;
                        oByteSize += bufSize;
                    }
                    for (int i = 0; i < nBytesRead; i++) oBuf[nBytesWritten++] = buf[i];
                    nBytesWritten += nBytesRead;
                }
            }
            byte tmp[] = new byte[nBytesWritten];
            for (int i = 0; i < nBytesWritten; i++) tmp[i] = oBuf[i];
            oBuf = tmp;
        } catch (Exception e1) {
            errMsgLog += "Can't read URL '" + srcName + "'\n";
            lastErrMsgLog = errMsgLog;
            return (null);
        }
        return (oBuf);
    }
