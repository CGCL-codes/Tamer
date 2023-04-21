    private String decodeAtrac(byte[] atracData, int address, int atracFileSize, String decodedFileName) {
        try {
            ByteBuffer riffBuffer = ByteBuffer.wrap(atracData);
            if (dumpEncodedFile) {
                new File(AtracCodec.getBaseDirectory()).mkdirs();
                FileOutputStream encodedOut = new FileOutputStream(getAtracAudioPath(address, atracFileSize, "encoded"));
                encodedOut.getChannel().write(riffBuffer);
                encodedOut.close();
                riffBuffer.rewind();
            }
            ByteBuffer omaBuffer = OMAFormat.convertRIFFtoOMA(riffBuffer);
            if (omaBuffer == null) {
                Modules.log.info("AT3+ data could not be decoded by the external decoder (error while converting to OMA)");
                return null;
            }
            new File(AtracCodec.getBaseDirectory()).mkdirs();
            String encodedFileName = getAtracAudioPath(address, atracFileSize, "oma");
            FileOutputStream os = new FileOutputStream(encodedFileName);
            os.getChannel().write(omaBuffer);
            os.close();
            if (!executeExternalDecoder(encodedFileName, decodedFileName, keepOmaFile)) {
                int channels = OMAFormat.getOMANumberAudioChannels(omaBuffer);
                if (channels == 1) {
                    Modules.log.info("Mono AT3+ data could not be decoded by the external decoder");
                } else if (channels == 2) {
                    Modules.log.info("Stereo AT3+ data could not be decoded by the external decoder");
                } else {
                    Modules.log.info("AT3+ data could not be decoded by the external decoder (channels=" + channels + ")");
                }
                return null;
            }
        } catch (IOException e) {
            log.error(e);
        }
        return decodedFileName;
    }
