    public void saveCurrentDb(String dbFileName, String password) throws DbLocationException, InvalidPasswordException {
        prepareCipher(Cipher.ENCRYPT_MODE, password);
        ObjectOutputStream outObjectStream = null;
        FileOutputStream fout = null;
        DigestOutputStream dout = null;
        try {
            fout = new FileOutputStream(dbFileName);
            outObjectStream = new ObjectOutputStream(fout);
        } catch (FileNotFoundException e) {
            throw new DbLocationException();
        } catch (Exception e) {
            System.out.println("DbManager.setCurrentDb: " + e.getMessage());
        }
        Iterator<Category> catIter = catMan.searchCategories(null, CategoryManager.SearchMethod.SUBSTRING_MATCH);
        try {
            byte hashedPwd[] = hashPassword(password);
            outObjectStream.writeObject(hashedPwd);
            try {
                dout = new DigestOutputStream(new CipherOutputStream(fout, cipher), md);
                md.reset();
                outObjectStream = new ObjectOutputStream(dout);
            } catch (Exception e) {
                System.out.println("DbManager.setCurrentDb: " + e.getMessage());
            }
            outObjectStream.writeInt(catMan.getNumCategories());
            while (catIter.hasNext()) outObjectStream.writeObject(catIter.next());
            dout.on(false);
            byte fileMD5[] = md.digest();
            outObjectStream.writeObject(fileMD5);
            outObjectStream.close();
            dout.close();
            fout.close();
        } catch (Exception e) {
            System.out.println("DbManager.saveCurrentDb: " + e.getMessage());
        }
    }
