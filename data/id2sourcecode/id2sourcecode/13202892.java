    private String getFileMd5(File file) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            FileInputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            while (true) {
                int read = inputStream.read(buffer);
                if (read == -1) break;
                md5.update(buffer, 0, read);
            }
            inputStream.close();
            byte[] md5Bytes = md5.digest();
            StringBuffer md5Buffer = new StringBuffer(md5Bytes.length * 2);
            for (int i = 0; i < md5Bytes.length; i++) {
                String hex = Integer.toHexString(md5Bytes[i] & 0x00ff);
                if (hex.length() == 1) md5Buffer.append('0');
                md5Buffer.append(hex);
            }
            return md5Buffer.toString();
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }
