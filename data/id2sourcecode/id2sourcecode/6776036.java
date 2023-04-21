        ProcessedPacketImpl(IPacket rawPacket) {
            if (rawPacket.getStreamIndex() == videoIndex) {
                myType = Type.VIDEO;
                IVideoPicture rawPicture = IVideoPicture.make(videoCoder.getPixelType(), videoWidth, videoHeight);
                int offset = 0;
                while (offset < rawPacket.getSize()) {
                    int bytesDecoded = videoCoder.decodeVideo(rawPicture, rawPacket, offset);
                    if (bytesDecoded < 0) throw new RuntimeException("got error decoding video in: " + source);
                    offset += bytesDecoded;
                    if (rawPicture.isComplete()) {
                        pic = rawPicture;
                        if (resampler != null) {
                            pic = IVideoPicture.make(resampler.getOutputPixelFormat(), rawPicture.getWidth(), rawPicture.getHeight());
                            if (resampler.resample(pic, rawPicture) < 0) throw new RuntimeException("could not resample video from: " + source);
                        }
                        if (pic.getPixelType() != IPixelFormat.Type.BGR24) throw new RuntimeException("could not decode video" + " as BGR 24 bit data in: " + source);
                        timeStamp = pic.getTimeStamp();
                        timeStamp = rawPicture.getTimeStamp();
                        timeStamp = rawPicture.getPts();
                        if (!pic.isComplete()) logger.warn("resampled picture is not complete");
                    } else {
                        logger.warn("rawPicture is not complete");
                    }
                }
            } else if (rawPacket.getStreamIndex() == audioIndex) {
                myType = Type.AUDIO;
                samples = IAudioSamples.make(1024, audioCoder.getChannels());
                int offset = 0;
                while (offset < rawPacket.getSize()) {
                    int bytesDecoded = audioCoder.decodeAudio(samples, rawPacket, offset);
                    if (bytesDecoded < 0) throw new RuntimeException("could not decode audio");
                    offset += bytesDecoded;
                    if (samples.isComplete()) {
                        timeStamp = samples.getTimeStamp();
                    }
                }
            }
        }
