    public static void start(Context content) {
        mContext = content;
        mDateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long saveTime = mSaveTime * 24 * 60 * 60 * 1000;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        mConfig = mContext.getSharedPreferences("log_config", Context.MODE_PRIVATE);
        mLogFile = new FileRW(PATH, null, null);
        String startTimeS = mConfig.getString(START_TIME_STRING, null);
        if (startTimeS != null) {
            try {
                if (today.getTime() - df.parse(startTimeS).getTime() > saveTime) {
                    deleteFiles(saveTime, df, today);
                }
                mLastFile = mConfig.getString(LAST_FILE_NAME, null);
                if (mLastFile != null && mLastFile.contains(df.format(today))) {
                    mLogFile.Close();
                    mLogFile = new FileRW(PATH, mLastFile, null);
                    mFileSize = mConfig.getInt(LAST_FILE_SIZE, 0);
                } else {
                    mLogFile.Close();
                    mLastFile = df.format(today) + "_" + mFileCount + FILE_NAME_POSTFIX;
                    mLogFile = new FileRW(PATH, mLastFile, null);
                }
                mFileCount = mConfig.getInt(FILE_COUNT, 0);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            mLogFile.Close();
            mLastFile = df.format(today) + "_" + mFileCount + FILE_NAME_POSTFIX;
            mLogFile = new FileRW(PATH, mLastFile, null);
            SharedPreferences.Editor configEdit = mConfig.edit();
            configEdit.putString(START_TIME_STRING, df.format(new Date(today.getTime())));
            configEdit.commit();
        }
        writeFileThread = new Thread(save);
        writeFileThread.setDaemon(true);
        writeFileThread.start();
    }
