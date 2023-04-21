        synchronized boolean buildThumbNail() {
            if (rect.getWidth() == 0) return true;
            thumbNailImage = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_BYTE_BINARY);
            gg = (Graphics2D) thumbNailImage.getGraphics();
            long nSamp = gin.getLengthInFrames();
            frameToScreen = rect.width / (double) nSamp;
            try {
                gin.seekFrame(0, false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            int nChannel = gin.getFormat().getChannels();
            long nFrame = gin.getLengthInFrames();
            int chunkSize = 1024;
            int nRead = 0;
            float valMax = 0;
            float valMin = 0;
            double scale = rect.height / 2.0;
            gg.setColor(Color.white);
            int midY = rect.height / 2;
            int pix = 0;
            int ii = 0;
            AudioBuffer buff = new AudioBuffer("WaveThumbnail", nChannel, chunkSize, FrinikaConfig.sampleRate);
            System.out.println(" chunkSize,nFrame =" + chunkSize + " " + nFrame);
            while (nRead < nFrame) {
                if (runThread.isInterrupted()) {
                    System.out.println("Interupted .....");
                    return false;
                }
                int nn = chunkSize;
                buff.makeSilence();
                gin.processAudio(buff);
                nRead += chunkSize;
                float sampleLA[] = buff.getChannel(0);
                float sampleRA[];
                if (nChannel == 2) sampleRA = buff.getChannel(1); else sampleRA = buff.getChannel(0);
                assert (buff.getSampleCount() == chunkSize);
                for (int i = 0; i < chunkSize; i++, ii++) {
                    float sampleL = sampleLA[i];
                    float sampleR = sampleRA[i];
                    valMin = Math.min(Math.min(valMin, sampleL), sampleR);
                    valMax = Math.max(Math.max(valMax, sampleL), sampleR);
                    int pixNow = (int) (ii * frameToScreen);
                    if (pixNow > pix) {
                        gg.drawLine(pix, (int) (midY + valMin * scale), pix, (int) (midY + valMax * scale));
                        pix = pixNow;
                        valMax = valMin = 0;
                    }
                }
            }
            setChanged();
            notifyObservers();
            System.out.println(" BUILD DONE" + rect);
            return true;
        }
