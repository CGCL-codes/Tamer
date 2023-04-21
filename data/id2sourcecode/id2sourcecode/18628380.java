    private String getFileMD5_v2(String fileName) throws Exception {
        String s = "";
        MP3File file = new MP3File();
        tagSize = (int) file.getMp3StartByte(new File(fileName));
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream fin = new FileInputStream(fileName);
        DigestInputStream in = new DigestInputStream(fin, md);
        fin.getChannel().position(tagSize);
        byte[] b = new byte[163840];
        in.read(b, 0, 163840);
        md = in.getMessageDigest();
        s = bytesToHex(md.digest());
        fin.close();
        in.close();
        return s;
    }
