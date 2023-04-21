    public void testRemoteExecReaderWriter() {
        File pp = new File("test" + File.separator + "input" + File.separator + "ein-pp");
        InputStream is = null;
        InputStream ris = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count = 0;
        try {
            is = new FileInputStream(pp);
            Reader reader = new InputStreamReader(is, CharsetUtil.forName("x-PICA"));
            ris = new ReaderInputStream(reader, CharsetUtil.forName("x-PICA"));
            while ((count = ris.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
            out.flush();
            if (LOGGER.isDebugEnabled()) {
                OutputStream fout = new FileOutputStream("test" + File.separator + "output" + File.separator + "remoteExecReaderWriter.out");
                fout.write(out.toByteArray());
                fout.close();
            }
            byte[] expected = new byte[(int) pp.length()];
            InputStream fis = new FileInputStream(pp);
            fis.read(expected);
            fis.close();
            Assert.assertArrayEquals(expected, out.toByteArray());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (ris != null) {
                try {
                    ris.close();
                } catch (IOException e) {
                }
            }
        }
    }
