    public static String verifyCode(File file) throws EntityException {
        try {
            FileInputStream in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md = getMd5Digest();
            md.update(byteBuffer);
            return bufferToHex(md.digest());
        } catch (IOException e) {
            throw new EntityException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new EntityException(e);
        }
    }
