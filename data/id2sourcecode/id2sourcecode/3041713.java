    public void statusChanged(StatusEvent se) {
        log.debug("statusChanged=" + se);
        if (se.getNewStatus() == StatusEvent.ERROR) {
            synchronized (this) {
                try {
                    mTrack = mVideocast.getTrack(mTrack.getUrl());
                } catch (Exception ex) {
                    Tools.logException(VideocastingThread.class, ex, "Track update failed");
                }
            }
            synchronized (this) {
                try {
                    mTrack.setStatus(VideocastTrack.STATUS_DOWNLOAD_ERROR);
                    int errors = mTrack.getErrors();
                    mTrack.setErrors(errors + 1);
                    VideocastManager.updateVideocast(mVideocast);
                    mTrack = mVideocast.getTrack(mTrack.getUrl());
                } catch (HibernateException ex) {
                    Tools.logException(VideocastingThread.class, ex, "Track update failed");
                }
            }
            mDownloadNext = true;
            mWaiting = false;
            interrupt();
        } else if (se.getNewStatus() == StatusEvent.IN_PROGRESS) {
            synchronized (this) {
                try {
                    mTrack = mVideocast.getTrack(mTrack.getUrl());
                } catch (Exception ex) {
                    Tools.logException(VideocastingThread.class, ex, "Track update failed");
                }
            }
            synchronized (this) {
                try {
                    mTrack.setStatus(VideocastTrack.STATUS_DOWNLOADING);
                    VideocastManager.updateVideocast(mVideocast);
                    mTrack = mVideocast.getTrack(mTrack.getUrl());
                } catch (HibernateException ex) {
                    Tools.logException(VideocastingThread.class, ex, "Track update failed");
                }
            }
            mStart = System.currentTimeMillis();
        } else if (se.getNewStatus() == StatusEvent.COMPLETED) {
            synchronized (this) {
                try {
                    mTrack = mVideocast.getTrack(mTrack.getUrl());
                } catch (Exception ex) {
                    Tools.logException(VideocastingThread.class, ex, "Track update failed");
                }
            }
            try {
                if (mDownload.getElapsedTime() > 0) log.info("Download rate=" + mDownload.getSize() / mDownload.getElapsedTime() + " KBps");
                synchronized (this) {
                    try {
                        Video video = null;
                        List<Video> videos = VideoManager.findByPath(mDownload.getLocalFile().getCanonicalPath());
                        if (videos != null && videos.size() > 0) {
                            video = videos.get(0);
                            if (video != null) {
                                video.setOrigen("Videocast");
                                if (mTrack.getDuration() != 0) video.setDuration((int) mTrack.getDuration() / 1000);
                                VideoManager.updateVideo(video);
                            }
                        } else {
                            try {
                                video = VideoFile.getVideo(mDownload.getLocalFile().getCanonicalPath());
                            } catch (Exception ex) {
                                Tools.logException(VideocastingThread.class, ex, "Track tags failed");
                            }
                            if (video != null) {
                                video.setOrigen("Videocast");
                                if (mTrack.getDuration() != 0) video.setDuration((int) mTrack.getDuration() / 1000);
                                VideoManager.createVideo(video);
                            }
                        }
                        if (video != null) {
                            mTrack.setSize(mDownload.getLocalFile().length());
                            mTrack.setTrack(video);
                            mTrack.setStatus(VideocastTrack.STATUS_DOWNLOADED);
                            VideocastManager.updateVideocast(mVideocast);
                            boolean correctFormat = VideoFile.isTiVoFormat(video);
                            ServerConfiguration serverConfiguration = Server.getServer().getServerConfiguration();
                            GoBackConfiguration goBackConfiguration = serverConfiguration.getGoBackConfiguration();
                            String path = System.getProperty("data") + File.separator + "videocasts" + File.separator + clean(mVideocast.getTitle());
                            File dir = new File(path);
                            if (correctFormat) {
                                String name = clean(mTrack.getTitle()) + ".mpg";
                                File output = new File(dir.getCanonicalPath() + File.separator + name);
                                mDownload.getLocalFile().renameTo(output);
                                video.setPath(output.getCanonicalPath());
                                VideoManager.updateVideo(video);
                            } else if (goBackConfiguration.isConvertVideo()) {
                                String name = clean(mTrack.getTitle()) + ".mpg";
                                File output = new File(dir.getCanonicalPath() + File.separator + name);
                                if (VideoFile.convert(video, output.getCanonicalPath())) {
                                    Video converted = VideoFile.getVideo(output.getCanonicalPath());
                                    if (converted != null && output.length() > 0) {
                                        converted.setOrigen("Videocast");
                                        VideoManager.createVideo(converted);
                                        mTrack.setTrack(converted);
                                        VideocastManager.updateVideocast(mVideocast);
                                        VideoManager.deleteVideo(video);
                                        File file = new File(video.getPath());
                                        if (file.exists()) file.delete();
                                        File data = new File(System.getProperty("data") + File.separator + "temp");
                                        if (data.exists() && data.isDirectory()) {
                                            File[] files = data.listFiles();
                                            for (int i = 0; i < files.length; i++) files[i].delete();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Tools.logException(VideocastingThread.class, ex, "Track update failed");
                    }
                }
                mDownloadNext = true;
            } catch (Exception ex) {
                Tools.logException(VideocastingThread.class, ex, mTrack.getUrl());
                mDownloadNext = true;
            } finally {
            }
            mWaiting = false;
            interrupt();
        }
    }
