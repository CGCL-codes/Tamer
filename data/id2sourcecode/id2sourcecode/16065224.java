        synchronized boolean buildThumbNail() {
            thumbNailImage = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_BYTE_BINARY);
            gg = (Graphics2D) thumbNailImage.getGraphics();
            int y = rect.height / 2;
            gg.drawString("...", 0, 5);
            panel.setDirty();
            double x = getStartInSecs();
            double w = getDurationInSecs();
            try {
                thumbNailIn.seekFrame(0, false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            int nChannel = thumbNailIn.getFormat().getChannels();
            long nFrame = thumbNailIn.getLengthInFrames();
            ProjectContainer project = lane.getProject();
            double sampleToScreen = panel.userToScreen / FrinikaConfig.sampleRate;
            int chunkSize = 1024;
            AudioBuffer buff = new AudioBuffer("thumbnail", nChannel, chunkSize, 44100.0f);
            int nRead = 0;
            double valMax = 0;
            double valMin = 0;
            double scale = rect.height / 2.0;
            gg.setColor(Color.white);
            int midY = rect.height / 2;
            int pix = 0;
            int ii = 0;
            int cc = 0;
            try {
                thumbNailIn.seekTimeInMicros(envelope.tOn, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (nRead < nFrame) {
                if (runThread.isInterrupted()) {
                    return false;
                }
                int nn = chunkSize;
                if (nRead + chunkSize > nFrame) nn = (int) (nFrame - nRead);
                buff.makeSilence();
                thumbNailIn.processAudio(buff);
                nRead += nn;
                if (nChannel == 2) {
                    float left[] = buff.getChannel(0);
                    float right[] = buff.getChannel(1);
                    for (int i = 0; i < nn; i++, ii++) {
                        float sampleL = left[i];
                        float sampleR = right[i];
                        valMin = Math.min(Math.min(valMin, sampleL), sampleR);
                        valMax = Math.max(Math.max(valMax, sampleL), sampleR);
                        int pixNow = (int) (ii * sampleToScreen);
                        if (pixNow > pix) {
                            gg.drawLine(pix, (int) (midY + valMin * scale), pix, (int) (midY + valMax * scale));
                            pix = pixNow;
                            valMax = valMin = 0;
                        }
                    }
                } else {
                    float left[] = buff.getChannel(0);
                    for (int i = 0; i < nn; i++, ii++) {
                        float sampleL = left[i];
                        valMin = Math.min(valMin, sampleL);
                        valMax = Math.max(valMax, sampleL);
                        int pixNow = (int) (ii * sampleToScreen);
                        if (pixNow > pix) {
                            gg.drawLine(pix, (int) (midY + valMin * scale), pix, (int) (midY + valMax * scale));
                            pix = pixNow;
                            valMax = valMin = 0;
                        }
                    }
                }
            }
            Rectangle2D brect = gg.getFontMetrics().getStringBounds(audioFileName, gg);
            gg.setColor(Color.BLACK);
            gg.fillRect(1, 0, (int) brect.getWidth(), (int) brect.getHeight());
            gg.setColor(Color.WHITE);
            gg.drawString(audioFileName, 1, (int) brect.getHeight());
            panel.setDirty();
            panel.repaint();
            return true;
        }
