    public static boolean upZipMxl(String target, String mxlfile) throws Exception {
        ZipFile zf = new ZipFile(mxlfile);
        Enumeration en = zf.entries();
        List<String> subMxls = new ArrayList<String>();
        while (en.hasMoreElements()) {
            ZipEntry fi = (ZipEntry) en.nextElement();
            if (fi.isDirectory()) {
                File f = new File(target + fi.getName());
                f.mkdirs();
            } else {
                if (fi.getName().toLowerCase().endsWith("mxl")) subMxls.add(target + fi.getName());
                InputStream in = zf.getInputStream(fi);
                FileOutputStream out = new FileOutputStream(target + fi.getName());
                byte[] buf = new byte[2048];
                int len = 0;
                while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
                out.close();
                in.close();
            }
        }
        zf.close();
        for (String subMxl : subMxls) {
            String subDir = subMxl.substring(subMxl.lastIndexOf(File.separator), subMxl.lastIndexOf("."));
            upZipMxl(target + subDir, subMxl);
        }
        return true;
    }
