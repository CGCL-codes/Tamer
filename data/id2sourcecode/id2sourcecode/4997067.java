        public void generateCaption(Context context) {
            if (mClusterChanged) {
                Resources resources = context.getResources();
                long minTimestamp = -1L;
                long maxTimestamp = -1L;
                if (areTimestampsAvailable()) {
                    minTimestamp = mMinTimestamp;
                    maxTimestamp = mMaxTimestamp;
                } else if (areAddedTimestampsAvailable()) {
                    minTimestamp = mMinAddedTimestamp;
                    maxTimestamp = mMaxAddedTimestamp;
                }
                if (minTimestamp != -1L) {
                    if (mIsPicassaAlbum) {
                        minTimestamp -= App.CURRENT_TIME_ZONE.getOffset(minTimestamp);
                        maxTimestamp -= App.CURRENT_TIME_ZONE.getOffset(maxTimestamp);
                    }
                    String minDay = DateFormat.format(MMDDYY_FORMAT, minTimestamp).toString();
                    String maxDay = DateFormat.format(MMDDYY_FORMAT, maxTimestamp).toString();
                    if (minDay.substring(4).equals(maxDay.substring(4))) {
                        mName = DateUtils.formatDateRange(context, minTimestamp, maxTimestamp, DateUtils.FORMAT_ABBREV_ALL);
                        if (minDay.equals(maxDay)) {
                            int flags = DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_DATE;
                            String dateRangeWithOptionalYear = DateUtils.formatDateTime(context, minTimestamp, flags);
                            String dateRangeWithYear = DateUtils.formatDateTime(context, minTimestamp, flags | DateUtils.FORMAT_SHOW_YEAR);
                            if (!dateRangeWithOptionalYear.equals(dateRangeWithYear)) {
                                long midTimestamp = (minTimestamp + maxTimestamp) / 2;
                                mName = DateUtils.formatDateRange(context, midTimestamp, midTimestamp, DateUtils.FORMAT_SHOW_TIME | flags);
                            }
                        }
                    } else {
                        int flags = DateUtils.FORMAT_NO_MONTH_DAY | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_DATE;
                        mName = DateUtils.formatDateRange(context, minTimestamp, maxTimestamp, flags);
                    }
                } else {
                    mName = resources.getString(Res.string.date_unknown);
                }
                updateNumExpectedItems();
                generateTitle(false);
                mClusterChanged = false;
            }
        }
