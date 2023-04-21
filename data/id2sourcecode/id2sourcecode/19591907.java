    public static javax.sound.sampled.AudioFormat convertFormat(AudioFormat format) {
        String encodingString = format.getEncoding();
        int channels = format.getChannels();
        double frameRate = format.getFrameRate();
        int frameSize = format.getFrameSizeInBits() / 8;
        double sampleRate = format.getSampleRate();
        int sampleSize = format.getSampleSizeInBits();
        boolean endian = (format.getEndian() == AudioFormat.BIG_ENDIAN);
        int signed = format.getSigned();
        Encoding encoding;
        if (AudioFormat.LINEAR.equals(encodingString)) {
            switch(signed) {
                case AudioFormat.SIGNED:
                    encoding = Encoding.PCM_SIGNED;
                    break;
                case AudioFormat.UNSIGNED:
                    encoding = Encoding.PCM_UNSIGNED;
                    break;
                default:
                    throw new IllegalArgumentException("Signed/Unsigned must be specified");
            }
        } else if (AudioFormat.ALAW.equals(encodingString)) {
            encoding = Encoding.ALAW;
        } else if (AudioFormat.ULAW.equals(encodingString)) {
            encoding = Encoding.ULAW;
        } else if (toMpegEncoding(encodingString) != null) {
            encoding = toMpegEncoding(encodingString);
        } else if (toVorbisEncoding(encodingString) != null) {
            encoding = toVorbisEncoding(encodingString);
        } else {
            encoding = new CustomEncoding(encodingString);
        }
        javax.sound.sampled.AudioFormat sampledFormat;
        Class classMpegEncoding = null;
        Class classVorbisEncoding = null;
        if (!JavaSoundUtils.onlyStandardFormats) {
            try {
                classMpegEncoding = Class.forName("javazoom.spi.mpeg.sampled.file.MpegEncoding");
                classVorbisEncoding = Class.forName("javazoom.spi.vorbis.sampled.file.VorbisEncoding");
            } catch (Exception dontcare) {
            }
        }
        if (encoding == Encoding.PCM_SIGNED) {
            sampledFormat = new javax.sound.sampled.AudioFormat((float) sampleRate, sampleSize, channels, true, endian);
        } else if (encoding == Encoding.PCM_UNSIGNED) {
            sampledFormat = new javax.sound.sampled.AudioFormat((float) sampleRate, sampleSize, channels, false, endian);
        } else if ((null != classMpegEncoding) && classMpegEncoding.isInstance(encoding)) {
            try {
                Class classMpegAudioFormat = Class.forName("javazoom.spi.mpeg.sampled.file.MpegAudioFormat");
                Class partypes[] = new Class[8];
                partypes[0] = javax.sound.sampled.AudioFormat.Encoding.class;
                partypes[1] = Float.TYPE;
                partypes[2] = Integer.TYPE;
                partypes[3] = Integer.TYPE;
                partypes[4] = Integer.TYPE;
                partypes[5] = Float.TYPE;
                partypes[6] = Boolean.TYPE;
                partypes[7] = java.util.Map.class;
                Constructor ct = classMpegAudioFormat.getConstructor(partypes);
                Object arglist[] = new Object[8];
                arglist[0] = encoding;
                arglist[1] = (float) sampleRate;
                arglist[2] = sampleSize;
                arglist[3] = channels;
                arglist[4] = frameSize;
                arglist[5] = (float) frameRate;
                arglist[6] = endian;
                arglist[7] = new HashMap();
                sampledFormat = (javax.sound.sampled.AudioFormat) ct.newInstance(arglist);
            } catch (Exception dontcare) {
                sampledFormat = null;
            }
        } else if ((null != classVorbisEncoding) && classVorbisEncoding.isInstance(encoding)) {
            try {
                Class classVorbisAudioFormat = Class.forName("javazoom.spi.vorbis.sampled.file.VorbisAudioFormat");
                Class partypes[] = new Class[8];
                partypes[0] = javax.sound.sampled.AudioFormat.Encoding.class;
                partypes[1] = Float.TYPE;
                partypes[2] = Integer.TYPE;
                partypes[3] = Integer.TYPE;
                partypes[4] = Integer.TYPE;
                partypes[5] = Float.TYPE;
                partypes[6] = Boolean.TYPE;
                partypes[7] = java.util.Map.class;
                Constructor ct = classVorbisAudioFormat.getConstructor(partypes);
                Object arglist[] = new Object[8];
                arglist[0] = encoding;
                arglist[1] = (float) sampleRate;
                arglist[2] = sampleSize;
                arglist[3] = channels;
                arglist[4] = frameSize;
                arglist[5] = (float) frameRate;
                arglist[6] = endian;
                arglist[7] = new HashMap();
                sampledFormat = (javax.sound.sampled.AudioFormat) ct.newInstance(arglist);
            } catch (Exception dontcare) {
                sampledFormat = null;
            }
        } else {
            sampledFormat = new javax.sound.sampled.AudioFormat(encoding, (float) sampleRate, sampleSize, channels, frameSize, (float) frameRate, endian);
        }
        return sampledFormat;
    }
