    public File store(Artifact artifact, InputStream stream, byte[] remoteMD5, boolean failonmd5) throws SavantException {
        String artifactFile = artifact.getArtifactFile();
        File file = new File(localCache, artifactFile);
        construct(file);
        try {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("MD5");
                digest.reset();
            } catch (NoSuchAlgorithmException e) {
                System.err.println("Unable to locate MD5 algorithm");
                System.exit(1);
            }
            FileOutputStream os = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            BufferedInputStream bis = new BufferedInputStream(stream);
            DigestInputStream dis = new DigestInputStream(bis, digest);
            dis.on(true);
            byte[] ba = new byte[1024];
            int count;
            while ((count = dis.read(ba, 0, 1024)) != -1) {
                bos.write(ba, 0, count);
            }
            dis.close();
            bos.close();
            bis.close();
            os.close();
            byte[] localMD5 = digest.digest();
            if (remoteMD5 != null && localMD5 != null && !Arrays.equals(localMD5, remoteMD5)) {
                if (failonmd5) {
                    file.delete();
                    throw new SavantException("Downloaded artifact [" + artifact + "] corrupt according to MD5. Attempt rebuilding when remote" + " repository is fixed");
                } else {
                    Log.log("Downloaded artifact [" + artifact + "] corrupt" + " according to MD5.", Log.WARN);
                }
            }
        } catch (FileNotFoundException fnfe) {
            throw new SavantException(fnfe);
        } catch (IOException ioe) {
            throw new SavantException(ioe);
        }
        return file;
    }
