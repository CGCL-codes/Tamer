    public void updateMediaState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mediaAvailable = true;
            mediaWritable = true;
            Log.i(this.getClass().getSimpleName(), "External storage mounted in read/write mode, enabling avatars cache.");
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mediaAvailable = true;
            mediaWritable = false;
            Log.i(this.getClass().getSimpleName(), "External storage mounted in read-only mode, enabling avatars cache in read-only mode.");
        } else {
            mediaAvailable = false;
            mediaWritable = false;
            Log.i(this.getClass().getSimpleName(), "External storage unmounted, disabling avatars cache.");
        }
        avatarsPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/cz.jabbim.android/cache/avatars");
        try {
            avatarsPath.mkdirs();
        } catch (Exception e) {
            Log.w(this.getClass().getSimpleName(), "Unable to write cache directory on External Media!");
        }
    }
