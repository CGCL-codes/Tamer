    public LogRecordInfo getOneRecordInfoFromRAF(RandomAccessFile raf1, byte[] secureRandomBytes) throws logReadingException {
        java.security.MessageDigest md = null;
        try {
            md = java.security.MessageDigest.getInstance(this.hashAlgorithmName);
        } catch (Exception e) {
            this.showMessage("Message Digest error. SAWS will stop.", SAWSTextOutputCallback.ERROR);
            if (debugLevel > SAWSConstant.NoInfo) sawsDebugLog.write(e.toString());
            System.exit(-1);
        }
        byte[] h1 = readRecordHeaderFromRAF(raf1);
        if (h1 == null) return null;
        int thisRecordLength = getThisRecordLengthFromRecordHeader(h1);
        LogRecordInfo recordInf = new LogRecordInfo();
        currentSNCheck = getSNFromRecordHeader(h1);
        recordInf.setSn(currentSNCheck);
        byte recordType = (byte) getRecordTypeFromRecordHeader(h1);
        recordInf.setRecordType(recordType);
        recordInf.setEncryptionFlag(getEncryptionTypeString(getEncryptionFlagFromRecordHeader(h1)));
        recordInf.setTimeStamp(new Date(getTimestampFromRecordHeader(h1)));
        recordInf.setLastRecordLength(getLastRecordLengthFromRecordHeader(h1));
        recordInf.setRecordLength(thisRecordLength);
        byte[] body = null;
        byte[] hash1 = null;
        body = readRecordBodyFromRAF(raf1, 0, thisRecordLength - this.accMD.getDigestLength() - SAWSConstant.HeaderLength);
        hash1 = readRecordHashFromRAF(raf1);
        byte[] decryptedBody = null;
        byte[] newAccumulatedHashByCalc = null;
        if (recordType == SAWSConstant.SAWSAccumulatedHashType) {
            accumulatedRecordMetFlag = true;
            accumulatedHashFromLog = body;
        } else if (recordType == SAWSConstant.SAWSLogFileSignatureType) {
            signatureFromLog = body;
            if (accumulatedRecordMetFlag) logEndFlag = true;
        } else {
            if (recordType == SAWSConstant.SAWSCertificateType) {
                ByteArrayInputStream bais = new ByteArrayInputStream(body);
                java.security.cert.CertificateFactory cf = null;
                try {
                    cf = java.security.cert.CertificateFactory.getInstance("X.509");
                    certFromLog = cf.generateCertificate(bais);
                } catch (Exception e3) {
                    e3.printStackTrace(System.err);
                }
            }
            if ((sawsSecretKeyFromLog == null) && (recordType == SAWSConstant.SymmetricEncryptionKeyType) && (getUserIDFromRecordHeader(h1) != SAWSConstant.USERSAWS) && (userPrivateKey != null)) {
                byte[] rawKey1 = ADecryptRecordBodyByPrivateKey(body, userPrivateKey);
                if (rawKey1 != null) {
                    sawsSecretKeyFromLog = new SecretKeySpec(rawKey1, "AES");
                }
                logFileNameForLastLog = null;
            }
            if ((recordType == SAWSConstant.SymmetricEncryptionKeyType) && (getUserIDFromRecordHeader(h1) == SAWSConstant.USERSAWS) && (sawsPrivateKey != null)) {
                byte[] rawKey1 = ADecryptRecordBodyByPrivateKey(body, sawsPrivateKey);
                sawsSecretKeyFromLog = new SecretKeySpec(rawKey1, "AES");
                logFileNameForLastLog = null;
            }
            byte[] clearMessageBlock = null;
            if (getEncryptionFlagFromRecordHeader(h1) == SAWSConstant.SymmetricEncryptionFlag) {
                clearMessageBlock = SDecryptRecordBody(body);
            } else {
                clearMessageBlock = body;
            }
            if (recordType == SAWSConstant.SAWSLastFileType) {
                extractLastLogInfo(clearMessageBlock);
            }
            if (recordType == SAWSConstant.SAWSClientLogDataType) {
                recordInf.setData(new String(clearMessageBlock));
                RecordBlock rb = new RecordBlock(clearMessageBlock, getUserIDFromRecordHeader(h1));
                recordBlockList.addElement(rb);
            }
            if (recordType == SAWSConstant.SysAuditorNotificationType) {
                recordInf.setData(new String(clearMessageBlock));
            }
            int thisRecordLength2 = 0;
            byte[] thisLogRecord2 = null;
            if (recordType == SAWSConstant.SAWSHashAlgorithmType) {
                recordInf.setData(this.hashAlgorithmName);
            }
            if (recordType == SAWSConstant.SAWSSigningAlgorithmType) {
                this.signingAlgorithm = new String(body);
                recordInf.setData(this.signingAlgorithm);
            }
            thisRecordLength2 = h1.length + body.length + hash1.length;
            thisLogRecord2 = new byte[thisRecordLength2];
            System.arraycopy(h1, 0, thisLogRecord2, 0, h1.length);
            System.arraycopy(body, 0, thisLogRecord2, h1.length, body.length);
            System.arraycopy(hash1, 0, thisLogRecord2, h1.length + body.length, hash1.length);
            if (!accumulatedRecordMetFlag) {
                accMD.update(thisLogRecord2);
                try {
                    java.security.MessageDigest tc1 = (java.security.MessageDigest) accMD.clone();
                    accumulatedHashByCalc = tc1.digest();
                } catch (Exception e4) {
                    this.showMessage("Accumulated hash error. SAWS will stop.", SAWSTextOutputCallback.ERROR);
                    if (debugLevel > SAWSConstant.NoInfo) {
                        sawsDebugLog.write(e4.toString());
                    }
                    System.exit(-1);
                }
                if (recordType == SAWSConstant.SAWSHeaderSignatureType) {
                    this.accumulatedHashForLogHeader = this.accumulatedHashByCalc;
                    this.headerSignature = body;
                }
            }
        }
        if ((secureRandomBytes != null)) {
            md.reset();
            md.update(h1);
            md.update(body);
            md.update(secureRandomBytes);
            byte[] digest = md.digest();
            String s1 = new String(Base64.encode(digest));
            String s2 = new String(Base64.encode(hash1));
            if (s1.compareTo(s2) == 0) {
                this.showMessage("The secure hash of this log record is verified.", SAWSTextOutputCallback.INFORMATION);
            } else {
                throw new logReadingException(SAWSConstant.SecureHashNotCorrect, currentSNCheck);
            }
        }
        try {
            if (raf1.getFilePointer() < raf1.length() - 1) {
                return recordInf;
            } else {
                return null;
            }
        } catch (Exception e) {
            this.showMessage("Log file get file pointer error. SAWS will stop.", SAWSTextOutputCallback.ERROR);
            if (debugLevel > SAWSConstant.NoInfo) sawsDebugLog.write(e.toString());
            System.exit(-1);
        }
        return recordInf;
    }
