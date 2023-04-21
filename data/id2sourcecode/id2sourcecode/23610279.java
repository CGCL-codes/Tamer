    private COSStream makeUniqObjectNames(Map objectNameMap, COSStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(10240);
        byte[] buf = new byte[10240];
        int read;
        InputStream is = stream.getUnfilteredStream();
        while ((read = is.read(buf)) > -1) {
            baos.write(buf, 0, read);
        }
        buf = baos.toByteArray();
        baos = new ByteArrayOutputStream(buf.length + 100);
        StringBuffer sbObjectName = new StringBuffer(10);
        boolean bInObjectIdent = false;
        boolean bInText = false;
        boolean bInEscape = false;
        for (int i = 0; i < buf.length; i++) {
            byte b = buf[i];
            if (!bInEscape) {
                if (!bInText && b == '(') {
                    bInText = true;
                }
                if (bInText && b == ')') {
                    bInText = false;
                }
                if (b == '\\') {
                    bInEscape = true;
                }
                if (!bInText && !bInEscape) {
                    if (b == '/') {
                        bInObjectIdent = true;
                    } else if (bInObjectIdent && Character.isWhitespace((char) b)) {
                        bInObjectIdent = false;
                        String objectName = sbObjectName.toString().substring(1);
                        String newObjectName = objectName + "overlay";
                        baos.write('/');
                        baos.write(newObjectName.getBytes());
                        objectNameMap.put(objectName, COSName.getPDFName(newObjectName));
                        sbObjectName.delete(0, sbObjectName.length());
                    }
                }
                if (bInObjectIdent) {
                    sbObjectName.append((char) b);
                    continue;
                }
            } else {
                bInEscape = false;
            }
            baos.write(b);
        }
        COSDictionary streamDict = new COSDictionary();
        streamDict.setItem(COSName.LENGTH, new COSInteger(baos.size()));
        COSStream output = new COSStream(streamDict, pdfDocument.getDocument().getScratchFile());
        output.setFilters(stream.getFilters());
        OutputStream os = output.createUnfilteredStream();
        baos.writeTo(os);
        os.close();
        return output;
    }
