    public static void unZipFiles(String zipFileName, String descFileName) {
        if (!descFileName.endsWith(File.separator)) {
            descFileName = descFileName + File.separator;
        }
        try {
            ZipFile zipFile = new ZipFile(zipFileName);
            ZipEntry entry = null;
            String entryName = null;
            String descFileDir = null;
            byte[] buf = new byte[4096];
            int readByte = 0;
            Enumeration enums = zipFile.entries();
            while (enums.hasMoreElements()) {
                entry = (ZipEntry) enums.nextElement();
                entryName = entry.getName();
                descFileDir = descFileName + entryName;
                if (entry.isDirectory()) {
                    new File(descFileDir).mkdirs();
                    continue;
                } else {
                    new File(descFileDir).getParentFile().mkdirs();
                }
                File file = new File(descFileDir);
                FileOutputStream fouts = new FileOutputStream(file);
                InputStream ins = zipFile.getInputStream(entry);
                while ((readByte = ins.read(buf)) != -1) {
                    fouts.write(buf, 0, readByte);
                }
                fouts.close();
                ins.close();
            }
            System.out.println("�ļ���ѹ�ɹ�!");
        } catch (Exception e) {
            System.out.println("�ļ���ѹʧ�ܣ�" + e.getMessage());
        }
    }
