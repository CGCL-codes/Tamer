    public void setupAllKeys(byte userPassword[], byte ownerPassword[], int permissions) {
        if (ownerPassword == null || ownerPassword.length == 0) ownerPassword = md5.digest(createDocumentId());
        permissions |= (revision == STANDARD_ENCRYPTION_128 || revision == AES_128 || revision == AES_256) ? 0xfffff0c0 : 0xffffffc0;
        permissions &= 0xfffffffc;
        this.permissions = permissions;
        if (revision == AES_256) {
            try {
                if (userPassword == null) userPassword = new byte[0];
                documentID = createDocumentId();
                byte[] uvs = IVGenerator.getIV(8);
                byte[] uks = IVGenerator.getIV(8);
                key = IVGenerator.getIV(32);
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(userPassword, 0, Math.min(userPassword.length, 127));
                md.update(uvs);
                userKey = new byte[48];
                md.digest(userKey, 0, 32);
                System.arraycopy(uvs, 0, userKey, 32, 8);
                System.arraycopy(uks, 0, userKey, 40, 8);
                md.update(userPassword, 0, Math.min(userPassword.length, 127));
                md.update(uks);
                AESCipherCBCnoPad ac = new AESCipherCBCnoPad(true, md.digest());
                ueKey = ac.processBlock(key, 0, key.length);
                byte[] ovs = IVGenerator.getIV(8);
                byte[] oks = IVGenerator.getIV(8);
                md.update(ownerPassword, 0, Math.min(ownerPassword.length, 127));
                md.update(ovs);
                md.update(userKey);
                ownerKey = new byte[48];
                md.digest(ownerKey, 0, 32);
                System.arraycopy(ovs, 0, ownerKey, 32, 8);
                System.arraycopy(oks, 0, ownerKey, 40, 8);
                md.update(ownerPassword, 0, Math.min(ownerPassword.length, 127));
                md.update(oks);
                md.update(userKey);
                ac = new AESCipherCBCnoPad(true, md.digest());
                oeKey = ac.processBlock(key, 0, key.length);
                byte[] permsp = IVGenerator.getIV(16);
                permsp[0] = (byte) permissions;
                permsp[1] = (byte) (permissions >> 8);
                permsp[2] = (byte) (permissions >> 16);
                permsp[3] = (byte) (permissions >> 24);
                permsp[4] = (byte) (255);
                permsp[5] = (byte) (255);
                permsp[6] = (byte) (255);
                permsp[7] = (byte) (255);
                permsp[8] = encryptMetadata ? (byte) 'T' : (byte) 'F';
                permsp[9] = (byte) 'a';
                permsp[10] = (byte) 'd';
                permsp[11] = (byte) 'b';
                ac = new AESCipherCBCnoPad(true, key);
                perms = ac.processBlock(permsp, 0, permsp.length);
            } catch (Exception ex) {
                throw new ExceptionConverter(ex);
            }
        } else {
            byte userPad[] = padPassword(userPassword);
            byte ownerPad[] = padPassword(ownerPassword);
            this.ownerKey = computeOwnerKey(userPad, ownerPad);
            documentID = createDocumentId();
            setupByUserPad(this.documentID, userPad, this.ownerKey, permissions);
        }
    }
