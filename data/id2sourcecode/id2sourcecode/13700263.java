    public void start() {
        if (!m_doneInit) init();
        Thread t = new Thread(new Runnable() {

            public void run() {
                InputStream is = null;
                BufferedInputStream bis = null;
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                try {
                    is = m_connection.getInputStream();
                    bis = new BufferedInputStream(is, 1024);
                    m_tempFile = File.createTempFile("aclocator.", ".tmp");
                    fos = new FileOutputStream(m_tempFile);
                    bos = new BufferedOutputStream(fos, 1024);
                    byte[] buffer = new byte[1024];
                    int readcount;
                    double totalcount = (double) m_connection.getContentLength();
                    double totalreadcount = 0;
                    while ((!m_cancelled) && ((readcount = bis.read(buffer, 0, buffer.length)) != -1)) {
                        bos.write(buffer, 0, readcount);
                        totalreadcount += (double) readcount;
                        fireTransferUpdateEvent(totalreadcount / totalcount);
                    }
                    bos.flush();
                    fos.flush();
                    bos.close();
                    fos.close();
                    bis.close();
                    is.close();
                    if (m_cancelled) {
                        m_connection.disconnect();
                    }
                    fireTransferCompleteEvent();
                    return;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    try {
                        if (bos != null) bos.close();
                    } catch (IOException iox) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException iox) {
                    }
                    try {
                        if (bis != null) bis.close();
                    } catch (IOException iox) {
                    }
                    try {
                        if (is != null) is.close();
                    } catch (IOException iox) {
                    }
                }
                m_cancelled = true;
                fireTransferFailedEvent();
            }
        });
        t.start();
    }
