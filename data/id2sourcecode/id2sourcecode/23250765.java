        private void updateText() {
            try {
                setPainting(false);
                int location = 40;
                Video video = getVideo();
                if (icon.getResource() != null) icon.setResource(mEmptyIcon);
                if (video.getIcon() != null) {
                    if (video.getIcon().equals("in-progress-recording")) icon.setResource(mRedIcon); else if (video.getIcon().equals("expires-soon-recording")) icon.setResource(mYellowIcon); else if (video.getIcon().equals("expired-recording")) icon.setResource(mYellowExclamationIcon); else if (video.getIcon().equals("save-until-i-delete-recording")) icon.setResource(mGreenIcon); else if (video.getIcon().equals("suggestion-recording")) icon.setResource(mBlueIcon); else {
                        icon.setResource(mEmptyIcon);
                        location = 0;
                    }
                } else location = 0;
                titleText.setLocation(BORDER_LEFT + location, TOP);
                titleText.setValue(video.getTitle() == null ? "" : Tools.trim(video.getTitle(), 28));
                mCalendar.setTime(video.getOriginalAirDate() == null ? new Date() : video.getOriginalAirDate());
                mCalendar.set(GregorianCalendar.MINUTE, (mCalendar.get(GregorianCalendar.MINUTE) * 60 + mCalendar.get(GregorianCalendar.SECOND) + 30) / 60);
                mCalendar.set(GregorianCalendar.SECOND, 0);
                String description = null;
                if (video.getEpisodeTitle() != null && video.getEpisodeTitle().length() != 0) {
                    description = video.getEpisodeTitle() + " (" + mCalendar.get(Calendar.YEAR) + ")";
                    if (video.getDescription() != null) description = description + " " + video.getDescription();
                } else {
                    if (video.getDescription() == null) description = "(" + mCalendar.get(Calendar.YEAR) + ")"; else description = video.getDescription() + " (" + mCalendar.get(Calendar.YEAR) + ") ";
                }
                descriptionText.setValue(description);
                mCalendar.setTime(video.getDateRecorded() == null ? new Date() : video.getDateRecorded());
                mCalendar.set(GregorianCalendar.MINUTE, (mCalendar.get(GregorianCalendar.MINUTE) * 60 + mCalendar.get(GregorianCalendar.SECOND) + 30) / 60);
                mCalendar.set(GregorianCalendar.SECOND, 0);
                dateText.setLabel("Date:");
                String channel = String.valueOf(video.getChannelMajorNumber());
                if (channel.equals("0") && video.getChannel() != null) channel = video.getChannel();
                String value = mDateFormat.format(mCalendar.getTime());
                if (!channel.equals("0")) value = value + " - " + channel;
                if (video.getStation() != null) value = value + " " + video.getStation();
                dateText.setValue(value);
                int duration = (int) Math.rint(video.getDuration() / 1000 / 60.0);
                mCalendar.set(GregorianCalendar.HOUR_OF_DAY, (duration / 60));
                mCalendar.set(GregorianCalendar.MINUTE, duration % 60);
                mCalendar.set(GregorianCalendar.SECOND, 0);
                durationText.setLabel("Duration:");
                durationText.setValue(mTimeFormat.format(mCalendar.getTime()));
                ratingText.setLabel("Rated:");
                ratingText.setValue((video.getRating() == null ? "N/A" : video.getRating()));
                videoText.setLabel("Quality:");
                String txt = video.getRecordingQuality();
                String hd = video.getHighDefinition();
                if (hd != null && hd.equals("Yes")) txt = "[HD]"; else if (txt == null) txt = "DIGITAL"; else if (txt.length() == 0) txt = "UNKNOWN"; else if (txt.equalsIgnoreCase("good")) txt = "BASIC";
                videoText.setValue(txt);
                genreText.setLabel("Genre:");
                if (video.getProgramGenre() != null && video.getProgramGenre().length() > 0) value = Tools.trim(video.getProgramGenre(), 40); else value = "Unknown";
                genreText.setValue(value);
                sizeText.setLabel("Size:");
                sizeText.setValue(mNumberFormat.format(video.getSize() / (1024 * 1024)) + " MB");
                tivoText.setLabel("On TiVo:");
                if (video.getTivo() != null && video.getTivo().length() > 0) value = Tools.trim(video.getTivo(), 40); else value = "Unknown";
                tivoText.setValue(value);
                if (video.isParentalControls()) mLock.setVisible(true); else mLock.setVisible(false);
                statusText.setValue(video.getStatusString());
                if (video.getStatus() == Video.STATUS_DOWNLOADING || video.getStatus() == Video.STATUS_DOWNLOADED) {
                    statusBarBg.setVisible(true);
                    statusBar.setVisible(true);
                    statusBar.setSize(1, statusBar.getHeight());
                    if (video.getDownloadTime() > 0) {
                        long rate = (video.getDownloadSize() / 1024) / video.getDownloadTime();
                        statusText.setValue(video.getStatusString() + ": " + rate + " KB/Sec");
                        if (video.getStatus() == Video.STATUS_DOWNLOADED) {
                            statusBar.setSize(statusBarBg.getWidth() - 4, statusBar.getHeight());
                        } else {
                            float barFraction = video.getDownloadSize() / (float) video.getSize();
                            if ((statusBarBg.getWidth() - 4) * barFraction < 1) statusBar.setSize(1, statusBar.getHeight()); else {
                                int size = (int) (barFraction * (statusBarBg.getWidth() - 4));
                                if (size < 1) size = 1;
                                statusBar.setSize(size, statusBar.getHeight());
                            }
                        }
                    } else {
                        String progress = "";
                        for (int i = 0; i < mCounter; i++) progress = progress + ".";
                        statusText.setValue("Connecting" + progress);
                        statusBar.setVisible(false);
                        mCounter++;
                    }
                } else {
                    statusBarBg.setVisible(false);
                    statusBar.setVisible(false);
                }
                Boolean status = new Boolean(video.getStatus() == Video.STATUS_RULE_MATCHED || video.getStatus() == Video.STATUS_USER_SELECTED || video.getStatus() == Video.STATUS_DOWNLOADING || video.getStatus() == Video.STATUS_PROTECTED);
                if (status.booleanValue()) if (video.getStatus() == Video.STATUS_PROTECTED) {
                    mLock.setVisible(true);
                    list.set(0, "Copy-Protected");
                } else {
                    mLock.setVisible(false);
                    list.set(0, "Don't save to computer");
                } else list.set(0, "Save to computer");
            } finally {
                setPainting(true);
            }
            if (ToGoScreen.this.getApp().getContext() != null) flush();
        }
