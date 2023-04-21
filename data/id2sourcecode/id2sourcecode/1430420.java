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
                lCurrentPwsRec = new PwsRecordImpl();
                {
                    PwsField lPwsField = CodecUtil.readValue(lStream, lDataBuf, lFish);
                    if (lPwsField.getAsString().startsWith(" !!!Version 2 File Format!!!")) {
                        lPwsField = CodecUtil.readValue(lStream, lDataBuf, lFish);
                        if (!VERSION.equals(lPwsField.getAsString())) {
                            final String lMsg = "codec09";
                            throw new CodecException(lMsg);
                        }
                        lPwsField = CodecUtil.readValue(lStream, lDataBuf, lFish);
                        lDb.setParameters(lPwsField.getAsString());
                    } else {
                        final String lMsg = "codec08";
                        throw new CodecException(lMsg);
                    }
                }
                while (lDataBuf.remaining() + lStream.available() >= 8) {
                    PwsFieldImpl lPwsField = CodecUtil.readValue(lStream, lDataBuf, lFish);
                    if (lPwsField.getType().equals(PwsField.FIELD_EOR)) {
                        lDb.add(lCurrentPwsRec);
                        lCurrentPwsRec = new PwsRecordImpl();
                    } else {
                        lCurrentPwsRec.put(lPwsField);
                    }
                }
            } else {
                final String lMsg = "codec02";
                throw new CodecException(lMsg);
            }
        } catch (ModelException e) {
            final String lMsg = "codec03";
            throw new CodecException(lMsg);
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
