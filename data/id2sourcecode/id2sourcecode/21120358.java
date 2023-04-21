    public boolean readKey(PdfDictionary enc, byte[] password) throws BadPasswordException {
        try {
            if (password == null) password = new byte[0];
            byte[] oValue = com.itextpdf.text.DocWriter.getISOBytes(enc.get(PdfName.O).toString());
            byte[] uValue = com.itextpdf.text.DocWriter.getISOBytes(enc.get(PdfName.U).toString());
            byte[] oeValue = com.itextpdf.text.DocWriter.getISOBytes(enc.get(PdfName.OE).toString());
            byte[] ueValue = com.itextpdf.text.DocWriter.getISOBytes(enc.get(PdfName.UE).toString());
            byte[] perms = com.itextpdf.text.DocWriter.getISOBytes(enc.get(PdfName.PERMS).toString());
            boolean isUserPass = false;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password, 0, Math.min(password.length, 127));
            md.update(oValue, VALIDATION_SALT_OFFSET, SALT_LENGHT);
            md.update(uValue, 0, OU_LENGHT);
            byte[] hash = md.digest();
            boolean isOwnerPass = compareArray(hash, oValue, 32);
            if (isOwnerPass) {
                md.update(password, 0, Math.min(password.length, 127));
                md.update(oValue, KEY_SALT_OFFSET, SALT_LENGHT);
                md.update(uValue, 0, OU_LENGHT);
                hash = md.digest();
                AESCipherCBCnoPad ac = new AESCipherCBCnoPad(false, hash);
                key = ac.processBlock(oeValue, 0, oeValue.length);
            } else {
                md.update(password, 0, Math.min(password.length, 127));
                md.update(uValue, VALIDATION_SALT_OFFSET, SALT_LENGHT);
                hash = md.digest();
                isUserPass = compareArray(hash, uValue, 32);
                if (!isUserPass) throw new BadPasswordException(MessageLocalization.getComposedMessage("bad.user.password"));
                md.update(password, 0, Math.min(password.length, 127));
                md.update(uValue, KEY_SALT_OFFSET, SALT_LENGHT);
                hash = md.digest();
                AESCipherCBCnoPad ac = new AESCipherCBCnoPad(false, hash);
                key = ac.processBlock(ueValue, 0, ueValue.length);
            }
            AESCipherCBCnoPad ac = new AESCipherCBCnoPad(false, key);
            byte[] decPerms = ac.processBlock(perms, 0, perms.length);
            if (decPerms[9] != (byte) 'a' || decPerms[10] != (byte) 'd' || decPerms[11] != (byte) 'b') throw new BadPasswordException(MessageLocalization.getComposedMessage("bad.user.password"));
            permissions = (decPerms[0] & 0xff) | ((decPerms[1] & 0xff) << 8) | ((decPerms[2] & 0xff) << 16) | ((decPerms[2] & 0xff) << 24);
            encryptMetadata = decPerms[8] == (byte) 'T';
            return isOwnerPass;
        } catch (BadPasswordException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }
