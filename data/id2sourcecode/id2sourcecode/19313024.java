    private boolean playbackWav(String fileName) throws IOException {
        InputStream raf = null;
        if (fileName.startsWith("res:")) {
            raf = Music.class.getResourceAsStream(fileName.substring(4));
        } else {
            raf = new FileInputStream(root + "/" + fileName);
        }
        if (raf != null) {
            try {
                long startOffset = findData(raf);
                raf.close();
                byte[] buffer = new byte[16384];
                initWave();
                sdl.start();
                while (checkStop()) {
                    if (fileName.startsWith("res:")) {
                        raf = Music.class.getResourceAsStream(fileName.substring(4));
                    } else {
                        raf = new FileInputStream(root + "/" + fileName);
                    }
                    IOUtils.skipFully(raf, startOffset);
                    int read = 0;
                    do {
                        read = raf.read(buffer);
                        if (read > 0) {
                            sdl.write(buffer, 0, read);
                        }
                    } while (checkStop() && read >= 0);
                }
            } finally {
                raf.close();
            }
            return true;
        }
        return false;
    }
