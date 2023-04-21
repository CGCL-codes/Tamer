    @Test
    public void compareChecksum() throws Throwable {
        int imageIndex = 1;
        InputStream is = getClass().getResourceAsStream(filepath);
        System.out.println("####################################");
        System.out.println("File: " + filepath);
        DefaultInputStreamFactory disf = new DefaultInputStreamFactory();
        ImageInputStream iis = disf.getInputStream(is);
        JBIG2Document doc = new JBIG2Document(iis);
        long time = System.currentTimeMillis();
        Bitmap b = doc.getPage(imageIndex).getBitmap();
        long duration = System.currentTimeMillis() - time;
        byte[] digest = MessageDigest.getInstance("MD5").digest(b.getByteArray());
        StringBuilder stringBuilder = new StringBuilder();
        for (byte toAppend : digest) {
            stringBuilder.append(toAppend);
        }
        System.out.println("Completed decoding in " + duration + " ms");
        System.out.println("####################################\n");
        Assert.assertEquals(checksum, stringBuilder.toString());
    }
