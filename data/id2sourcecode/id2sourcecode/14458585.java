    protected void downloadPicture(String urlpath) {
        try {
            URL url = new URL(urlpath);
            File file = new File(ImageManager.getLocalStorageLocation(urlpath));
            if (!file.exists()) file.createNewFile();
            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.outHeight = 800;
            o.outWidth = 800;
            Bitmap bm = BitmapFactory.decodeByteArray(baf.toByteArray(), 0, baf.toByteArray().length);
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
