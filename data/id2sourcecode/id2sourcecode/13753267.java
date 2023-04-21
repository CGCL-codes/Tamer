    public static void s_copyFromJarred(String strPathRelIn, File fleOut) throws Exception {
        InputStream ism = GfrFile.class.getResourceAsStream("/" + strPathRelIn);
        OutputStream out = new FileOutputStream(fleOut);
        byte buf[] = new byte[1024];
        int len;
        while ((len = ism.read(buf)) > 0) out.write(buf, 0, len);
        out.close();
        ism.close();
    }
