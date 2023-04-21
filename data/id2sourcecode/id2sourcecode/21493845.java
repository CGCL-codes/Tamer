    public void setCurrentDb(String dbFileName, String password) throws DbLocationException, InvalidPasswordException, DbFormatInvalidException {
        prepareCipher(Cipher.DECRYPT_MODE, password);
        ObjectInputStream inObjectStream = null;
        FileInputStream fin = null;
        DigestInputStream din = null;
        try {
            fin = new FileInputStream(dbFileName);
            inObjectStream = new ObjectInputStream(fin);
        } catch (FileNotFoundException e) {
            throw new DbLocationException();
        } catch (SecurityException e) {
            throw new DbLocationException();
        } catch (EOFException e) {
            throw new DbFormatInvalidException("DbManager.setCurrentDb: " + e.getMessage());
        } catch (StreamCorruptedException e) {
            throw new DbFormatInvalidException("DbManager.setCurrentDb: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Vector<FPCategory> catVector = new Vector<FPCategory>();
        try {
            byte hashedPwd[] = hashPassword(password);
            byte hashedPwdInFile[] = (byte[]) inObjectStream.readObject();
            if (!verifyPassword(hashedPwd, hashedPwdInFile)) throw new InvalidPasswordException();
            try {
                din = new DigestInputStream(new CipherInputStream(fin, cipher), md);
                md.reset();
                inObjectStream = new ObjectInputStream(din);
            } catch (Exception e) {
                throw new DbFormatInvalidException("DbManager.setCurrentDb: " + e.getMessage());
            }
            int numOfCat = inObjectStream.readInt();
            for (int i = 0; i < numOfCat; i++) catVector.add((FPCategory) inObjectStream.readObject());
            din.on(false);
            byte calculatedMD5[] = md.digest();
            byte fileMD5[] = (byte[]) inObjectStream.readObject();
            if (!MessageDigest.isEqual(calculatedMD5, fileMD5)) throw new DbFormatInvalidException("DbManager.setCurrentDb: File checksum test failed.");
            inObjectStream.close();
            din.close();
            fin.close();
        } catch (EOFException e) {
            throw new DbFormatInvalidException("DbManager.setCurrentDb: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new DbFormatInvalidException("DbManager.setCurrentDb: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("DbManager.setCurrentDb: " + e.getMessage());
        }
        unsetCurrentDb();
        Iterator<FPCategory> catIter = catVector.iterator();
        try {
            while (catIter.hasNext()) {
                FPCategory curCat = catIter.next();
                if (curCat.name().equals(CategoryManager.CONSTANT_CATEGORY)) {
                    catMan.replaceCategory(curCat);
                } else {
                    catMan.addCategory(curCat);
                }
                Iterator<Secret> secretIter = curCat.secrets(null);
                while (secretIter.hasNext()) secretMan.addSecret((FPSecret) secretIter.next());
            }
        } catch (Exception e) {
            System.out.println("DbManager.setCurrentDb: " + e.getMessage());
        }
    }
