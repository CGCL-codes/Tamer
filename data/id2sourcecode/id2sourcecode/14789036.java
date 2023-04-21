    public void uploadChunk(String key, String fileKey, long begin, ADOB filePartADOB, @SuppressWarnings("unused") ProgressListener lsn) throws UploadException {
        try {
            String fname = key + File.separator + fileKey + '#' + begin + '#';
            File outFile = new File(workDir, fname);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buf = new byte[1000];
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance(Constants.hashAlgorithm);
            } catch (NoSuchAlgorithmException e) {
            }
            InputStream is = filePartADOB.getInputStream();
            int n;
            long total = 0;
            try {
                while ((n = is.read(buf)) > 0) {
                    md.update(buf, 0, n);
                    fos.write(buf, 0, n);
                    total += n;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            is.close();
            fos.close();
            System.out.println("Uploaded: " + total);
            TransactionInfo ti = tranz.get(key);
            int cid = ti.addFileChunk(fileKey, new Chunk(begin, begin + total, md.digest()));
            outFile.renameTo(getChunkFile(key, fileKey, begin, total, cid));
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(workDir, key + File.separator + Constants.metaFileName)));
            oos.writeObject(ti);
            oos.close();
            filePartADOB.release();
        } catch (IOException e) {
            e.printStackTrace();
            throw new UploadException("IO Error", e, UploadException.IOError);
        }
    }
