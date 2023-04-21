    public String encodeVideo(long id, String source, String target) throws JaiomException {
        EncoderProgressListener encoderProgressListener = new JaiomEncoderProgressListener();
        log.debug(" source : " + source);
        log.debug(" target : " + target);
        File sourceFile = new File(source);
        File targetFile = new File(target);
        Encoder encoder = new Encoder(new JaiomFFMpegLocator());
        String mpResult = null;
        try {
            MultimediaInfo videoInfo = encoder.getInfo(sourceFile);
            EncodingAttributes attrs = this.setEncodingAttributes(videoInfo);
            String mediaType = JaiomConstants.VIDEO_TYPE;
            String fileName = targetFile.getName();
            String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf(".") - 1);
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
            encoder.encode(sourceFile, targetFile, attrs, encoderProgressListener);
            mpResult = MediaUploadUtil.createVideoMPResultString(mediaType, contentFileName, fileNameTarget, fileWidth, fileHeight, fileDuration, videoFrameRate, videoBitRate, audioFrameRate, videoCodec, audioCodec, audioChannel, audioSamplingRate, audioBitRate, "", "", "none");
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException while encoding Video : " + e);
            throw new JaiomException("IllegalArgumentException while encoding Video : " + e);
        } catch (InputFormatException e) {
            log.error("InputFormatException while encoding Video : " + e);
            throw new JaiomException("InputFormatException while encoding Video : " + e);
        } catch (EncoderException e) {
            log.error("EncoderException while encoding Video : " + e);
            throw new JaiomException("EncoderException while encoding Video : " + e);
        }
        return mpResult;
    }
