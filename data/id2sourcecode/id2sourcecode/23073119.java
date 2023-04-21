    private String checkSum(String path) {
        File f = new File(path);
        if (f.isDirectory() || !f.exists() || !f.canRead()) {
            return "0";
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                FileInputStream fis = new FileInputStream(f);
                byte[] buffer = new byte[4096];
                while (true) {
                    int read = fis.read(buffer);
                    if (read == -1) {
                        break;
                    }
                    if (read == 0) {
                        continue;
                    }
                    md.update(buffer, 0, read);
                }
                fis.close();
                return byteArrayToHexString(md.digest());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return "0";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "0";
            } catch (IOException e) {
                e.printStackTrace();
                return "0";
            }
        }
    }
