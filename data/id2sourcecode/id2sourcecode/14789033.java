    public byte[] getFileSum(String key, String fileKey, long fileSize) throws UploadException {
        TransactionInfo ti = tranz.get(key);
        if (ti == null) {
            return null;
        }
        FileInfo fi = ti.getFileInfo(fileKey);
        if (fi == null) {
            return null;
        }
        if (fi.getDigest() != null) return fi.getDigest();
        List<Chunk> chlst = fi.getChunks();
        if (fi.getStatus() == FileInfo.STATUS.CHUNKED) throw new UploadException(fileKey, UploadException.FILE_INCOMPLETE);
        if (fi.getStatus() == FileInfo.STATUS.ASSEMBLED) {
            byte[] dig;
            try {
                dig = new FileDigest(new File(workDir, key + File.separator + fileKey)).getDigest();
            } catch (IOException e) {
                e.printStackTrace();
                throw new UploadException("Error in FileDigest", e, UploadException.IOError);
            }
            fi.setDigest(dig);
            return dig;
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(Constants.hashAlgorithm);
        } catch (NoSuchAlgorithmException e) {
        }
        Chunk prev = null;
        for (Chunk ch : chlst) {
            try {
                FileInputStream fis = new FileInputStream(getChunkFile(key, fileKey, ch.getBegin(), ch.getEnd() - ch.getBegin(), ch.getID()));
                if (prev != null && prev.getEnd() > ch.getBegin()) fis.skip(prev.getEnd() - ch.getBegin());
                byte[] buffer = new byte[10000];
                int l;
                while ((l = fis.read(buffer)) > 0) {
                    md.update(buffer, 0, l);
                }
                fis.close();
                prev = ch;
            } catch (IOException e) {
                e.printStackTrace();
                throw new UploadException("IO Error", e, UploadException.IOError);
            }
        }
        fi.setDigest(md.digest());
        return fi.getDigest();
    }
