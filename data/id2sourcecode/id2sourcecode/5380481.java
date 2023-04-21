        public SampleHeader setSample(int sample, String name, long length, AudioFormat f) {
            data = new byte[26 + name.length()];
            ZUtilities.applyBytes(data, sample, 0, 3);
            data[3] = (byte) f.getSampleSizeInBits();
            data[4] = (byte) f.getChannels();
            int pns = (int) Math.round(1000000000.0 / f.getSampleRate());
            ZUtilities.applyBytes(data, pns, 5, 3);
            ZUtilities.applyBytes(data, (int) length, 8, 4);
            AudioUtilities.AudioSampleLoop asl = AudioUtilities.getFirstLoop(f, (int) length);
            ZUtilities.applyBytes(data, asl.getLoopStart(), 12, 4);
            ZUtilities.applyBytes(data, asl.getLoopEnd(), 16, 4);
            data[20] = (byte) asl.getLoopControl();
            ZUtilities.applyBytes(data, 60, 21, 2);
            ZUtilities.applyBytes(data, 0, 23, 2);
            data[25] = (byte) name.length();
            System.arraycopy(ZUtilities.applyToByteArray(name.toCharArray(), 0, name.length()), 0, data, 26, name.length());
            return this;
        }
