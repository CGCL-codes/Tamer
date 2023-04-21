    public void send(PrintStream out) {
        out.println(getStatusHeader());
        for (String header : getHeaders()) {
            out.println(header);
        }
        out.println();
        if (url != null) {
            int n;
            byte[] buf = new byte[2048];
            BufferedInputStream bis = null;
            try {
                bis = new BufferedInputStream(url.openStream());
                while ((n = bis.read(buf)) > 0) {
                    out.write(buf, 0, n);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }
