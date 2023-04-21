    public String pullSnapShots(long id, String source, int number, int offset) throws JaiomException {
        EncoderProgressListener encoderProgressListener = new JaiomEncoderProgressListener();
        Encoder encoder = new Encoder(new JaiomFFMpegLocator());
        File sourceFile = new File(source);
        String mpResult = null;
        try {
            MultimediaInfo videoInfo = encoder.getInfo(sourceFile);
            EncodingAttributes attrs = this.setEncodingAttributes(videoInfo);
            String mediaType = JaiomConstants.VIDEO_TYPE;
            String fileName = "";
            String fileNameWithoutExtension = "";
            String contentFileName = sourceFile.getName();
            String fileNameFlv = "";
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
            encoder.pullSnapShots(sourceFile, number, offset, encoderProgressListener);
            mpResult = MediaUploadUtil.createVideoMPResultString(mediaType, contentFileName, fileNameFlv, fileWidth, fileHeight, fileDuration, videoFrameRate, videoBitRate, audioFrameRate, videoCodec, audioCodec, audioChannel, audioSamplingRate, audioBitRate, number + "", offset + "", "none");
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException while grabbing Video frame: " + e);
            throw new JaiomException("IllegalArgumentException while grabbing Video frame : " + e);
        } catch (InputFormatException e) {
            log.error("InputFormatException while grabbing Video frame : " + e);
            throw new JaiomException("InputFormatException while grabbing Video frame : " + e);
        } catch (EncoderException e) {
            log.error("EncoderException while grabbing Video frame : " + e);
            throw new JaiomException("EncoderException while grabbing Video frame : " + e);
        } catch (Exception e) {
            log.error("Exception while grabbing Video frame : " + e);
            throw new JaiomException("Exception while grabbing Video frame : " + e);
        } finally {
        }
        return mpResult;
    }
