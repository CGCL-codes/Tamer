    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        File ed = context.getDir(exceptionsDir, Context.MODE_PRIVATE);
        File out = new File(ed, ex.getClass().getSimpleName() + "@" + new Date().toString().replace(' ', '_'));
        final StringWriter result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);
        try {
            BufferedWriter bos = new BufferedWriter(new FileWriter(out));
            try {
                PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                bos.write("Version number = " + pi.versionCode + "\n");
                bos.write("Version name = " + pi.versionName + "\n");
            } catch (NameNotFoundException e) {
                bos.write("could not find package info");
            }
            bos.write(new Date().toString() + "\n\n");
            bos.write("SDK version = " + Build.VERSION.SDK + "\n");
            bos.write("Manufacturer = " + Build.MANUFACTURER + "\n");
            bos.write("Product = " + Build.PRODUCT + "\n");
            bos.write("Model = " + Build.MODEL + "\n");
            bos.write("Device = " + Build.DEVICE + "\n");
            bos.write("Brand = " + Build.BRAND + "\n");
            bos.write("Fingerprint = " + Build.FINGERPRINT + "\n\n");
            bos.write(result.toString());
            bos.write("\nOn thread " + thread.toString());
            bos.write("\n\nExtra log information:\n");
            for (Map.Entry<String, String> e : extraInfo.entrySet()) {
                bos.write(e.getKey());
                bos.write(":\t");
                bos.write(e.getValue());
            }
            bos.close();
        } catch (Exception e) {
            Log.e("ExceptionHandler", "Oh dear. encountered a " + e.getMessage() + " while trying to save a log for:", ex);
        }
        if (defaultHandler != null) {
            defaultHandler.uncaughtException(thread, ex);
        }
    }
