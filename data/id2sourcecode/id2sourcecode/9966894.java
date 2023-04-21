    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        superToPdf(writer, os);
        os.write(STARTSTREAM);
        if (length > 0) {
            PdfEncryption crypto = null;
            if (writer != null) crypto = writer.getEncryption();
            if (offset < 0) {
                if (crypto == null) os.write(bytes); else {
                    crypto.prepareKey();
                    byte buf[] = new byte[length];
                    System.arraycopy(bytes, 0, buf, 0, length);
                    crypto.encryptRC4(buf);
                    os.write(buf);
                }
            } else {
                byte buf[] = new byte[Math.min(length, 4092)];
                RandomAccessFileOrArray file = writer.getReaderFile(reader);
                boolean isOpen = file.isOpen();
                try {
                    file.seek(offset);
                    int size = length;
                    PdfEncryption decrypt = reader.getDecrypt();
                    if (decrypt != null) {
                        decrypt.setHashKey(objNum, objGen);
                        decrypt.prepareKey();
                    }
                    if (crypto != null) crypto.prepareKey();
                    while (size > 0) {
                        int r = file.read(buf, 0, Math.min(size, buf.length));
                        size -= r;
                        if (decrypt != null) decrypt.encryptRC4(buf, 0, r);
                        if (crypto != null) crypto.encryptRC4(buf, 0, r);
                        os.write(buf, 0, r);
                    }
                } finally {
                    if (!isOpen) try {
                        file.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        os.write(ENDSTREAM);
    }
