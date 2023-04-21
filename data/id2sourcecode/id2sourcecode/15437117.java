    public void stepVideo() {
        if (container.readNextPacket(packet) >= 0) {
            if (packet.getStreamIndex() == videoStreamId && videoCoder != null) {
                IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(), videoCoder.getWidth(), videoCoder.getHeight());
                int offset = 0;
                while (offset < packet.getSize()) {
                    int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
                    if (bytesDecoded < 0) {
                        throw new RuntimeException("got error decoding video in: " + fileName);
                    }
                    offset += bytesDecoded;
                    if (picture.isComplete()) {
                        IVideoPicture newPic = picture;
                        if (resampler != null) {
                            newPic = IVideoPicture.make(resampler.getOutputPixelFormat(), picture.getWidth(), picture.getHeight());
                            if (resampler.resample(newPic, picture) < 0) {
                                throw new RuntimeException("could not resample video from: " + fileName);
                            }
                        }
                        if (newPic.getPixelType() != IPixelFormat.Type.BGR24) {
                            throw new RuntimeException("could not decode video BGR 24 bit data in: " + fileName);
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
                            sleep(millisecondsToSleep);
                        }
                        image = converter.toImage(newPic);
                    }
                }
            } else if (packet.getStreamIndex() == audioStreamId && audioCoder != null) {
                IAudioSamples samples = IAudioSamples.make(1024, audioCoder.getChannels());
                int offset = 0;
                while (offset < packet.getSize()) {
                    int bytesDecoded = audioCoder.decodeAudio(samples, packet, offset);
                    if (bytesDecoded < 0) {
                        throw new RuntimeException("got error decoding audio in: " + fileName);
                    }
                    offset += bytesDecoded;
                    if (samples.isComplete()) {
                        playAudio(samples);
                    }
                }
            }
        } else {
            endOfVideo = true;
        }
    }
