    public static byte[] getMD5(File aFile) {
        List<File> list = new ArrayList<File>();
        getFileListSorted(aFile, list);
        File actFile = null;
        byte[] result = null;
        try {
            MessageDigest md = MessageDigest.getInstance(MD5_ALGORITHM);
            md.reset();
            byte[] buffer = new byte[BUF_SIZE];
            int readBytes;
            for (File file : list) {
                actFile = file;
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(file), BUF_SIZE);
                while ((readBytes = in.read(buffer, 0, BUF_SIZE)) != -1) {
                    md.update(buffer, 0, readBytes);
                }
                System.out.println(file.getAbsolutePath());
            }
            result = md.digest();
        } catch (NoSuchAlgorithmException e) {
            ourLogger.error("cannot find MessageDigest Algorithm:" + MD5_ALGORITHM);
        } catch (FileNotFoundException e) {
            ourLogger.error(String.format("cannot find file <%s>", actFile), e);
        } catch (IOException e) {
            ourLogger.error("cannot read file", e);
        }
        return result;
    }
