    public void saveHash(File outputFile) {
        try {
            if (!outputFile.exists()) {
                new File(outputFile.getParent()).mkdirs();
            }
            DataOutputStream bOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
            bOut.writeInt((int) this.in.length());
            System.out.println("FileSize:" + this.in.length());
            MessageDigest hashSum = MessageDigest.getInstance("SHA-256");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BufferedInputStream fb = new BufferedInputStream(new FileInputStream(in));
            RollingChecksum32 r32 = new RollingChecksum32();
            byte[] buffer = new byte[blockSize];
            int readSize = fb.read(buffer);
            while (readSize > 0) {
                r32.check(buffer, 0, readSize);
                md5.reset();
                md5.update(buffer, 0, readSize);
                hashSum.update(buffer, 0, readSize);
                readSize = fb.read(buffer);
                final byte[] engineDigest = md5.digest();
                bOut.writeInt(r32.getValue());
                bOut.write(engineDigest);
            }
            byte[] fileHash = new byte[hashSum.getDigestLength()];
            fileHash = hashSum.digest();
            bOut.write(fileHash);
            bOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
