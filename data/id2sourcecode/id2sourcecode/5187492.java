    public static byte[] getBinaryContent(IFile file) throws CoreException {
        InputStreamReader in = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            file.refreshLocal(IResource.DEPTH_ZERO, null);
            bis = new BufferedInputStream(file.getContents());
            byte[] readBuffer = new byte[2048];
            int n = bis.read(readBuffer);
            while (n > 0) {
                baos.write(readBuffer, 0, n);
                n = bis.read(readBuffer);
            }
        } catch (Exception e) {
            throw GSearchCorePlugin.createException(e);
        } finally {
            close(in);
            close(bis);
        }
        return baos.toByteArray();
    }
