    public void stepVideo() {
        if (container.readNextPacket(packet) >= 0) {
            if (packet.getStreamIndex() == videoStreamId && videoCoder != null) {
                IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(), videoCoder.getWidth(), videoCoder.getHeight());
                int offset = 0;
                while (offset < packet.getSize()) {
                    int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
                    if (bytesDecoded < 0) {
                        return;
                    }
                    offset += bytesDecoded;
                    if (picture.isComplete()) {
                        IVideoPicture newPic = picture;
                        if (resampler != null) {
                            newPic = IVideoPicture.make(resampler.getOutputPixelFormat(), screenWidth, screenHeigth);
                            if (resampler.resample(newPic, picture) < 0) {
                                return;
                            }
                        }
                        if (newPic.getPixelType() != IPixelFormat.Type.BGR24) {
                            return;
                        }
                        if (firstTimestampInStream == Global.NO_PTS) {
                            firstTimestampInStream = picture.getTimeStamp();
                            systemClockStartTime = System.currentTimeMillis();
                        } else {
                            long systemClockCurrentTime = System.currentTimeMillis();
                            long millisecondsClockTimeSinceStartofVideo = systemClockCurrentTime - systemClockStartTime;
                            long millisecondsStreamTimeSinceStartOfVideo = (picture.getTimeStamp() - firstTimestampInStream) / 1000;
                            final long millisecondsTolerance = 50;
                            final long millisecondsToSleep = (millisecondsStreamTimeSinceStartOfVideo - (millisecondsClockTimeSinceStartofVideo + millisecondsTolerance));
                            if (!seekFrameFastForward && !seekFrameRewind && !videoPaused) {
                                sleep(millisecondsToSleep);
                            }
                        }
                        if ((converter != null) && (newPic != null)) {
                            image = converter.toImage(newPic);
                        }
                    }
                }
            } else if (packet.getStreamIndex() == audioStreamId && audioCoder != null) {
                IAudioSamples samples = IAudioSamples.make(1024, audioCoder.getChannels());
                int offset = 0;
                while (offset < packet.getSize()) {
                    int bytesDecoded = audioCoder.decodeAudio(samples, packet, offset);
                    if (bytesDecoded < 0) {
                        return;
                    }
                    offset += bytesDecoded;
                    if (samples.isComplete()) {
                        playAudio(samples);
                    }
                }
            }
            if (seekFrameFastForward) {
                container.seekKeyFrame(-1, 0, IContainer.SEEK_FLAG_FRAME);
                int bitrate = container.getBitRate();
                long seconds = (packet.getTimeStamp() / 1000) + 10;
                long bytes = seconds * bitrate / 8;
                container.seekKeyFrame(videoStreamId, bytes, IContainer.SEEK_FLAG_BYTE);
            } else if (seekFrameRewind) {
                container.seekKeyFrame(-1, 0, IContainer.SEEK_FLAG_BACKWARDS);
                int bitrate = container.getBitRate();
                long seconds = (packet.getTimeStamp() / 1000) - 10;
                long bytes = seconds * bitrate / 8;
                container.seekKeyFrame(videoStreamId, bytes, IContainer.SEEK_FLAG_BYTE);
            }
        } else {
            endOfVideo = true;
        }
    }
