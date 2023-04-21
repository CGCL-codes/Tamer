    public boolean md5verify() {
        if (!fileExists(name + ".mod")) return false;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] data = readFile(name + ".mod");
            byte[] results = md5.digest(data);
            for (int x = 0; x < 0x10; x++) if (results[x] != md5Hash[x]) return false;
            return true;
        } catch (Exception e) {
            System.out.println("md5Verify failed: " + e.toString());
        }
        return false;
    }
