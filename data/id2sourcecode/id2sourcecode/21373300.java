    public void testEvidenceRecordTest() {
        for (int x = 0; x < inputFiles.length; x++) {
            InputStream inIS = Thread.currentThread().getContextClassLoader().getResourceAsStream(inputFiles[x]);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                while (true) {
                    int m = inIS.read();
                    if (m == -1) break;
                    baos.write(m);
                }
            } catch (Exception e) {
            }
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (Exception e) {
            }
            byte[] digestBytes = digest.digest(baos.toByteArray());
            String digestString = new String(Base64.encodeBase64(digestBytes));
            System.out.println(digestString);
        }
    }
