    private void getMovieMediaInfos(Movie mf) {
        if (UMCConstants.debug) log.debug("BW" + identifier + ": Retreiving Media infos from movie file...");
        if (new File(System.getProperty("user.dir") + "/tools/").exists()) {
            Process p = null;
            try {
                String mediaPath = mf.getPCPath();
                log.info("=======>" + mediaPath);
                if (StringUtils.isNotEmpty(mediaPath) && !mediaPath.startsWith("\\\\")) mediaPath = mediaPath.replaceAll("//", "/").replaceAll("\\\\\\\\", "\\");
                log.info("File: " + mediaPath + " for " + mf.getMetaInfoSearchValue());
                if (mediaPath == null || mediaPath.equals("")) {
                    log.warn("Path to movie file not valid -> " + mediaPath + ". MediaInfo could not retreive the media information and will be aborted.");
                    return;
                }
                String[] playWin = { System.getProperty("user.dir") + "/tools/win/MediaInfo.exe", "--Inform=file://mediainfo_template", mediaPath };
                String[] playMac = { "./mediainfo", "--Inform=file://mediainfo_template", mediaPath };
                String[] playLinux = { Publisher.getInstance().getParamMediainfo(), "--Inform=file://mediainfo_template", mediaPath };
                ProcessBuilder pc = null;
                if (UMCConstants.os.toUpperCase().indexOf("WIN") != -1) {
                    pc = new ProcessBuilder(playWin);
                    pc.directory(new File(System.getProperty("user.dir") + "/tools/win/"));
                } else if (UMCConstants.os.toUpperCase().indexOf("MAC OS") != -1) {
                    pc = new ProcessBuilder(playMac);
                    pc.directory(new File(System.getProperty("user.dir") + "/tools/mac/"));
                } else {
                    pc = new ProcessBuilder(playLinux);
                    pc.directory(new File(System.getProperty("user.dir") + "/tools/linux/"));
                }
                p = pc.start();
                InputStream is = p.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                Properties props = new Properties();
                props.load(isr);
                if (UMCConstants.debug) {
                    log.debug("fileSize -> " + props.getProperty("fileSize", "").toString());
                    log.debug("videoCodec -> " + props.getProperty("videoCodec", "").toString());
                    log.debug("videoBitrate -> " + props.getProperty("videoBitrate", "").toString());
                    log.debug("videoWidth -> " + props.getProperty("videoWidth", "").toString());
                    log.debug("videoHeight -> " + props.getProperty("videoHeight", "").toString());
                    log.debug("videoFrameRate -> " + props.getProperty("videoFrameRate", "").toString());
                    log.debug("durationHHmm -> " + props.getProperty("durationHHmm", "").toString());
                    log.debug("videoAR -> " + props.getProperty("videoAR", "").toString());
                    log.debug("audioCount -> " + props.getProperty("audioCount", "").toString());
                }
                if (mf.getFilesize() == null || mf.getFilesize().equals("")) {
                    mf.setFilesize(props.getProperty("fileSize", "").toString().replaceAll("GiB", "GB").replaceAll("MiB", "MB"));
                }
                if (mf.getCodec() == null || mf.getCodec().equals("")) mf.setCodec(props.getProperty("videoCodec", "").toString());
                if (mf.getBitrate() == null || mf.getBitrate().equals("")) mf.setBitrate(props.getProperty("videoBitrate", "").toString());
                if (mf.getWidth() == null || mf.getWidth().equals("")) {
                    mf.setWidth(props.getProperty("videoWidth", ""));
                }
                if (mf.getWidth() != null && !mf.getWidth().equals("") && Integer.parseInt(mf.getWidth().replaceAll("[a-zA-Z ]", "")) >= 1280) {
                    mf.addGenre("HD");
                    mf.setHD(true);
                } else {
                    mf.setSD(true);
                }
                if (mf.getHeight() == null || mf.getHeight().equals("")) {
                    mf.setHeight(props.getProperty("videoHeight", "").toString());
                }
                if (mf.getFramerate() == null || mf.getFramerate().equals("")) mf.setFramerate(props.getProperty("videoFrameRate", "").toString());
                if (mf.isMultipartgroup() || mf.isVideoTS() || mf.isBDMV()) {
                    long milliseconds = 0;
                    ProcessBuilder pcMP = null;
                    Process pMP = null;
                    InputStream isMP = null;
                    InputStreamReader isrMP = null;
                    Properties propsMP = null;
                    for (int x = 0; x < mf.getChilds().size(); x++) {
                        pcMP = new ProcessBuilder(pc.command());
                        pcMP.directory(pc.directory());
                        pMP = pc.start();
                        isMP = pMP.getInputStream();
                        isrMP = new InputStreamReader(isMP);
                        propsMP = new Properties();
                        propsMP.load(isrMP);
                        try {
                            milliseconds += Long.parseLong(propsMP.getProperty("durationMs", "").toString());
                        } catch (NumberFormatException e) {
                            log.info("asdfasdfasd");
                        }
                        pMP.getErrorStream().close();
                        pMP.getInputStream().close();
                        pMP.getOutputStream().close();
                        int returnCode = pMP.waitFor();
                        if (returnCode != 0) {
                            log.error("Error mediainfo return code = " + returnCode + " => " + pcMP.command());
                            if (UMCConstants.guiMode) GuiController.getInstance().getScanStatusPanel().getPanelBottom().addWarning("Error mediainfo return code = " + returnCode + " => " + pcMP.command());
                        }
                    }
                    if (mf.getDuration() == 0) mf.setDuration(milliseconds);
                    if (StringUtils.isEmpty(mf.getDurationFormatted())) {
                        int milli = (int) (milliseconds % 1000);
                        milliseconds /= 1000;
                        byte sec = (byte) (milliseconds % 60);
                        milliseconds /= 60;
                        byte min = (byte) (milliseconds % 60);
                        milliseconds /= 60;
                        byte h = (byte) (milliseconds % 24);
                        milliseconds /= 24;
                        int d = (int) milliseconds;
                        mf.setDurationFormatted(h + "h" + " " + min + "mn");
                    }
                } else {
                    if (mf.getDuration() == 0 && StringUtils.isNotEmpty(props.getProperty("durationMs", ""))) mf.setDuration(Long.parseLong(props.getProperty("durationMs", "")));
                    if (StringUtils.isEmpty(mf.getDurationFormatted())) mf.setDurationFormatted(props.getProperty("durationHHmm", "").toString());
                }
                if (mf.getAspectRatio() == null || mf.getAspectRatio().equals("")) mf.setAspectRatio(props.getProperty("videoAR", "").toString());
                if (StringUtils.isNotEmpty(mf.getWidth()) && StringUtils.isNotEmpty(mf.getHeight())) {
                    int width = Integer.parseInt(mf.getWidth());
                    int height = Integer.parseInt(mf.getHeight());
                    if (width == 1920) {
                        if (height <= 1080) mf.addIcon(UMCConstants.ICON_1080p);
                    } else if (width == 1280) {
                        if (height <= 720) mf.addIcon(UMCConstants.ICON_720p);
                    } else {
                        if (height > 540 && height <= 576) {
                            mf.addIcon(UMCConstants.ICON_576p);
                        } else if (height > 480 && height <= 540) {
                            mf.addIcon(UMCConstants.ICON_540p);
                        } else if (height <= 480) {
                            mf.addIcon(UMCConstants.ICON_480p);
                        }
                    }
                }
                if ((mf.getAudioTracks() != null || mf.getAudioTracks().size() == 0) && StringUtils.isNotEmpty(props.getProperty("audioCount"))) {
                    int audioCount = Integer.parseInt(props.getProperty("audioCount"));
                    for (int a = 0; a < audioCount; a++) {
                        AudioTrack audioTrack = new AudioTrack();
                        if (StringUtils.isNotEmpty(props.getProperty("audioChannels" + a))) {
                            audioTrack.setChannels(props.getProperty("audioChannels" + a, "").toString().replaceAll("channels", "").trim());
                        }
                        if (StringUtils.isNotEmpty(props.getProperty("audioFormat" + a))) {
                            String formatProfile = props.getProperty("formatProfile" + a, "").toString();
                            if (StringUtils.isNotEmpty(formatProfile) && (formatProfile.indexOf("TrueHD") != -1 || formatProfile.equalsIgnoreCase("HRA") || formatProfile.equalsIgnoreCase("MA"))) {
                                if (formatProfile.indexOf("TrueHD") != -1) {
                                    audioTrack.setCodec("TrueHD");
                                    mf.addIcon(UMCConstants.ICON_DOLBYTRUEHD);
                                }
                                if (formatProfile.equalsIgnoreCase("HRA") || formatProfile.equalsIgnoreCase("MA")) {
                                    audioTrack.setCodec("DTS-HD");
                                    mf.addIcon(UMCConstants.ICON_DTSMA);
                                }
                            } else {
                                audioTrack.setCodec(props.getProperty("audioFormat" + a, "").toString());
                                if (audioTrack.getCodec().equalsIgnoreCase("AC-3")) {
                                    mf.addIcon(UMCConstants.ICON_AC3);
                                } else if (audioTrack.getCodec().equalsIgnoreCase("DTS")) {
                                    if (audioTrack.getChannels() == "6") mf.addIcon(UMCConstants.ICON_DTS51); else if (audioTrack.getChannels() == "8") mf.addIcon(UMCConstants.ICON_DTS71); else mf.addIcon(UMCConstants.ICON_DTS);
                                } else if (audioTrack.getCodec().equalsIgnoreCase("MPEG Audio")) {
                                    mf.addIcon(UMCConstants.ICON_MP2);
                                } else if (audioTrack.getCodec().equalsIgnoreCase("AAC")) {
                                    mf.addIcon(UMCConstants.ICON_AAC);
                                }
                            }
                        }
                        if (StringUtils.isNotEmpty(props.getProperty("audioLng" + a))) audioTrack.setLanguage(props.getProperty("audioLng" + a, "").toString().toUpperCase());
                        if (StringUtils.isNotEmpty(props.getProperty("audioBitrate" + a))) audioTrack.setBitrate(props.getProperty("audioBitrate" + a, "").toString());
                        if (StringUtils.isNotEmpty(audioTrack.getCodec()) || StringUtils.isNotEmpty(audioTrack.getLanguage()) || StringUtils.isNotEmpty(audioTrack.getChannels()) || StringUtils.isNotEmpty(audioTrack.getBitrate())) {
                            mf.addAudioTrack(audioTrack);
                            if (UMCConstants.debug) log.debug("Audio Track " + (a + 1) + " added: " + audioTrack.getCodec() + " | " + audioTrack.getLanguage() + " | " + audioTrack.getChannels() + " | " + audioTrack.getBitrate());
                        } else {
                            if (UMCConstants.debug) log.debug("Audio Track " + (a + 1) + " not added: " + audioTrack.getCodec() + " | " + audioTrack.getLanguage() + " | " + audioTrack.getChannels() + " | " + audioTrack.getBitrate());
                        }
                    }
                }
                p.getErrorStream().close();
                p.getInputStream().close();
                p.getOutputStream().close();
                int returnCode = p.waitFor();
                if (returnCode == 0) {
                } else {
                    log.error("Error mediainfo return code = " + returnCode + " => " + pc.command());
                    mf.addWarning(new Warning(UMCConstants.STATUS_MPLAYER_ERROR, mf.getMetaInfoSearchValue() + ": Returncode -> " + returnCode));
                    if (UMCConstants.guiMode) GuiController.getInstance().getScanStatusPanel().getPanelBottom().addWarning("Error mediainfo return code = " + returnCode + " => " + pc.command());
                }
            } catch (NullPointerException exc) {
                log.error("NullPointer error", exc);
                mf.addWarning(new Warning(UMCConstants.STATUS_MPLAYER_ERROR, mf.getMetaInfoSearchValue() + ": NullPointer Exception"));
                if (UMCConstants.guiMode) GuiController.getInstance().getScanStatusPanel().getPanelBottom().addWarning("MediaInfo: " + mf.getMetaInfoSearchValue() + " (" + mf.getFilename() + mf.getFiletype() + "): NullPointer Exception");
            } catch (IndexOutOfBoundsException exc) {
                log.error("IndexOutOfBounds error", exc);
                mf.addWarning(new Warning(UMCConstants.STATUS_MPLAYER_ERROR, mf.getMetaInfoSearchValue() + ": IndexOutOfBounds Exception"));
                if (UMCConstants.guiMode) GuiController.getInstance().getScanStatusPanel().getPanelBottom().addWarning("MediaInfo: " + mf.getMetaInfoSearchValue() + " (" + mf.getFilename() + mf.getFiletype() + "): IndexOutOfBounds Exception");
            } catch (SecurityException exc) {
                log.error("Security error", exc);
                mf.addWarning(new Warning(UMCConstants.STATUS_MPLAYER_ERROR, mf.getMetaInfoSearchValue() + ": Security Exception"));
                if (UMCConstants.guiMode) GuiController.getInstance().getScanStatusPanel().getPanelBottom().addWarning("MediaInfo: " + mf.getMetaInfoSearchValue() + " (" + mf.getFilename() + mf.getFiletype() + "): Security Exception");
            } catch (IOException exc) {
                log.error("IO error", exc);
                mf.addWarning(new Warning(UMCConstants.STATUS_MPLAYER_ERROR, mf.getMetaInfoSearchValue() + ": IO Exception, check MediaInfo permissions"));
                if (UMCConstants.guiMode) GuiController.getInstance().getScanStatusPanel().getPanelBottom().addWarning("MediaInfo: " + mf.getFilename() + mf.getFiletype() + " - IO Exception, check MediaInfo permissions");
            } catch (Exception exc) {
                log.error("Allgemeiner Fehler", exc);
                mf.addWarning(new Warning(UMCConstants.STATUS_MPLAYER_ERROR, mf.getMetaInfoSearchValue() + ": Generic Exception"));
                if (UMCConstants.guiMode) GuiController.getInstance().getScanStatusPanel().getPanelBottom().addWarning("MediaInfo: " + mf.getMetaInfoSearchValue() + ": Generic Exception");
            }
        } else {
            log.error("MediaInfo directory " + System.getProperty("user.dir") + "/tools/" + " not found.");
            mf.addWarning(new Warning(UMCConstants.STATUS_MPLAYER_ERROR, "Verzeichniss " + System.getProperty("user.dir") + "/tools/" + " wurde nicht gefunden."));
            if (UMCConstants.guiMode) GuiController.getInstance().getScanStatusPanel().getPanelBottom().addWarning("MediaInfo: Tools directory " + System.getProperty("user.dir") + "/tools/" + " not found.");
        }
    }
