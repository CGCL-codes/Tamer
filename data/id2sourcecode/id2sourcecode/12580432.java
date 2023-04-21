        public void run() {
            int contentLength = 0, downloaded = 0;
            try {
                URLConnection conn;
                conn = url.openConnection();
                conn.connect();
                contentLength = conn.getContentLength();
                InputStream in = conn.getInputStream();
                int down = 0;
                byte buf[] = new byte[4096];
                FileOutputStream out = new FileOutputStream(downloadFile);
                downloaded = 0;
                while (true) {
                    down = in.read(buf);
                    if (down == -1) break;
                    downloaded += down;
                    out.write(buf, 0, down);
                    fireDownloadUpdated(new DownloadEvent(downloaded, contentLength));
                }
                in.close();
                out.close();
                contentLength = downloaded;
                fireDownloadCompleted(new DownloadEvent(downloaded, contentLength));
            } catch (IOException ioe) {
                fireDownloadAborted(new DownloadEvent(downloaded, contentLength), ioe);
            }
        }
