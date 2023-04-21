    public static List unzip(String zipFileName, String unzipDirectory) throws Exception {
        String unzipDir = unzipDirectory;
        if (!unzipDir.endsWith(File.separator)) {
            unzipDir += File.separator;
        }
        if (!StringUtils.isEmpty(unzipDir)) {
            File file = new File(unzipDir);
            if (!file.isDirectory()) throw new IOException("Initial directory  is  not a directory!");
            if (!file.exists()) throw new IOException("Initial directory does not exists !");
            if (!file.canWrite()) {
                throw new IOException("Initial directory  is read-only!");
            } else {
                unzipDir = file.getCanonicalPath();
                unzipDir = unzipDir + File.separator;
            }
        }
        List<String> list = new ArrayList<String>();
        ZipFile zipfile = new ZipFile(zipFileName);
        for (Enumeration enumeration = zipfile.entries(); enumeration.hasMoreElements(); ) {
            String s1 = enumeration.nextElement().toString();
            String s2 = unzipDir + s1;
            ZipEntry zipentry = zipfile.getEntry(s1);
            if (File.separator.equals("\\")) {
                s1 = StringUtils.replace(s1, "/", File.separator);
                s2 = StringUtils.replace(s2, "/", File.separator);
            } else {
                s1 = StringUtils.replace(s1, "\\", File.separator);
                s2 = StringUtils.replace(s2, "\\", File.separator);
            }
            list.add(s2);
            if (!zipentry.isDirectory()) {
                File file = new File(s2);
                String s3 = file.getParent();
                if (s3 != null && !s3.equals("")) {
                    File file2 = new File(s3);
                    if (!file2.exists()) file2.mkdirs();
                }
                DataInputStream datainputstream = new DataInputStream(zipfile.getInputStream(zipentry));
                DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(s2));
                byte abyte0[] = new byte[1024];
                int i;
                while ((i = datainputstream.read(abyte0)) != -1) dataoutputstream.write(abyte0, 0, i);
                datainputstream.close();
                dataoutputstream.close();
            } else {
                File file1 = new File(s1);
                if (!file1.exists()) file1.mkdirs();
            }
        }
        return list;
    }
