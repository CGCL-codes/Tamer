    public void encode(File source, File target, EncodingAttributes attributes, EncoderProgressListener listener) throws IllegalArgumentException, InputFormatException, EncoderException {
        String formatAttribute = attributes.getFormat();
        Float offsetAttribute = attributes.getOffset();
        Float durationAttribute = attributes.getDuration();
        AudioAttributes audioAttributes = attributes.getAudioAttributes();
        VideoAttributes videoAttributes = attributes.getVideoAttributes();
        if (audioAttributes == null && videoAttributes == null) {
            throw new IllegalArgumentException("Both audio and video attributes are null");
        }
        target = target.getAbsoluteFile();
        target.getParentFile().mkdirs();
        FFMPEGExecutor ffmpeg = locator.createExecutor();
        if (offsetAttribute != null) {
            ffmpeg.addArgument("-ss");
            ffmpeg.addArgument(String.valueOf(offsetAttribute.floatValue()));
        }
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(source.getAbsolutePath());
        if (durationAttribute != null) {
            ffmpeg.addArgument("-t");
            ffmpeg.addArgument(String.valueOf(durationAttribute.floatValue()));
        }
        if (videoAttributes == null) {
            ffmpeg.addArgument("-vn");
        } else {
            String codec = videoAttributes.getCodec();
            if (codec != null) {
                ffmpeg.addArgument("-vcodec");
                ffmpeg.addArgument(codec);
            }
            String tag = videoAttributes.getTag();
            if (tag != null) {
                ffmpeg.addArgument("-vtag");
                ffmpeg.addArgument(tag);
            }
            Integer bitRate = videoAttributes.getBitRate();
            if (bitRate != null) {
                ffmpeg.addArgument("-b");
                ffmpeg.addArgument(String.valueOf(bitRate.intValue()));
            }
            Integer frameRate = videoAttributes.getFrameRate();
            if (frameRate != null) {
                ffmpeg.addArgument("-r");
                ffmpeg.addArgument(String.valueOf(frameRate.intValue()));
            }
            VideoSize size = videoAttributes.getSize();
            if (size != null) {
                ffmpeg.addArgument("-s");
                ffmpeg.addArgument(String.valueOf(size.getWidth()) + "x" + String.valueOf(size.getHeight()));
            }
        }
        if (audioAttributes == null) {
            ffmpeg.addArgument("-an");
        } else {
            String codec = audioAttributes.getCodec();
            if (codec != null) {
                ffmpeg.addArgument("-acodec");
                ffmpeg.addArgument(codec);
            }
            Integer bitRate = audioAttributes.getBitRate();
            if (bitRate != null) {
                ffmpeg.addArgument("-ab");
                ffmpeg.addArgument(String.valueOf(bitRate.intValue()));
            }
            Integer channels = audioAttributes.getChannels();
            if (channels != null) {
                ffmpeg.addArgument("-ac");
                ffmpeg.addArgument(String.valueOf(channels.intValue()));
            }
            Integer samplingRate = audioAttributes.getSamplingRate();
            if (samplingRate != null) {
                ffmpeg.addArgument("-ar");
                ffmpeg.addArgument(String.valueOf(samplingRate.intValue()));
            }
            Integer volume = audioAttributes.getVolume();
            if (volume != null) {
                ffmpeg.addArgument("-vol");
                ffmpeg.addArgument(String.valueOf(volume.intValue()));
            }
        }
        ffmpeg.addArgument("-f");
        ffmpeg.addArgument(formatAttribute);
        ffmpeg.addArgument("-y");
        ffmpeg.addArgument(target.getAbsolutePath());
        try {
            ffmpeg.execute();
        } catch (IOException e) {
            throw new EncoderException(e);
        }
        try {
            String lastWarning = null;
            long duration;
            long progress = 0;
            RBufferedReader reader = null;
            reader = new RBufferedReader(new InputStreamReader(ffmpeg.getErrorStream()));
            MultimediaInfo info = parseMultimediaInfo(source, reader);
            if (durationAttribute != null) {
                duration = (long) Math.round((durationAttribute.floatValue() * 1000L));
            } else {
                duration = info.getDuration();
                if (offsetAttribute != null) {
                    duration -= (long) Math.round((offsetAttribute.floatValue() * 1000L));
                }
            }
            if (listener != null) {
                listener.sourceInfo(info);
            }
            int step = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (step == 0) {
                    if (line.startsWith("WARNING: ")) {
                        if (listener != null) {
                            listener.message(line);
                        }
                    } else if (!line.startsWith("Output #0")) {
                        throw new EncoderException(line);
                    } else {
                        step++;
                    }
                } else if (step == 1) {
                    if (!line.startsWith("  ")) {
                        step++;
                    }
                }
                if (step == 2) {
                    if (!line.startsWith("Stream mapping:")) {
                        throw new EncoderException(line);
                    } else {
                        step++;
                    }
                } else if (step == 3) {
                    if (!line.startsWith("  ")) {
                        step++;
                    }
                }
                if (step == 4) {
                    line = line.trim();
                    if (line.length() > 0) {
                        Hashtable table = parseProgressInfoLine(line);
                        if (table == null) {
                            if (listener != null) {
                                listener.message(line);
                            }
                            lastWarning = line;
                        } else {
                            if (listener != null) {
                                String time = (String) table.get("time");
                                if (time != null) {
                                    int dot = time.indexOf('.');
                                    if (dot > 0 && dot == time.length() - 2 && duration > 0) {
                                        String p1 = time.substring(0, dot);
                                        String p2 = time.substring(dot + 1);
                                        try {
                                            long i1 = Long.parseLong(p1);
                                            long i2 = Long.parseLong(p2);
                                            progress = (i1 * 1000L) + (i2 * 100L);
                                            int perm = (int) Math.round((double) (progress * 1000L) / (double) duration);
                                            if (perm > 1000) {
                                                perm = 1000;
                                            }
                                            listener.progress(perm);
                                        } catch (NumberFormatException e) {
                                            ;
                                        }
                                    }
                                }
                            }
                            lastWarning = null;
                        }
                    }
                }
            }
            if (lastWarning != null) {
                if (!SUCCESS_PATTERN.matcher(lastWarning).matches()) {
                    throw new EncoderException(lastWarning);
                }
            }
        } catch (IOException e) {
            throw new EncoderException(e);
        } finally {
            ffmpeg.destroy();
        }
    }
