    public void doExecute() throws VFSException {
        BufferedInputStream is = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            is = new BufferedInputStream(new DigestInputStream(file.getInputStream(), digest));
            byte[] buffer = new byte[4096];
            while (is.read(buffer) != -1) {
                if (isStopped()) {
                    throw new ManipulationStoppedException(this);
                }
            }
            hash = digest.digest();
        } catch (IOException e) {
            throw new VFSIOException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new VFSSystemException(e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
        }
    }
