    private FileWrapper downloadTestFile(String urlStr, String localTempFilePrefix, String localTempFileSuffix) {
        InputStream is = null;
        FileWrapper result = null;
        try {
            URL url = new URL(urlStr);
            is = url.openStream();
            String prefix = "MyURLClassLoaderTest-" + localTempFilePrefix;
            result = FileWrapperImpl.createTempFile(prefix, localTempFileSuffix);
            final int bytesRead = ioutils.downloadHttpFile(url, result, null);
            assertTrue(bytesRead > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            ioutils.closeInputStream(is);
        }
        return result;
    }
