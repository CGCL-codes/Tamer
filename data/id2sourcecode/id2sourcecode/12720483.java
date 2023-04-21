    public String getVideoInfo(long id, String source) throws JaiomException {
        EncoderProgressListener encoderProgressListener = new JaiomEncoderProgressListener();
        log.debug(" source : " + source);
        log.debug(" target : " + target);
        File sourceFile = new File(source);
        String mpResult = null;
        boolean error = false;
        String errorString = "";
        Encoder encoder = new Encoder(new JaiomFFMpegLocator());
        MultimediaInfo videoInfo;
        try {
            videoInfo = encoder.getInfo(sourceFile);
            EncodingAttributes attrs = this.setEncodingAttributes(videoInfo);
            String mediaType = JaiomConstants.VIDEO_TYPE;
            String fileName = "";
            String fileNameWithoutExtension = "";
            String contentFileName = sourceFile.getName();
            String fileNameTarget = fileName;
            String fileWidth = videoInfo.getVideo().getSize().getWidth() + "";
            String fileHeight = videoInfo.getVideo().getSize().getHeight() + "";
            String fileDuration = videoInfo.getDuration() + "";
            String videoFrameRate = videoInfo.getVideo().getFrameRate() + "";
            String audioFrameRate = videoInfo.getAudio().getSamplingRate() + "";
            String videoCodec = videoInfo.getVideo().getDecoder();
            String audioCodec = videoInfo.getAudio().getDecoder();
            String audioChannel = videoInfo.getAudio().getChannels() + "";
            String audioSamplingRate = videoInfo.getAudio().getSamplingRate() + "";
            String audioBitRate = videoInfo.getAudio().getBitRate() + "";
            String videoBitRate = videoInfo.getVideo().getBitRate() + "";
            mpResult = MediaUploadUtil.createVideoMPResultString(mediaType, contentFileName, fileNameTarget, fileWidth, fileHeight, fileDuration, videoFrameRate, videoBitRate, audioFrameRate, videoCodec, audioCodec, audioChannel, audioSamplingRate, audioBitRate, "", "", "none");
        } catch (InputFormatException e) {
            log.error("InputFormatException while encoding Video : " + e);
            throw new JaiomException("InputFormatException while getting Video info: " + e);
        } catch (EncoderException e) {
            log.error("EncoderException while encoding Video : " + e);
            throw new JaiomException("EncoderException while encoding Video info: " + e);
        } catch (Exception e) {
            log.error("Exception while encoding Video  : " + e);
            throw new JaiomException("Exception while encoding Video  : " + e);
        }
        return mpResult;
    }
