    public static int getChannelId(Context context) {
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData.getInt("ICD_CHANNEL");
        } catch (NameNotFoundException e) {
            return 0;
        }
    }
