    public Void doInBackground(Void... unused) {
        try {
            URL url = new URL("http://www.cis.umassd.edu/~bcollins/books/" + bookFolder + "/" + bookFolder + ".zip");
            File bookZip = new File(Environment.getExternalStorageDirectory() + "/" + bookFolder + ".zip");
            long startTime = System.currentTimeMillis();
            Log.d("Book Downloader", "Download Starting");
            Log.d("Book Downloader", "Downloading: " + url);
            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            FileOutputStream fos = new FileOutputStream(bookZip);
            fos.write(baf.toByteArray());
            fos.close();
            Log.d("BookDownloader", "Downloaded");
            unzip(Environment.getExternalStorageDirectory() + "/" + bookFolder + ".zip", internalStorage);
            bookZip.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
