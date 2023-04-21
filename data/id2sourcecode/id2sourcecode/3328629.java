    private void installLibrary() {
        try {
            if (force_install_library) throw new Exception("forcing install");
            log.i("trying to load library " + LIBRARY_NAME + " w/o installation");
            System.loadLibrary(LIBRARY_NAME);
            log.i(LIBRARY_NAME + " loaded successfully");
        } catch (Exception ee) {
            log.i(SO_NAME + " not found using standard paths, will install manually");
            File sopath = mActivity.getDir("libs", Context.MODE_PRIVATE);
            File soname = new File(sopath, SO_NAME);
            try {
                sopath.mkdirs();
                File zip = new File(mActivity.getPackageCodePath());
                ZipFile zipfile = new ZipFile(zip);
                ZipEntry zipentry = zipfile.getEntry("lib/armeabi/" + SO_NAME);
                if (!soname.exists() || zipentry.getSize() != soname.length()) {
                    InputStream is = zipfile.getInputStream(zipentry);
                    OutputStream os = new FileOutputStream(soname);
                    Log.i("cr3", "Installing JNI library " + soname.getAbsolutePath());
                    final int BUF_SIZE = 0x10000;
                    byte[] buf = new byte[BUF_SIZE];
                    int n;
                    while ((n = is.read(buf)) > 0) os.write(buf, 0, n);
                    is.close();
                    os.close();
                } else {
                    log.i("JNI library " + soname.getAbsolutePath() + " is up to date");
                }
                System.load(soname.getAbsolutePath());
            } catch (Exception e) {
                log.e("cannot install " + LIBRARY_NAME + " library", e);
            }
        }
    }
