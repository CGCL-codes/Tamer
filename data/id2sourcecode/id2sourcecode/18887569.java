    public void run() {
        try {
            s.setSoTimeout(to);
            Socket newsock = s.accept();
            InputStream is = data.getDataStream();
            OutputStream os = newsock.getOutputStream();
            byte[] b = new byte[1000];
            int len = -1;
            while ((len = is.read(b)) != -1) os.write(b, 0, len);
            is.close();
            os.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
