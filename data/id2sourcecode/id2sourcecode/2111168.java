    public void test_calculateContent() {
        File file;
        String content = "Hello, world!";
        String expected;
        FileETag etag;
        try {
            file = File.createTempFile("temp", "txt");
            file.deleteOnExit();
            FileOutputStream out = new FileOutputStream(file);
            out.write(content.getBytes());
            out.flush();
            out.close();
            SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            long lastModified = date.parse("06/21/2007 11:19:36").getTime();
            file.setLastModified(lastModified);
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            expected = new String(Hex.encodeHex(messageDigest.digest(content.getBytes())));
            etag = new FileETag();
            etag.setFlags(FileETag.FLAG_CONTENT);
            String value = etag.calculate(file);
            assertEquals("Unexpected value", expected, value);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("Unexpected exception");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected exception");
        } catch (ParseException e) {
            e.printStackTrace();
            fail("Unexpected exception");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            fail("Unexpected exception");
        }
    }
