    public void write(PwsDatabase aDb) throws CodecException {
        FileOutputStream lStream = null;
        File lFile = aDb.getFile();
        if (lFile == null) {
            final String lMsg = "codec05";
            throw new CodecException(lMsg);
        }
        final PwsFileHeader lPwsHeader = CodecUtil.initPwsHeader(aDb.getPassphrase());
        final Blowfish lFish = CodecUtil.initBlowfish(lPwsHeader, aDb.getPassphrase());
        try {
            lStream = new FileOutputStream(lFile);
            final ByteBuffer lDataBuf = ByteBuffer.allocateDirect(56);
            lDataBuf.order(ByteOrder.LITTLE_ENDIAN);
            lPwsHeader.writeToBuffer(lDataBuf);
            lStream.getChannel().write(lDataBuf);
            final Iterator lIter = aDb.iterator();
            while (lIter.hasNext()) {
                final PwsRecord lRecord = (PwsRecord) lIter.next();
                {
                    byte[] lTitleBuf = null;
                    byte[] lUidBuf = null;
                    byte[] lValueBuf = null;
                    if (lRecord.hasType(PwsField.FIELD_DEFAULT_UID)) {
                        if (lRecord.hasType(PwsField.FIELD_TITLE)) lTitleBuf = lRecord.get(PwsField.FIELD_TITLE).getValue(); else lTitleBuf = new byte[0];
                        lUidBuf = new byte[] { (byte) 0x20, (byte) 0x20, (byte) 0xA0 };
                        lValueBuf = new byte[lTitleBuf.length + lUidBuf.length];
                        System.arraycopy(lTitleBuf, 0, lValueBuf, 0, lTitleBuf.length);
                        System.arraycopy(lUidBuf, 0, lValueBuf, lTitleBuf.length, lUidBuf.length);
                    } else {
                        if (lRecord.hasType(PwsField.FIELD_TITLE)) lTitleBuf = lRecord.get(PwsField.FIELD_TITLE).getValue(); else lTitleBuf = new byte[0];
                        if (lRecord.hasType(PwsField.FIELD_UID)) lUidBuf = lRecord.get(PwsField.FIELD_UID).getValue(); else lUidBuf = new byte[0];
                        if ((lTitleBuf.length > 0) && (lUidBuf.length > 0)) {
                            lValueBuf = new byte[lTitleBuf.length + lUidBuf.length + 5];
                            System.arraycopy(lTitleBuf, 0, lValueBuf, 0, lTitleBuf.length);
                            lValueBuf[lTitleBuf.length + 0] = 32;
                            lValueBuf[lTitleBuf.length + 1] = 32;
                            lValueBuf[lTitleBuf.length + 2] = (byte) 0xAD;
                            lValueBuf[lTitleBuf.length + 3] = 32;
                            lValueBuf[lTitleBuf.length + 4] = 32;
                            System.arraycopy(lUidBuf, 0, lValueBuf, lTitleBuf.length + 5, lUidBuf.length);
                        } else if ((lTitleBuf.length > 0) && (lUidBuf.length == 0)) {
                            lValueBuf = new byte[lTitleBuf.length];
                            System.arraycopy(lTitleBuf, 0, lValueBuf, 0, lTitleBuf.length);
                        } else {
                            lValueBuf = new byte[lTitleBuf.length + lUidBuf.length + 1];
                            System.arraycopy(lTitleBuf, 0, lValueBuf, 0, lTitleBuf.length);
                            lValueBuf[lTitleBuf.length + 0] = (byte) 0xAD;
                            System.arraycopy(lUidBuf, 0, lValueBuf, lTitleBuf.length + 1, lUidBuf.length);
                        }
                    }
                    CodecUtil.writeValue(lStream, lDataBuf, lFish, new PwsFieldImpl(PwsField.FIELD_HEADER, lValueBuf));
                }
                {
                    byte[] lPwdbuf = null;
                    if (lRecord.hasType(PwsField.FIELD_PWD)) lPwdbuf = lRecord.get(PwsField.FIELD_PWD).getValue(); else lPwdbuf = new byte[0];
                    CodecUtil.writeValue(lStream, lDataBuf, lFish, new PwsFieldImpl(PwsField.FIELD_HEADER, lPwdbuf));
                }
                {
                    byte[] lNoteBuf = null;
                    if (lRecord.hasType(PwsField.FIELD_NOTES)) lNoteBuf = lRecord.get(PwsField.FIELD_NOTES).getValue(); else lNoteBuf = new byte[0];
                    CodecUtil.writeValue(lStream, lDataBuf, lFish, new PwsFieldImpl(PwsField.FIELD_HEADER, lNoteBuf));
                }
            }
            lDataBuf.flip();
            lStream.getChannel().write(lDataBuf);
            lDataBuf.flip();
        } catch (ModelException e) {
            final String lMsg = "codec07";
            throw new CodecException(lMsg, e);
        } catch (FileNotFoundException e) {
            final String lMsg = "codec06";
            throw new FileCodecException(lMsg, lFile.getAbsolutePath(), e);
        } catch (IOException e) {
            final String lMsg = "codec04";
            throw new FileCodecException(lMsg, lFile.getAbsolutePath(), e);
        } finally {
            if (lStream != null) try {
                lStream.flush();
                lStream.close();
            } catch (Exception eIgnore) {
            }
            ;
        }
    }
