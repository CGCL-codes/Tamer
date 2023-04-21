    public void start(Proxy proxy) throws Exception {
        InputStream in = null;
        URLConnection conn = null;
        OutputStream out = null;
        System.out.println("Download.start() " + this.fileFromServer);
        try {
            URL url = new URL(this.fileFromServer);
            out = new BufferedOutputStream(new FileOutputStream(this.localFile));
            if (proxy != null) {
                conn = url.openConnection(proxy);
            } else {
                conn = url.openConnection();
            }
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            this.status = IN_PROGRESS;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
            }
            System.out.println(this.localFile + "\t" + numWritten);
            file = new File(this.localFile);
            this.status = COMPLETED;
        } catch (Exception exception) {
            this.status = ERROR;
            throw exception;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
