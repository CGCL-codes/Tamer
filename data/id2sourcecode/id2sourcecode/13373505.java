    private boolean writeFile(String url, File file, HttpResponse res) {
        boolean result = true;
        try {
            MapedFile mapedFile = fileCache.get(url);
            if (mapedFile == null) {
                mapedFile = new MapedFile();
                mapedFile.lastModified = file.lastModified();
                mapedFile.size = file.length();
                mapedFile.contentType = context.getMimeType(file.getName());
                FileChannel fileChannel = new FileInputStream(file).getChannel();
                mapedFile.data = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
                fileChannel.close();
                fileCache.put(url, mapedFile, 60);
            }
            res.setContentLength(mapedFile.size);
            res.setContentType(mapedFile.contentType);
            res.setLastModified(mapedFile.lastModified);
            res.setHttpResult(HTTP.HTTP_OK);
            res.flush(mapedFile.data.slice());
        } catch (IOException e) {
            result = false;
            Log.logger.warning(e.getMessage());
        }
        return result;
    }
