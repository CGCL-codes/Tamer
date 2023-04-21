    public boolean download(ReleaseDownload download) {
        Download dl;
        m_DownloadTab = new HTTPDownloadTab(m_Interface, download);
        while ((dl = download.getNextInLine()) != null) {
            FileOutputStream out = null;
            URLConnection conn = null;
            InputStream in = null;
            try {
                URL url = new URL(dl.getAddress());
                File file = new File(dl.getToFile());
                System.out.println("Tofile:" + dl.getToFile());
                System.out.println("addr: " + dl.getAddress());
                if (!file.exists()) {
                    if (file.isDirectory()) {
                        file.mkdirs();
                    } else {
                        System.out.println("PATH: " + file.getCanonicalPath());
                        File dir = new File(file.getCanonicalPath().substring(0, file.getCanonicalPath().lastIndexOf("\\")));
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        file.createNewFile();
                    }
                }
                out = new FileOutputStream(file);
                conn = url.openConnection();
                double fileSize = conn.getContentLength();
                dl.setFileSize((fileSize * 1024) * 1024);
                in = conn.getInputStream();
                byte[] buffer = new byte[2048];
                int numRead;
                long numWritten = 0;
                while (((numRead = in.read(buffer)) != -1) && !m_Cancel) {
                    out.write(buffer, 0, numRead);
                    numWritten += numRead;
                    dl.setProgress(numWritten);
                }
                in.close();
                out.flush();
                out.close();
                if (m_Cancel) {
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException ioe) {
                    return false;
                }
            }
        }
        return false;
    }
