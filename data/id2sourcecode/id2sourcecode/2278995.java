    public AudioData(File file) {
        if (file == null) {
            data = new float[0];
            name = "Choose Wav";
        } else {
            String fileName = file.getName();
            int extensionIndex = fileName.lastIndexOf(".");
            if (extensionIndex == -1) {
                name = fileName;
            } else {
                name = fileName.substring(0, extensionIndex);
            }
            AudioInputStream inputStream = getAudioInputStream(file);
            AudioFormat inputFormat = inputStream.getFormat();
            byte[] rawData = convertAudioInputStreamToByteArray(inputStream, inputFormat);
            data = convertByteToFloat(rawData, inputFormat);
            if (inputFormat.getChannels() == 1) {
                data = convertMonoToStereo(data);
            }
            if (inputFormat.getSampleRate() == SAMPLE_RATE_22KHZ) {
                data = convert22to44(data);
            } else if (inputFormat.getSampleRate() != SAMPLE_RATE_44KHZ) {
                throw new IllegalArgumentException("Unsupported sample rate: " + inputFormat.getSampleRate());
            }
        }
    }
