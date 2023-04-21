    public void updateTimeLabel() {
        if (reader != null) timeField.setText(Time.writeTime(reader.getCurrentTime_s(), Time.TIMEFORMAT_HHMMSS, '-'));
    }
