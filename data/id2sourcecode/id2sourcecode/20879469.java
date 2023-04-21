    private String copyResourceInner(Uri packageURI, String newCid, String key, String resFileName) {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            Log.w(TAG, "Make sure sdcard is mounted.");
            return null;
        }
        String codePath = packageURI.getPath();
        File codeFile = new File(codePath);
        String newCachePath = null;
        if ((newCachePath = PackageHelper.createSdDir(codeFile, newCid, key, Process.myUid())) == null) {
            Log.e(TAG, "Failed to create container " + newCid);
            return null;
        }
        if (localLOGV) Log.i(TAG, "Created container for " + newCid + " at path : " + newCachePath);
        File resFile = new File(newCachePath, resFileName);
        if (!FileUtils.copyFile(new File(codePath), resFile)) {
            Log.e(TAG, "Failed to copy " + codePath + " to " + resFile);
            PackageHelper.destroySdDir(newCid);
            return null;
        }
        if (localLOGV) Log.i(TAG, "Copied " + codePath + " to " + resFile);
        if (!PackageHelper.finalizeSdDir(newCid)) {
            Log.e(TAG, "Failed to finalize " + newCid + " at path " + newCachePath);
            PackageHelper.destroySdDir(newCid);
        }
        if (localLOGV) Log.i(TAG, "Finalized container " + newCid);
        if (PackageHelper.isContainerMounted(newCid)) {
            if (localLOGV) Log.i(TAG, "Unmounting " + newCid + " at path " + newCachePath);
            Runtime.getRuntime().gc();
            PackageHelper.unMountSdDir(newCid);
        } else {
            if (localLOGV) Log.i(TAG, "Container " + newCid + " not mounted");
        }
        return newCachePath;
    }
