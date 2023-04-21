    @Test
    public void verify_validBase64_passes_large_string() throws XPathException, IOException {
        File home = ConfigurationHelper.getExistHome();
        File binaryFile = new File(home, "webapp/logo.jpg");
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        String base64data = null;
        try {
            is = new Base64InputStream(new FileInputStream(binaryFile), true, -1, null);
            baos = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int read = -1;
            while ((read = is.read(buf)) > -1) {
                baos.write(buf, 0, read);
            }
            base64data = new String(baos.toByteArray());
        } finally {
            if (is != null) {
                is.close();
            }
            if (baos != null) {
                baos.close();
            }
        }
        assertNotNull(base64data);
        TestableBase64BinaryValueType base64Type = new TestableBase64BinaryValueType();
        base64Type.verifyString(base64data);
    }
