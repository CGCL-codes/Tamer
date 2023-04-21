    public static boolean DownloadFromUrl(String imageURL, String fileName) {
        try {
            URL url = new URL(imageURL);
            File file = new File(fileName);
            long startTime = System.currentTimeMillis();
            Log.d("ImageManager", "comecando download");
            Log.d("ImageManager", "url:" + url);
            Log.d("ImageManager", "nome do arquivo:" + fileName);
            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            Log.d("ImageManager", "download terminou em" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");
            return true;
        } catch (IOException e) {
            Log.d("ImageManager", "Error:" + e);
            return false;
        }
    }
