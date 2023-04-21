    public boolean Save(Gauge gauge, boolean overwrite) throws IOException {
        String newFile = file;
        if (true) {
            int whereDot = file.indexOf('.', 0);
            if (whereDot == -1) {
                throw new IOException("File name has no . (dot)");
            }
            newFile = file.substring(0, whereDot) + "_edited.mp3";
        }
        FileConnection connOut = (FileConnection) Connector.open(newFile, Connector.READ_WRITE);
        if (connOut.exists() && !overwrite) {
            throw new IOException("Tags not saved because file " + newFile + " already exists. Set overwrite mode ON to overwrite existing files");
        }
        if (!connOut.exists()) {
            connOut.create();
        }
        FileConnection connIn = (FileConnection) Connector.open(file, Connector.READ_WRITE);
        OutputStream os = connOut.openOutputStream();
        InputStream is = connIn.openInputStream();
        long inSize = connIn.fileSize();
        System.out.println("complieTag started");
        compileTag();
        System.out.println("complieTag ended");
        if (header[3] < 0x03) {
            header[3] = 0x03;
        }
        os.write(header, 0, 6);
        os.write(tagSizeBuffer);
        if (extendedHeaderExists) {
            os.write(extendedHeader);
        }
        int i = 0;
        while (i < frames.size()) {
            System.out.println("Writing frame " + i);
            Frame f = (Frame) frames.elementAt(i);
            os.write(f.serialize());
            i++;
        }
        i = 0;
        while (i < padding) {
            os.write(0);
            i++;
        }
        long toRead = inSize;
        if (tagExists) {
            long skipped = is.skip(originalTagSize);
            if (skipped < originalTagSize) {
                long skippedThisTime = is.skip(originalTagSize - skipped);
                skipped = skipped + skippedThisTime;
            }
            toRead -= originalTagSize;
        }
        int read;
        long totalRead = 0;
        boolean first = true;
        int SIZE = 4096;
        do {
            byte[] buffer = new byte[SIZE];
            read = is.read(buffer, 0, SIZE);
            totalRead += read;
            int perc = (int) ((float) totalRead / (float) inSize * 100);
            System.out.println("Percentage " + perc);
            gauge.setValue(perc);
            System.out.println("Read = " + read + " " + "Buffer " + buffer.length + " " + totalRead + "/" + inSize);
            if (read != -1) {
                if (read < SIZE) {
                    System.out.println("Read < size " + read + " " + SIZE);
                    for (int j = 0; j < read; j++) {
                        os.write(buffer[j]);
                    }
                } else {
                    os.write(buffer, 0, read);
                }
            }
        } while (read != -1);
        is.close();
        os.close();
        System.out.println("Output size " + connOut.fileSize());
        connIn.delete();
        connIn.close();
        String base = file.substring(file.lastIndexOf('/') + 1);
        System.out.println("Base = " + base);
        connOut.rename(base);
        connOut.close();
        return true;
    }
