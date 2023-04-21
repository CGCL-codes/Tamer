    public static void main(String[] args) throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        Database db = new Database("Database.kdb");
        db.setMasterKey(Hex.decode("83b62ec2690df02ce7b2f94208469decd93fb0d2febbc2408c86ae7860f5d6af"));
        db.setPasswordHash(sha256.digest("password".getBytes()));
        db.decrypt();
        for (Group g : db.getGroups()) System.out.println(g);
        for (Entry e : db.getEntries()) System.out.println(e);
    }
