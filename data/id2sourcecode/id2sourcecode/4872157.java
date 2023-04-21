    public String SHA1sum() {
        try {
            sha1 = MessageDigest.getInstance("SHA1");
            fis = new FileInputStream(filename);
            byte[] dataBytes = new byte[1024];
            int read = 0;
            while ((read = fis.read(dataBytes)) != -1) {
                sha1.update(dataBytes, 0, read);
            }
            byte[] mdbytes = sha1.digest();
            sb = new StringBuilder("");
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (IOException ex) {
            System.out.println("Can't read file to count SHA-1 hash, check your permissions");
            System.exit(1);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Problem with SHA-1 hash");
            System.exit(1);
        }
        return sb.toString();
    }
