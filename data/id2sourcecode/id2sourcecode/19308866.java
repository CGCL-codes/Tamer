    public void processReplacing(float[][] in, float[][] out, int sampleFrames) {
        blockSize = sampleFrames;
        if (recording) {
            try {
                for (float f : in[0]) {
                    int i = (int) (f * Integer.MAX_VALUE);
                    recorder.write(intToByteArray(i));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < sampleFrames; i++) {
            out[0][i] = 0;
            for (Button btn : buttons) {
                if ((btn.getBaseSample() != null) && (isPlaying.contains(btn)) && (!btn.isLooping()) && (btn.valuesRemaining() == 0)) {
                    stopPlaying(btn);
                }
            }
            activeButtonPerChannel.clear();
            for (Button btn : isPlaying) {
                if (!activeButtonPerChannel.containsKey(btn.getChannel())) {
                    activeButtonPerChannel.put(btn.getChannel(), btn);
                } else {
                    Button chb = activeButtonPerChannel.get(btn.getChannel());
                    if (!btn.isLooping() && chb.isLooping()) activeButtonPerChannel.put(btn.getChannel(), btn);
                }
            }
            for (Button btn : activeButtonPerChannel.values()) {
                if (btn.getBaseSample() != null) {
                    float f = btn.nextValue();
                    out[0][i] += f;
                }
            }
            out[0][i] += in[0][i];
            if (out[0][i] > 1) out[0][i] = 1; else if (out[0][i] < -1) out[0][i] = -1;
        }
    }
