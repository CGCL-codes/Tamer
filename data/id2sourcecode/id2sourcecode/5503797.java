    private void getMediaInfos(MovieFile mf) {
        log.info("Ermittle Medieninfos aus Datei...");
        if (new File(System.getProperty("user.dir") + "/tools/").exists()) {
            Process p = null;
            try {
                String os = System.getProperty("os.name");
                String mediaPath = mf.getScandir() + System.getProperty("file.separator") + mf.getPath() + System.getProperty("file.separator") + mf.getFilename() + mf.getFiletype();
                if (mf.isVideoTS()) mediaPath = mf.getScandir() + mf.getPathFromVIDEO_TS() + UMCConstants.fileSeparator + mf.getPath();
                log.info("Datei: " + mediaPath + " für " + mf.getTitle() + "/" + mf.getGroupname());
                if (mediaPath == null || mediaPath.equals("")) {
                    log.warn("Kein korrekter Pfad zu einer Filmdatei -> " + mediaPath + ". Das auslesen der Infos mit MediaInfo kann nicht ausgeführt werden und wird an dieser Stelle abgebrochen.");
                    return;
                }
                String[] playWin = { System.getProperty("user.dir") + "/tools/win/MediaInfo.exe", "--Inform=file://mediainfo_template", mediaPath };
                String[] playMac = { "./mediainfo", "--Inform=file://mediainfo_template", mediaPath };
                String[] playLinux = { UMCParams.getInstance().getMediainfo(), "--Inform=file://mediainfo_template", mediaPath };
                ProcessBuilder pc = null;
                if (os.toUpperCase().indexOf("WIN") != -1) {
                    pc = new ProcessBuilder(playWin);
                    pc.directory(new File(System.getProperty("user.dir") + "/tools/win/"));
                } else if (os.toUpperCase().indexOf("MAC OS") != -1) {
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
                if (debug) {
                    log.debug(props.getProperty("fileSize", "").toString());
                    log.debug(props.getProperty("videoCodec", "").toString());
                    log.debug(props.getProperty("videoBitrate", "").toString());
                    log.debug(props.getProperty("videoWidth", "").toString());
                    log.debug(props.getProperty("videoHeight", "").toString());
                    log.debug(props.getProperty("videoFrameRate", "").toString());
                    log.debug(props.getProperty("durationHHmm", "").toString());
                    log.debug(props.getProperty("videoAR", "").toString());
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
                if (mf.getLength() == null || mf.getLength().equals("")) mf.setLength(props.getProperty("durationHHmm", "").toString());
                if (mf.getAspectRatio() == null || mf.getAspectRatio().equals("")) mf.setAspectRatio(props.getProperty("videoAR", "").toString());
                Collection<AudioTrack> audioTracks = mf.getAudioTracks();
                int i = 0;
                for (AudioTrack audioTrack : audioTracks) {
                    if ((audioTrack.getCodec() == null || audioTrack.getCodec().equals("")) && props.getProperty("audioFormat" + i) != null) audioTrack.setCodec(props.getProperty("audioFormat" + i, "").toString());
                    if ((audioTrack.getLanguage() == null || audioTrack.getLanguage().equals("")) && props.getProperty("audioLng" + i) != null) audioTrack.setLanguage(props.getProperty("audioLng" + i, "").toString());
                    if ((audioTrack.getChannels() == null || audioTrack.getChannels().equals("")) && props.getProperty("audioChannels" + i) != null) audioTrack.setChannels(props.getProperty("audioChannels" + i, "").toString());
                    if ((audioTrack.getBitrate() == null || audioTrack.getBitrate().equals("")) && props.getProperty("audioBitrate" + i) != null) audioTrack.setBitrate(props.getProperty("audioBitrate" + i, "").toString());
                    log.debug("Audio: " + audioTrack.getCodec() + " | " + audioTrack.getLanguage() + " | " + audioTrack.getChannels() + " | " + audioTrack.getBitrate());
                    i++;
                }
                mf.getAudioTracks().clear();
                mf.getAudioTracks().addAll(audioTracks);
                p.getErrorStream().close();
                p.getInputStream().close();
                p.getOutputStream().close();
                int returnCode = p.waitFor();
                if (returnCode == 0) {
                } else {
                    log.error("Fehler mplayer Returncode = " + returnCode + " => " + pc.command());
                    mf.addWarning(new Warning(UMCConstants.STATUS_MPLAYER_ERROR, mf.getTitle() + ": Returncode -> " + returnCode));
                }
            } catch (NullPointerException exc) {
                log.error("NullPointer Fehler", exc);
                mf.addWarning(new Warning(UMCConstants.STATUS_MPLAYER_ERROR, mf.getTitle() + ": NullPointer Exception"));
            } catch (IndexOutOfBoundsException exc) {
                log.error("IndexOutOfBounds Fehler", exc);
                mf.addWarning(new Warning(UMCConstants.STATUS_MPLAYER_ERROR, mf.getTitle() + ": IndexOutOfBounds Exception"));
            } catch (SecurityException exc) {
                log.error("Security Fehler", exc);
                mf.addWarning(new Warning(UMCConstants.STATUS_MPLAYER_ERROR, mf.getTitle() + ": Security Exception"));
            } catch (IOException exc) {
                log.error("IO Fehler", exc);
                mf.addWarning(new Warning(UMCConstants.STATUS_MPLAYER_ERROR, mf.getTitle() + ": IO Exception"));
            } catch (Exception exc) {
                log.error("Allgemeiner Fehler", exc);
                mf.addWarning(new Warning(UMCConstants.STATUS_MPLAYER_ERROR, mf.getTitle() + ": Generic Exception"));
            }
        } else {
            log.error("MPlayer Verzeichniss " + System.getProperty("user.dir") + "/tools/" + " wurde nicht gefunden.");
            mf.addWarning(new Warning(UMCConstants.STATUS_MPLAYER_ERROR, "Verzeichniss " + System.getProperty("user.dir") + "/tools/" + " wurde nicht gefunden."));
        }
    }
