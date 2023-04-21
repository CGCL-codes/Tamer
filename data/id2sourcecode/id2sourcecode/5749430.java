    private static void initialHandshake(ObjectInputStream dataIn, ObjectOutputStream dataOut) throws IOException, ClassNotFoundException {
        final long start = System.currentTimeMillis();
        dataOut.writeUnshared(ClientThread.HANDSHAKE_CLIENT);
        dataOut.writeInt(VersionUtils.getMajor());
        dataOut.writeInt(VersionUtils.getMinor());
        dataOut.writeLong(start);
        dataOut.flush();
        Object o = dataIn.readUnshared();
        if (!ClientThread.HANDSHAKE_SERVER.equals(o)) {
            throw new IOException("The server didn't send the correct hand-shake:" + o);
        }
        int major = dataIn.readInt();
        int minor = dataIn.readInt();
        long time = dataIn.readLong();
        logger.info("The server runs " + major + '.' + minor + " with a time diff of " + (start - time) / 1000 + "s. RTT is " + (System.currentTimeMillis() - start) + "ms");
        if (major < VersionUtils.getMajor()) {
            throw new IOException("The server is older than this client. Strange.");
        } else {
            final String myVersionString = VersionUtils.getMajor() + "." + VersionUtils.getMinor();
            final String serverVersionString = major + "." + minor;
            if (major > VersionUtils.getMajor()) {
                ProgressUtils.errorMessage(lang.getString("TooOld"), myVersionString, serverVersionString);
                System.exit(1);
            } else {
                if (minor > VersionUtils.getMinor()) {
                    ProgressUtils.infoMessage(lang.getString("MayUpdate"), myVersionString, serverVersionString);
                }
            }
        }
    }
