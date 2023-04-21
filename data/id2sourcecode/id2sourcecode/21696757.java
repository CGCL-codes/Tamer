        public Object getValueAt(int nRow, int nCol) {
            if (nRow < 0 || nRow >= getRowCount()) return "";
            Video show = (Video) mRecorded.get(nRow);
            switch(nCol) {
                case 0:
                    return new Boolean(show.getStatus() == Video.STATUS_RULE_MATCHED || show.getStatus() == Video.STATUS_USER_SELECTED || show.getStatus() == Video.STATUS_DOWNLOADING);
                case 1:
                    return show.getTitle() == null ? "" : show.getTitle();
                case 2:
                    return show.getEpisodeTitle() == null ? "" : show.getEpisodeTitle();
                case 3:
                    mCalendar.setTime(show.getDateRecorded());
                    mCalendar.set(GregorianCalendar.MINUTE, (mCalendar.get(GregorianCalendar.MINUTE) * 60 + mCalendar.get(GregorianCalendar.SECOND) + 30) / 60);
                    mCalendar.set(GregorianCalendar.SECOND, 0);
                    return mDateFormat.format(mCalendar.getTime());
                case 4:
                    int duration = (int) Math.rint(show.getDuration() / 1000 / 60.0);
                    mCalendar.set(GregorianCalendar.HOUR_OF_DAY, duration / 60);
                    mCalendar.set(GregorianCalendar.MINUTE, duration % 60);
                    mCalendar.set(GregorianCalendar.SECOND, 0);
                    return mTimeFormat.format(mCalendar.getTime());
                case 5:
                    return mNumberFormat.format(show.getSize() / (1024 * 1024)) + " MB";
                case 6:
                    return show.getStatusString() == null ? "" : show.getStatusString();
                case 7:
                    Iterator iterator = mTiVos.iterator();
                    while (iterator.hasNext()) {
                        TiVo tivo = (TiVo) iterator.next();
                        if (show.getSource().equals(tivo.getAddress())) {
                            return tivo.getName();
                        }
                    }
                    return "Unknown";
                case 8:
                    return show.getDescription() == null ? " " : show.getDescription();
                case 9:
                    String txtChannel = "";
                    String txtStation = "";
                    if (show.getChannel() != null) txtChannel = show.getChannel();
                    if (show.getStation() != null) txtStation = show.getStation();
                    return txtChannel + " " + txtStation;
                case 10:
                    return (show.getRating() == null || show.getRating().length() != 0) ? show.getRating() : "N/A";
                case 11:
                    if (show.getHighDefinition().equals("Yes")) return "[HD]"; else if (show.getRecordingQuality() == null) return "DIGITAL"; else if (show.getRecordingQuality().length() == 0) return "UNKNOWN"; else if (show.getRecordingQuality().equalsIgnoreCase("good")) return "BASIC"; else return show.getRecordingQuality();
            }
            return " ";
        }
