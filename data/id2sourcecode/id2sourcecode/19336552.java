    public Boolean read(String zipFileName) {
        try {
            final ZipFile zipFile = new ZipFile(zipFileName);
            for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements(); ) {
                final ZipEntry zipEntry = (ZipEntry) e.nextElement();
                mLogger.debug("File name: " + zipEntry.getName() + "; size: " + zipEntry.getSize() + "; compressed size: " + zipEntry.getCompressedSize());
                final String filename = this.getFilename(zipEntry.getName());
                if (filename.endsWith("gpx") || filename.endsWith("loc")) {
                    final ByteArrayOutputStream out = new ByteArrayOutputStream();
                    final InputStream in = zipFile.getInputStream(zipEntry);
                    byte[] tempBuffer = new byte[1024];
                    int read;
                    while (-1 != (read = in.read(tempBuffer))) {
                        out.write(tempBuffer, 0, read);
                    }
                    in.close();
                    final byte[] buffer = out.toByteArray();
                    out.close();
                    if (filename.endsWith("gpx")) {
                        final GPXFileReader gpxFileReader = new GPXFileReader();
                        gpxFileReader.readByteArray(buffer);
                    } else if (filename.endsWith("loc")) {
                        final LOCFileReader locFileReader = new LOCFileReader();
                        locFileReader.readByteArray(buffer);
                    }
                } else if (filename.endsWith("txt")) {
                    final InputStream inputStream = zipFile.getInputStream(zipEntry);
                    final String targetFilename = String.format("%s%s%s", this.mBaseOutputFolder, File.separator, filename);
                    this.saveInputStreamToFile(inputStream, targetFilename);
                    inputStream.close();
                } else if (filename.endsWith("jpg") || filename.endsWith("png")) {
                    final InputStream inputStream = zipFile.getInputStream(zipEntry);
                    final String targetFilename = String.format("%s%s%s%s%s", this.mBaseOutputFolder, File.separator, "spoiler", File.separator, filename);
                    this.saveInputStreamToFile(inputStream, targetFilename);
                    inputStream.close();
                }
            }
            return true;
        } catch (IOException ioex) {
            ioex.printStackTrace();
            return false;
        }
    }
