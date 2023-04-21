    public void saveMessage(Object uid, InputStream source) throws Exception {
        File messageFile = getMessageFile(uid);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(messageFile));
        out.setMethod(ZipOutputStream.DEFLATED);
        ZipEntry zipentry = new ZipEntry(uid.toString());
        zipentry.setSize(source.available());
        out.putNextEntry(zipentry);
        StreamUtils.streamCopy(source, out);
        source.close();
        out.close();
    }
