    public WOComponent migrate() {
        FileOutputStream adaptorConfigFileOutputStream = null;
        try {
            adaptorConfigFileOutputStream = new FileOutputStream(new File("/tmp/http-webobjects.conf"));
            FileChannel fc = adaptorConfigFileOutputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(adaptorConfigContent().length());
            buffer.put(adaptorConfigContent.getBytes());
            buffer.flip();
            while (buffer.hasRemaining()) {
                fc.write(buffer);
            }
            if (shouldRestartApache.booleanValue()) {
            }
            migrationStackTrace = "";
        } catch (Exception e) {
            migrationStackTrace = e.getMessage();
        } finally {
            try {
                if (adaptorConfigFileOutputStream != null) {
                    adaptorConfigFileOutputStream.close();
                }
            } catch (Exception e) {
                migrationStackTrace = migrationStackTrace + "\n" + e.getMessage();
            }
        }
        return null;
    }
