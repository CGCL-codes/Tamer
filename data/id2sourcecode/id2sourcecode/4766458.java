    private boolean saveMP3(URL url, DownloadTask task) {
        if (url == null) {
            return false;
        }
        FileOutputStream out = null;
        InputStream stream = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File file = new File(task.source.getPreferredMP3Location());
        publish("Starting download to file:" + file.getAbsolutePath());
        try {
            out = new FileOutputStream(file);
            URLConnection uc = url.openConnection();
            String contentType = uc.getContentType();
            int length = uc.getContentLength();
            StatisticsStorage.numberofdownloadedbytes += length;
            publish("Content type: " + contentType + " with a length of " + length);
            stream = uc.getInputStream();
            bis = new BufferedInputStream(stream);
            bos = new BufferedOutputStream(out);
            byte[] bytes = new byte[1024];
            int i = 0;
            long time = System.currentTimeMillis();
            int nextbyte;
            while ((nextbyte = bis.read()) != -1) {
                bos.write(nextbyte);
                i++;
                if (System.currentTimeMillis() - time >= 1000) {
                    publish("downloaded " + (i++) + " bytes of " + length + " bytes.");
                    time = System.currentTimeMillis();
                    int progress = (i - 1) / length;
                    if (progress <= 100 && progress >= 0) setProgress(progress);
                }
            }
            bos.write(bytes);
        } catch (IOException e) {
            System.err.println(url);
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) out.close();
                if (stream != null) stream.close();
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                return false;
            }
        }
        publish("done downloading mp3");
        task.source.setPath(file.getAbsolutePath());
        FileStorage.addMP3FileInformation(task.source);
        if (task.source.hasLyrics()) {
            task.source.getLyrics().setTag("MP3", file.getName());
        }
        return true;
    }
