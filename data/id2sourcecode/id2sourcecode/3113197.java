    public void createSAWSRecord(byte[] messageBlock, byte recordType, byte userID, byte encryptionFlag, PublicKey encryptionPublicKey) {
        byte[] bodyTemp = null;
        int thisRecordLength;
        if (debugLevel > SAWSConstant.NoInfo) {
            sawsDebugLog.write("\nSAWSWriter:createSAWSRecord: currentCount = " + (currentRecordWriteCount + 1));
        }
        java.security.MessageDigest md = null;
        try {
            md = java.security.MessageDigest.getInstance(this.hashAlgorithmName);
        } catch (Exception e) {
            this.showMessage("Message digest error. SAWS will stop.", SAWSTextOutputCallback.ERROR);
            if (debugLevel > SAWSConstant.NoInfo) {
                sawsDebugLog.write(e.toString());
            }
            System.exit(-1);
        }
        byte record[] = null;
        if (encryptionFlag == SAWSConstant.AsymmetricEncryptionFlag) {
            int messageLength = messageBlock.length;
            byte[] cipherText = null, c2 = null;
            try {
                javax.crypto.Cipher c1 = javax.crypto.Cipher.getInstance(encryptionPublicKey.getAlgorithm());
                c1.init(Cipher.ENCRYPT_MODE, encryptionPublicKey);
                cipherText = c1.doFinal(messageBlock);
                thisRecordLength = cipherText.length + SAWSConstant.HeaderLength + this.accMD.getDigestLength();
                record = new byte[thisRecordLength];
                ++currentRecordWriteCount;
                System.arraycopy(createRecordHeader(currentRecordWriteCount, recordType, userID, encryptionFlag, lastRecordLength, thisRecordLength), 0, record, 0, SAWSConstant.HeaderLength);
                lastRecordLength = thisRecordLength;
                System.arraycopy(cipherText, 0, record, SAWSConstant.HeaderLength, cipherText.length);
                bodyTemp = new byte[SAWSConstant.HeaderLength + cipherText.length];
                System.arraycopy(record, 0, bodyTemp, 0, SAWSConstant.HeaderLength + cipherText.length);
                md.reset();
                md.update(bodyTemp);
                md.update(secureRandomBytes);
                byte[] digest = md.digest();
                System.arraycopy(digest, 0, record, SAWSConstant.HeaderLength + cipherText.length, this.accMD.getDigestLength());
            } catch (Exception e) {
                e.printStackTrace();
                this.showMessage("Asymmetric encryption error. SAWS will stop.", SAWSTextOutputCallback.ERROR);
                if (debugLevel > SAWSConstant.NoInfo) {
                    sawsDebugLog.write(e.toString());
                }
                System.exit(-1);
            }
        }
        if (encryptionFlag == SAWSConstant.SymmetricEncryptionFlag) {
            int messageLength = messageBlock.length;
            byte[] encrypted = null;
            try {
                Cipher cipher = null;
                try {
                    Security.addProvider(new BouncyCastleProvider());
                    cipher = Cipher.getInstance("AES", "BC");
                } catch (Exception ex) {
                    cipher = Cipher.getInstance("AES");
                }
                sawsDebugLog.write("Provider: " + cipher.getProvider().getName());
                cipher.init(Cipher.ENCRYPT_MODE, symmetricKeyInLog);
                encrypted = cipher.doFinal(messageBlock);
                thisRecordLength = encrypted.length + SAWSConstant.HeaderLength + this.accMD.getDigestLength();
                record = new byte[thisRecordLength];
                ++currentRecordWriteCount;
                System.arraycopy(createRecordHeader(currentRecordWriteCount, recordType, userID, encryptionFlag, lastRecordLength, thisRecordLength), 0, record, 0, SAWSConstant.HeaderLength);
                lastRecordLength = thisRecordLength;
                System.arraycopy(encrypted, 0, record, SAWSConstant.HeaderLength, encrypted.length);
                bodyTemp = new byte[SAWSConstant.HeaderLength + encrypted.length];
                System.arraycopy(record, 0, bodyTemp, 0, SAWSConstant.HeaderLength + encrypted.length);
                md.reset();
                md.update(bodyTemp);
                md.update(secureRandomBytes);
                byte[] digest = md.digest();
                System.arraycopy(digest, 0, record, SAWSConstant.HeaderLength + encrypted.length, this.accMD.getDigestLength());
            } catch (Exception e) {
                this.showMessage("Symmetric encryption error. SAWS will stop.", SAWSTextOutputCallback.ERROR);
                if (debugLevel > SAWSConstant.NoInfo) {
                    sawsDebugLog.write(e.toString());
                }
                System.exit(-1);
            }
        }
        if (encryptionFlag == SAWSConstant.NoEncryptionFlag) {
            int messageLength = messageBlock.length;
            if (recordType != SAWSConstant.SAWSHashAlgorithmType) {
                thisRecordLength = messageLength + SAWSConstant.HeaderLength + this.accMD.getDigestLength();
                record = new byte[thisRecordLength];
                ++currentRecordWriteCount;
                System.arraycopy(createRecordHeader(currentRecordWriteCount, recordType, userID, encryptionFlag, lastRecordLength, thisRecordLength), 0, record, 0, SAWSConstant.HeaderLength);
                lastRecordLength = thisRecordLength;
                System.arraycopy(messageBlock, 0, record, SAWSConstant.HeaderLength, messageBlock.length);
                bodyTemp = new byte[SAWSConstant.HeaderLength + messageBlock.length];
                System.arraycopy(record, 0, bodyTemp, 0, SAWSConstant.HeaderLength + messageBlock.length);
                try {
                    md.reset();
                    md.update(bodyTemp);
                    md.update(secureRandomBytes);
                    byte[] digest = md.digest();
                    System.arraycopy(digest, 0, record, SAWSConstant.HeaderLength + messageBlock.length, this.accMD.getDigestLength());
                } catch (Exception e) {
                    this.showMessage("No-encryption record creating error. SAWS will stop.", SAWSTextOutputCallback.ERROR);
                    if (debugLevel > SAWSConstant.NoInfo) sawsDebugLog.write(e.toString());
                    System.exit(-1);
                }
            } else {
                thisRecordLength = messageLength + SAWSConstant.HeaderLength;
                record = new byte[thisRecordLength];
                ++currentRecordWriteCount;
                System.arraycopy(createRecordHeader(currentRecordWriteCount, recordType, userID, encryptionFlag, lastRecordLength, thisRecordLength), 0, record, 0, SAWSConstant.HeaderLength);
                lastRecordLength = thisRecordLength;
                System.arraycopy(messageBlock, 0, record, SAWSConstant.HeaderLength, messageBlock.length);
            }
        }
        if ((recordType != SAWSConstant.SAWSAccumulatedHashType) && (recordType != SAWSConstant.SAWSLogFileSignatureType)) {
            try {
                accMD.update(record);
                java.security.MessageDigest tc1 = (java.security.MessageDigest) accMD.clone();
                if (this.accumulatedHash == null) {
                    this.accumulatedHash = new byte[this.accMD.getDigestLength()];
                }
                accumulatedHash = tc1.digest();
            } catch (Exception e) {
                if (debugLevel > SAWSConstant.NoInfo) sawsDebugLog.write(e.toString());
            }
        }
        writeLogRecord(record);
    }
