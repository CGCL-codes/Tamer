    public static void copy(String from, String to) throws IOException {
        BufferedInputStream bis = makeBIS(from);
        File toF = new File(to);
        BufferedOutputStream bos = makeBOS(toF);
        byte[] buf = new byte[4096];
        int nr;
        while ((nr = bis.read(buf, 0, buf.length)) > 0) bos.write(buf, 0, nr);
        try {
            bis.close();
            bos.close();
        } catch (IOException e) {
        }
    }
