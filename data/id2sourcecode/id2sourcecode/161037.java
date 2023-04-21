    public static void main(String[] args) {
        int BUFFER_SIZE = 50000;
        double a11 = -1.69065929318241;
        double a12 = 0.73248077421585;
        double b10 = 1.53512485958697;
        double b11 = -2.69169618940638;
        double b12 = 1.19839281085285;
        double a21 = -1.99004745483398;
        double a22 = 0.99007225036621;
        double b20 = 1.0;
        double b21 = -2.0;
        double b22 = 1.0;
        try {
            if (args.length != 1) {
                System.out.println("Error: no filename specified!");
                System.out.println("usage: bs1770 <filename>");
                System.exit(0);
            }
            System.out.println("Analyzing file: " + args[0]);
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(args[0]));
            AudioFormat format = stream.getFormat();
            int nChannels = format.getChannels();
            int nFrames = (int) stream.getFrameLength();
            int nFrameSize = (int) format.getFrameSize();
            int nSampleSize = (int) format.getSampleSizeInBits();
            System.out.println("Sample Rate: " + format.getSampleRate() + " Hz");
            System.out.println("Sample Size: " + nSampleSize + " bits");
            System.out.println("Channels: " + nChannels);
            System.out.println("Frame Size: " + nFrameSize + " bytes");
            System.out.println("Frame Rate: " + format.getFrameRate() + " Hz");
            System.out.println("Length: " + nFrames + " frames");
            if (nChannels > 5) {
                System.out.println("Error: BS 1770 is only defined for up to 5 channels!");
                System.exit(0);
            }
            if (format.getSampleRate() != 48000.0) {
                System.out.println("Error: This program can only process 48 kHz sampled files!");
                System.exit(0);
            }
            if (nSampleSize != 16) {
                System.out.println("Error:  This program can only process 16 bit sampled files!");
                System.exit(0);
            }
            if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                System.out.println("Error:  This program can only process PCM_SIGNED files!");
                System.exit(0);
            }
            byte[] audioBytes = new byte[BUFFER_SIZE];
            double[] mem11 = new double[nChannels];
            double[] mem12 = new double[nChannels];
            double[] mem13 = new double[nChannels];
            double[] mem21 = new double[nChannels];
            double[] mem22 = new double[nChannels];
            double[] mem23 = new double[nChannels];
            double[] sum = new double[nChannels];
            int nStepBytes = nFrameSize;
            double sampleScaling = Math.pow(2, nSampleSize - 1);
            int nSampleSizeInBytes = nSampleSize / 8;
            int numBytesRead = 0;
            while ((numBytesRead = stream.read(audioBytes)) != -1) {
                for (int i = 0; i < (numBytesRead / nFrameSize); ++i) for (int k = 0; k < nChannels; ++k) {
                    int offset = i * nFrameSize + k * nSampleSizeInBytes;
                    double data = (double) (audioBytes[offset + 1] * 256) + (double) (audioBytes[offset] & 0xFF);
                    data /= sampleScaling;
                    mem13[k] = mem12[k];
                    mem12[k] = mem11[k];
                    mem11[k] = data - (mem12[k] * a11) - (mem13[k] * a12);
                    double temp = (mem11[k] * b10) + (mem12[k] * b11) + (mem13[k] * b12);
                    mem23[k] = mem22[k];
                    mem22[k] = mem21[k];
                    mem21[k] = temp - (mem22[k] * a21) - (mem23[k] * a22);
                    double sTemp = (mem21[k] * b20) + (mem22[k] * b21) + (mem23[k] * b22);
                    sum[k] = sum[k] + (sTemp * sTemp);
                }
            }
            stream.close();
            System.out.println();
            double totalLoudness = 0.0;
            for (int k = 0; k < nChannels; ++k) {
                double partialLoudness = -0.691 + 10 * Math.log10(sum[k] / nFrames);
                System.out.format("Mono Loudness ch:%d = %.2f dB\n", k + 1, partialLoudness);
                if (k < 3) totalLoudness += sum[k] / nFrames; else totalLoudness += 1.41 * (sum[k] / nFrames);
            }
            totalLoudness = -0.691 + 10 * Math.log10(totalLoudness);
            System.out.println();
            System.out.format("BS 1770 Total Loudness = %.2f dB\n", totalLoudness);
            System.out.println("(assumes channels 1,2,3,4,5 are L,R,C,Ls,Rs)");
        } catch (IOException e) {
        } catch (UnsupportedAudioFileException e) {
        }
    }
