    protected byte[] readInputStream(InputStream in) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buff = new byte[4096];
            int counter = 0;
            while ((counter = in.read(buff)) != -1) out.write(buff, 0, counter);
            return out.toByteArray();
        } catch (IOException ioex) {
            assertTrue("IOException happened during processing.", false);
            return null;
        }
    }
