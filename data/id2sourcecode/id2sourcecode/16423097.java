    private void checkBuffers(final long bin, int nbins, final boolean reading) throws FileNotFoundException, IOException {
        int bufs = bufferSize;
        if (nbins > bufs) throw new IOException("Trying to read/write more bins than the buffer size!");
        if (fb == null || raStream == null) {
            raStream = new RandomAccessFile(this.timFile, "rw");
            fb = null;
            bufs = (int) (RecipeParser.getMem(bufs * 4) / 4);
            ByteBuffer bb = null;
            try {
                bb = raStream.getChannel().map(MapMode.READ_WRITE, ((long) header.getHeaderLength() + bin * 4), bufs * 4);
            } catch (IOException ex) {
                bufferSize /= 2.0;
                ex.printStackTrace();
                PulsarHunter.out.println("Reduced Buffer Size... " + bufferSize);
                checkBuffers(bin, nbins, reading);
                return;
            }
            bb.order(ByteOrder.nativeOrder());
            fb = bb.asFloatBuffer();
            currentFilePos = bin;
        }
        if (bin + nbins >= currentFilePos + fb.capacity()) {
            int addj = 0;
            if (reading && header.getHeaderLength() + bin * 4 + this.bufferSize * 4 > this.timFile.length()) {
                addj = (int) ((long) (header.getHeaderLength() + bin * 4 + this.bufferSize * 4) - this.timFile.length());
            }
            fb = null;
            bufs = (int) (RecipeParser.getMem(bufs * 4) / 4);
            ByteBuffer bb = null;
            try {
                bb = raStream.getChannel().map(MapMode.READ_WRITE, header.getHeaderLength() + bin * 4 - addj, bufs * 4);
            } catch (IOException ex) {
                bufferSize /= 2.0;
                ex.printStackTrace();
                PulsarHunter.out.println("Reduced Buffer Size... " + bufferSize);
                checkBuffers(bin, nbins, reading);
                return;
            }
            bb.order(ByteOrder.nativeOrder());
            bb.position(addj);
            fb = bb.asFloatBuffer();
            currentFilePos = bin;
        }
        if (nbins > 1 && bin == currentFilePos + fb.position()) return;
        if (bin > currentFilePos + fb.position()) {
            while (bin != currentFilePos + fb.position()) {
                fb.get();
            }
        }
        if (bin < fb.position()) {
            raStream.close();
            raStream = new RandomAccessFile(this.timFile, "rw");
            int addj = 0;
            if (header.getHeaderLength() + bin * 4 + this.bufferSize * 4 > this.timFile.length()) {
                addj = (int) ((long) (header.getHeaderLength() + bin * 4 + bufs * 4) - this.timFile.length());
            }
            ByteBuffer bb = null;
            try {
                bb = raStream.getChannel().map(MapMode.READ_WRITE, header.getHeaderLength() + bin * 4 - addj, this.bufferSize * 4);
            } catch (IOException ex) {
                bufferSize /= 2.0;
                PulsarHunter.out.println("Reduced Buffer Size... " + bufferSize);
                checkBuffers(bin, nbins, reading);
                return;
            }
            bb.order(ByteOrder.nativeOrder());
            fb = bb.asFloatBuffer();
            currentFilePos = bin;
        }
    }
