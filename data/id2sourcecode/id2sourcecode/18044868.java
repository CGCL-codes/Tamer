    public static int samplesIn16th(int tempo) {
        int channels = AudioData.format.getChannels();
        float frameRate = AudioData.format.getFrameRate();
        int output = (int) (15 * channels * frameRate / (float) tempo);
        output = (output / channels) * channels;
        return output;
    }
