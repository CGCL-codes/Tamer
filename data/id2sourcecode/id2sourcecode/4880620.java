    private String getFormattedCodecInformation(String filePath) {
        if (filePath == null) {
            return null;
        } else {
            IContainer container = IContainer.make();
            int result = container.open(filePath, IContainer.Type.READ, null);
            if (result < 0) throw new RuntimeException("Failed to open media file");
            int numStreams = container.getNumStreams();
            long duration = container.getDuration();
            double fileSize = container.getFileSize();
            fileSize = fileSize / 1048576;
            fileSize = (double) Math.round(fileSize * 100) / 100;
            long bitRate = container.getBitRate();
            StringBuilder sB = new StringBuilder();
            sB.append("Number of streams: " + numStreams + "\n");
            sB.append("Duration (s): " + duration / 1000000 + "\n");
            sB.append("File Size (MB): " + fileSize + "\n");
            sB.append("Bit Rate: " + bitRate + "\n");
            sB.append("\n");
            for (int i = 0; i < numStreams; i++) {
                IStream stream = container.getStream(i);
                IStreamCoder coder = stream.getStreamCoder();
                sB.append("stream " + i + " ");
                sB.append("type: " + coder.getCodecType() + "; ");
                sB.append("duration: " + stream.getDuration() + "; ");
                sB.append("start time: " + container.getStartTime() + "; ");
                sB.append("timebase: " + stream.getTimeBase().getNumerator() + "," + " " + stream.getTimeBase().getDenominator() + "; ");
                sB.append("coder tb: " + coder.getTimeBase().getNumerator() + "," + " " + coder.getTimeBase().getDenominator() + "; ");
                sB.append("\n\t");
                if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
                    sB.append("sample rate: " + coder.getSampleRate() + "; ");
                    sB.append("channels: " + coder.getChannels() + "; ");
                    sB.append("format: " + coder.getSampleFormat() + "; ");
                } else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                    sB.append("width: " + coder.getWidth() + "; ");
                    sB.append("height: " + coder.getHeight() + "; ");
                    sB.append("format: " + coder.getPixelType() + "; ");
                    sB.append("frame-rate: " + coder.getFrameRate().getDouble() + "; ");
                }
                sB.append("\n");
            }
            return sB.toString();
        }
    }
