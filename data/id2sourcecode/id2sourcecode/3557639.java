    public void paintSamples(Graphics2D g2d, Color color, float colorGamma) {
        try {
            int cnt = 0;
            long t0 = System.currentTimeMillis();
            reloadCache();
            AChannel ch = getChannelModel();
            AClip clip = (AClip) ch.getParent().getParent();
            int sampleWidth = 1 << (clip.getSampleWidth() - 1);
            float samplerate = clip.getSampleRate();
            int fftLengthHalf = fftLength / 2;
            float samplerateHalf = samplerate / 2;
            final int gResolution = 2;
            int dgx = Math.max(gResolution, sampleToGraphX(fftLength) - sampleToGraphX(0));
            int dgy = Math.max(gResolution, sampleToGraphY(samplerate / fftLength) - sampleToGraphY(0));
            int rx = rectangle.width / dgx;
            int ry = rectangle.height / dgy;
            g2d.setClip(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            g2d.setColor(color);
            if (gy == null || gy.length != ry) {
                gy = new int[ry];
            }
            if (sy == null || sy.length != ry) {
                sy = new int[ry];
            }
            for (int j = 0; j < ry; j++) {
                gy[j] = rectangle.y + j * dgy;
                sy[j] = (int) graphToSampleY(gy[j]);
            }
            for (int i = 0; i < rx; i++) {
                int gx = rectangle.x + i * dgx;
                int sx = (int) graphToSampleX(gx);
                if (sx < 0) {
                    continue;
                }
                if (sx > ch.getSampleLength()) {
                    continue;
                }
                for (int j = 0; j < ry; j++) {
                    int syj = sy[j];
                    int gyj = gy[j];
                    if (syj < 0) {
                        break;
                    }
                    if (syj > samplerateHalf) {
                        continue;
                    }
                    float mag = cacheMag.get((int) (sx / fftLength * fftLengthHalf + syj * fftLengthHalf / samplerateHalf));
                    mag = (mag - magMin) / magRange;
                    float g = (float) Math.exp(Math.log(mag) * colorGamma);
                    float s = (float) (1.0 / (1.0 + Math.exp((magMean * colorGamma - mag) * 1000 / colorGamma)));
                    s = s * g;
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, s));
                    g2d.fillRect(gx, gyj, dgx, dgy);
                }
            }
            System.out.println("paint spectrogram time = " + (System.currentTimeMillis() - t0) + "ms cnt=" + cnt + " fftLength=" + fftLength);
        } catch (Exception e) {
            Debug.printStackTrace(5, e);
        }
    }
