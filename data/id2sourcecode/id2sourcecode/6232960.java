        private boolean writeLibrary(String path, String name, InputStream libraryStream) {
            try {
                externalLibaryPath = System.getProperty("java.io.tmpdir") + File.separatorChar + path + File.separatorChar + name;
                File fileOut = new File(externalLibaryPath);
                boolean alreadyExists = false;
                if (fileOut.exists()) {
                    LOGGER.log(Level.INFO, "Native library already exists, overwritting : {0}", fileOut.getAbsolutePath());
                    alreadyExists = true;
                }
                LOGGER.log(Level.INFO, "Writing native library to: {0}", fileOut.getAbsolutePath());
                try {
                    fileOut.createNewFile();
                    FileChannel libraryFileChannel = new FileOutputStream(fileOut).getChannel();
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = libraryStream.read(buf)) > 0) {
                        libraryFileChannel.write(ByteBuffer.wrap(buf, 0, len));
                    }
                    libraryFileChannel.force(true);
                    libraryFileChannel.close();
                } catch (Exception any) {
                    LOGGER.log(Level.INFO, "Exception occured while attempting to write native library", any);
                    libraryStream.close();
                    if (alreadyExists) return true;
                    return false;
                }
                libraryStream.close();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to load required system library " + name, e);
                return false;
            }
            LOGGER.fine("Finshed writing native library file");
            return true;
        }
