    private void copyDataBase() throws IOException {
        String Path = Environment.getDataDirectory() + "/data/net.sf.astroobserver/databases/";
        File saoFile = new File(Path, "dbraf.db");
        saoFile.delete();
        AssetManager assetManager = myContext.getAssets();
        OutputStream os = new FileOutputStream(saoFile);
        saoFile.createNewFile();
        byte[] b = new byte[1024];
        int i, r;
        String[] Files = assetManager.list("");
        Arrays.sort(Files);
        for (i = 1; i <= 17; i++) {
            String fn = String.format("file_%d.bin", i);
            if (Arrays.binarySearch(Files, fn) < 0) break;
            InputStream is = assetManager.open(fn);
            while ((r = is.read(b)) != -1) os.write(b, 0, r);
            is.close();
        }
        os.close();
    }
