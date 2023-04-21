    public void run() {
        while (true) {
            try {
                Socket s = ss.accept();
                HTTPInputStream in = new HTTPInputStream(new BufferedInputStream(s.getInputStream()), null);
                OutputStream os = s.getOutputStream();
                HTTPHeader header = in.readHTTPHeader();
                String requesturi = header.getRequestURI();
                System.err.println(header.toString());
                FileInputStream fis = new FileInputStream(basedir + File.separator + requesturi);
                byte[] buf = new byte[1024];
                int read;
                while ((read = fis.read(buf)) > 0) os.write(buf, 0, read);
                in.close();
                os.close();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
