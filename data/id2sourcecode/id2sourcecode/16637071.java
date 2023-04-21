    public PwsDatabase read(File aFile, String aPassphrase) throws CodecException {
        FileInputStream lStream = null;
        final PwsDatabaseImpl lDb = new PwsDatabaseImpl();
        lDb.setCodec(this);
        lDb.setPassphrase(aPassphrase);
        lDb.setFile(aFile);
        PwsRecordImpl lCurrentPwsRec = null;
        try {
            lStream = new FileInputStream(aFile);
            final ByteBuffer lDataBuf = ByteBuffer.allocateDirect(56);
            lDataBuf.order(ByteOrder.LITTLE_ENDIAN);
            lStream.getChannel().read(lDataBuf);
            lDataBuf.flip();
            final PwsFileHeader lPwsHeader = new PwsFileHeader();
            lPwsHeader.readFromBuffer(lDataBuf);
            if (CodecUtil.checkPassphrase(lPwsHeader, aPassphrase)) {
                final Blowfish lFish = CodecUtil.initBlowfish(lPwsHeader, aPassphrase);
                int lValueCounter = 0;
                while (lDataBuf.remaining() + lStream.available() >= 8) {
                    final byte[] lValueBuf = CodecUtil.readValue(lStream, lDataBuf, lFish).getValue();
                    lValueCounter++;
                    int lSelection = lValueCounter % 3;
                    if (lSelection == 0) {
                        final PwsFieldImpl lNotesField = new PwsFieldImpl(PwsField.FIELD_NOTES, lValueBuf);
                        lCurrentPwsRec.put(lNotesField);
                    } else if (lSelection == 1) {
                        lCurrentPwsRec = new PwsRecordImpl();
                        lDb.add(lCurrentPwsRec);
                        int lSplitBoundary = 0;
                        for (; (lSplitBoundary < lValueBuf.length) && (lValueBuf[lSplitBoundary] != (byte) 0xAD); lSplitBoundary++) ;
                        if (lSplitBoundary < lValueBuf.length) {
                            final int lTitleLen = lSplitBoundary;
                            final int lLoginLen = lValueBuf.length - lSplitBoundary - 1;
                            if (lTitleLen >= 2 && lLoginLen >= 2) {
                                final byte[] lTitle = new byte[lTitleLen - 2];
                                System.arraycopy(lValueBuf, 0, lTitle, 0, lTitleLen - 2);
                                final PwsFieldImpl lTitleField = new PwsFieldImpl(PwsField.FIELD_TITLE, lTitle);
                                lCurrentPwsRec.put(lTitleField);
                                final byte[] lLogin = new byte[lLoginLen - 2];
                                System.arraycopy(lValueBuf, lSplitBoundary + 3, lLogin, 0, lLoginLen - 2);
                                final PwsFieldImpl lLoginField = new PwsFieldImpl(PwsField.FIELD_UID, lLogin);
                                lCurrentPwsRec.put(lLoginField);
                            } else {
                                final byte[] lTitle = new byte[lTitleLen];
                                System.arraycopy(lValueBuf, 0, lTitle, 0, lTitleLen);
                                final PwsFieldImpl lTitleField = new PwsFieldImpl(PwsField.FIELD_TITLE, lTitle);
                                lCurrentPwsRec.put(lTitleField);
                                final byte[] lLogin = new byte[lLoginLen];
                                System.arraycopy(lValueBuf, lSplitBoundary + 1, lLogin, 0, lLoginLen);
                                final PwsFieldImpl lLoginField = new PwsFieldImpl(PwsField.FIELD_UID, lLogin);
                                lCurrentPwsRec.put(lLoginField);
                            }
                        } else {
                            int lDefaultBoundary = 0;
                            for (; (lDefaultBoundary < lValueBuf.length) && (lValueBuf[lDefaultBoundary] != (byte) 0xA0); lDefaultBoundary++) ;
                            if (lDefaultBoundary < lValueBuf.length) {
                                byte[] lTitle = new byte[lDefaultBoundary];
                                System.arraycopy(lValueBuf, 0, lTitle, 0, lDefaultBoundary);
                                final PwsFieldImpl lTitleField = new PwsFieldImpl(PwsField.FIELD_TITLE, lTitle);
                                lCurrentPwsRec.put(lTitleField);
                                final PwsFieldImpl lLoginField = new PwsFieldImpl(PwsField.FIELD_DEFAULT_UID, new byte[0]);
                                lCurrentPwsRec.put(lLoginField);
                            } else {
                                final PwsFieldImpl lTitleField = new PwsFieldImpl(PwsField.FIELD_TITLE, lValueBuf);
                                lCurrentPwsRec.put(lTitleField);
                            }
                        }
                    } else if (lSelection == 2) {
                        final PwsFieldImpl lPwdField = new PwsFieldImpl(PwsField.FIELD_PWD, lValueBuf);
                        lCurrentPwsRec.put(lPwdField);
                    }
                }
            } else {
                final String lMsg = "codec02";
                throw new CodecException(lMsg);
            }
        } catch (FileNotFoundException eEx) {
            final String lMsg = "codec00";
            throw new FileCodecException(lMsg, aFile.getAbsolutePath(), eEx);
        } catch (IOException eEx) {
            final String lMsg = "codec01";
            throw new FileCodecException(lMsg, aFile.getAbsolutePath(), eEx);
        } catch (BufferUnderflowException eEx) {
            final String lMsg = "codec01";
            throw new FileCodecException(lMsg, aFile.getAbsolutePath(), eEx);
        } finally {
            if (lStream != null) try {
                lStream.close();
            } catch (Exception e) {
            }
        }
        return lDb;
    }
