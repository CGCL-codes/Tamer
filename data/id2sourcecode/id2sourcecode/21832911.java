        @Override
        public void run() {
            String filename = video_file.getAbsolutePath();
            boolean decode_audio = VideoFrameProducer.this.audio_enabled;
            if (!IVideoResampler.isSupported(IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION)) throw new RuntimeException("you must install the GPL version" + " of Xuggler (with IVideoResampler support) for " + "this demo to work");
            IContainer container = IContainer.make();
            if (container.open(filename, IContainer.Type.READ, null) < 0) throw new IllegalArgumentException("could not open file: " + filename);
            int numStreams = container.getNumStreams();
            int videoStreamId = -1;
            IStreamCoder videoCoder = null;
            int audioStreamId = -1;
            IStreamCoder audioCoder = null;
            for (int i = 0; i < numStreams; i++) {
                IStream stream = container.getStream(i);
                IStreamCoder coder = stream.getStreamCoder();
                if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO && videoStreamId == -1) {
                    videoStreamId = i;
                    videoCoder = coder;
                }
                if (decode_audio && coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO && audioStreamId == -1) {
                    System.out.println("Audio id " + i);
                    audioStreamId = i;
                    audioCoder = coder;
                }
            }
            if (videoStreamId == -1) throw new RuntimeException("could not find video stream in container: " + filename);
            if (videoCoder.open() < 0) throw new RuntimeException("could not open video decoder for container: " + filename);
            if (decode_audio && audioCoder.open() < 0) throw new RuntimeException("could not open audio decoder for container: " + filename);
            if (decode_audio) openJavaSound(audioCoder);
            IConverter frameconverter = null;
            IVideoResampler resampler = null;
            if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24) {
                resampler = IVideoResampler.make(videoCoder.getWidth(), videoCoder.getHeight(), IPixelFormat.Type.BGR24, videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
                if (resampler == null) throw new RuntimeException("could not create color space " + "resampler for: " + filename);
            }
            IPacket packet = IPacket.make();
            long firstTimestampInStream = Global.NO_PTS;
            long systemClockStartTime = 0;
            while (container.readNextPacket(packet) >= 0 && keep_running) {
                if (packet.getStreamIndex() == videoStreamId) {
                    IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(), videoCoder.getWidth(), videoCoder.getHeight());
                    int offset = 0;
                    while (offset < packet.getSize() && keep_running) {
                        int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
                        if (bytesDecoded < 0) throw new RuntimeException("got error decoding video in: " + filename);
                        offset += bytesDecoded;
                        if (picture.isComplete()) {
                            IVideoPicture newPic = picture;
                            if (resampler != null) {
                                newPic = IVideoPicture.make(resampler.getOutputPixelFormat(), picture.getWidth(), picture.getHeight());
                                if (resampler.resample(newPic, picture) < 0) throw new RuntimeException("could not resample video from: " + filename);
                            }
                            if (newPic.getPixelType() != IPixelFormat.Type.BGR24) throw new RuntimeException("could not decode video" + " as BGR 24 bit data in: " + filename);
                            if (firstTimestampInStream == Global.NO_PTS) {
                                firstTimestampInStream = picture.getTimeStamp();
                                systemClockStartTime = System.currentTimeMillis();
                            } else {
                                long systemClockCurrentTime = System.currentTimeMillis();
                                long millisecondsClockTimeSinceStartofVideo = systemClockCurrentTime - systemClockStartTime;
                                long millisecondsStreamTimeSinceStartOfVideo = (picture.getTimeStamp() - firstTimestampInStream) / 1000;
                                final long millisecondsTolerance = 50;
                                final long millisecondsToSleep = (millisecondsStreamTimeSinceStartOfVideo - (millisecondsClockTimeSinceStartofVideo + millisecondsTolerance));
                                if (millisecondsToSleep > 0) {
                                    try {
                                        Thread.sleep(millisecondsToSleep);
                                    } catch (InterruptedException e) {
                                        return;
                                    }
                                }
                            }
                            if (frameconverter == null) frameconverter = ConverterFactory.createConverter(ConverterFactory.XUGGLER_BGR_24, newPic);
                            BufferedImage javaImage = frameconverter.toImage(newPic);
                            frame_q.add(javaImage);
                            frames_produced.release();
                            try {
                                frames_consumed.acquire();
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                } else if (decode_audio && packet.getStreamIndex() == audioStreamId) {
                    IAudioSamples samples = IAudioSamples.make(2048, audioCoder.getChannels());
                    int offset = 0;
                    while (offset < packet.getSize()) {
                        int bytesDecoded = audioCoder.decodeAudio(samples, packet, offset);
                        if (bytesDecoded < 0) throw new RuntimeException("got error decoding audio in: " + filename);
                        offset += bytesDecoded;
                        if (samples.isComplete()) {
                            playJavaSound(samples);
                        }
                    }
                } else {
                    do {
                    } while (false);
                }
            }
            if (videoCoder != null) {
                videoCoder.close();
                videoCoder = null;
            }
            if (audioCoder != null) {
                audioCoder.close();
                audioCoder = null;
            }
            if (container != null) {
                container.close();
                container = null;
            }
            if (decode_audio) closeJavaSound();
            System.err.println("Decoder thread terminating");
            frames_produced.release();
            keep_running = false;
        }
