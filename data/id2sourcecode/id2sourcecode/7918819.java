    public static byte[] readBytes(ZipFile theFile, String theEntry) {
        byte[] bytes = null;
        if (theFile != null) {
            try {
                ZipEntry entry = theFile.getEntry(theEntry);
                if (entry != null) {
                    byte[] buffer = new byte[BASE_BUFFER_SIZE];
                    ByteArrayOutputStream out = new ByteArrayOutputStream(BASE_BUFFER_SIZE);
                    InputStream in = theFile.getInputStream(entry);
                    int read = 0;
                    while ((read = in.read(buffer)) > 0) {
                        out.write(buffer, 0, read);
                    }
                    in.close();
                    bytes = out.toByteArray();
                }
            } catch (Exception e) {
                Log.main.println("IO.readBytes() zip=" + theFile.getName(), e);
                bytes = null;
            }
        }
        return bytes;
    }
