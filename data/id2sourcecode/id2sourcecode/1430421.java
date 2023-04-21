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
            CodecUtil.writeValue(lStream, lDataBuf, lFish, new PwsFieldImpl(PwsField.FIELD_HEADER, " !!!Version 2 File Format!!! Please upgrade to PasswordSafe 2.0 or later".getBytes()));
            CodecUtil.writeValue(lStream, lDataBuf, lFish, new PwsFieldImpl(PwsField.FIELD_HEADER, getVersion().getBytes()));
            CodecUtil.writeValue(lStream, lDataBuf, lFish, new PwsFieldImpl(PwsField.FIELD_HEADER, aDb.getParameters() != null ? aDb.getParameters().getBytes() : new byte[0]));
            final Iterator lIter = aDb.iterator();
            while (lIter.hasNext()) {
                final PwsRecord lRecord = (PwsRecord) lIter.next();
                final Iterator lTypeIter = lRecord.typeIterator();
                while (lTypeIter.hasNext()) {
                    final Byte lFieldType = (Byte) lTypeIter.next();
                    CodecUtil.writeValue(lStream, lDataBuf, lFish, lRecord.get(lFieldType));
                }
                CodecUtil.writeValue(lStream, lDataBuf, lFish, new PwsFieldImpl(PwsField.FIELD_EOR, new byte[0]));
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
