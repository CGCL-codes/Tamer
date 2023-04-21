    public static void setup() {
        InputStream inputStream = GroundControl.class.getResourceAsStream("/res/test-ass.zip");
        File f = new File(FileOps.tempdir + FileOps.sep + "test-ass.zip");
        f.deleteOnExit();
        try {
            OutputStream out = new FileOutputStream(f);
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        cp = new AssessmentContentPackage(f);
    }
