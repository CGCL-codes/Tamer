    public void downloadFile(final Versions.Bundle updateFileBundle, final String targetFileName) throws NoSuchAlgorithmException, IOException, InterruptedException {
        final File targetFile = new File(targetFileName);
        MessageDigest md = null;
        String hash = null;
        if (updateFileBundle.getSha256Hash() != null) {
            md = MessageDigest.getInstance("SHA-256");
            hash = updateFileBundle.getSha256Hash();
        } else if (updateFileBundle.getMd5Hash() != null) {
            md = MessageDigest.getInstance("MD5");
            hash = updateFileBundle.getMd5Hash();
        }
        final URL sourceUrl = new URL(updateFileBundle.getUrl());
        sourceUrl.openConnection();
        final InputStream in = sourceUrl.openStream();
        final FileOutputStream out = new FileOutputStream(targetFile);
        final byte[] buffer = new byte[bufferSize];
        int bytesRead = 0;
        while ((bytesRead = in.read(buffer)) > 0) {
            if (stopDownload) {
                throw new InterruptedException("thead stop has been flagged");
            }
            if (md != null) {
                md.update(buffer, 0, bytesRead);
            }
            out.write(buffer, 0, bytesRead);
        }
        out.close();
        in.close();
        if (stopDownload) {
            throw new InterruptedException("thead stop has been flagged");
        }
        if (md != null && hash != null) {
            final byte[] mdbytes = md.digest();
            for (int i = 0; i < mdbytes.length; i++) {
                final String hex = hash.substring(2 * i, 2 * i + 2);
                final byte subVal = (byte) Integer.valueOf(hex, 16).intValue();
                if (mdbytes[i] != subVal) {
                    targetFile.delete();
                    throw new IOException("file from URL \"" + updateFileBundle.getUrl() + "\" has wrong hash");
                }
            }
        }
    }
