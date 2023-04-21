    private final void getJNIFiles() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DigestInputStream dis = new DigestInputStream(new URL(strAppletDir + "/" + JNI_ARCHIVE).openStream(), MessageDigest.getInstance("MD5"));
        IOUtil.copyStream(dis, baos);
        byte[] baResult = dis.getMessageDigest().digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < baResult.length; i++) {
            if ((0xff & baResult[i]) < 0x10) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(0xff & baResult[i]));
        }
        if (!sb.toString().equals(this.JNI_ARCHIVE_MD5)) {
            throw new Exception("Checksum failed on jni.");
        }
        String strLocalPath = System.getProperty("com.ms.sysdir");
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));
        ZipEntry ze = null;
        while (null != (ze = zis.getNextEntry())) {
            FileOutputStream fos = new FileOutputStream(strLocalPath + "\\" + ze.getName());
            IOUtil.copyStream(zis, fos);
        }
    }
