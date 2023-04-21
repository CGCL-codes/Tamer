    public String downloadFile(URL url, String saveInDir, String saveAsFilename, DownloadProgressListener downloadProgressListener) {
        String sdDrive = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fullLocalDirPath = String.format("%s/%s", sdDrive, saveInDir);
        Log.d("warenix", "saved in " + fullLocalDirPath);
        boolean success = (new File(fullLocalDirPath)).mkdirs();
        if (success) {
            Log.i("warenix", String.format("created dir[%s]", fullLocalDirPath));
        }
        InputStream in = null;
        BufferedOutputStream out = null;
        String full_local_file_path = String.format("%s/%s", fullLocalDirPath, saveAsFilename);
        Log.v("warenix", String.format("of to %s", full_local_file_path));
        try {
            FileOutputStream fos = new FileOutputStream(full_local_file_path);
            BufferedOutputStream bfs = new BufferedOutputStream(fos, IO_BUFFER_SIZE);
            int iFileSize = DownloadFileTool.getContentLength(url);
            Log.d("warenix", String.format("going to download file size %d", iFileSize));
            in = new BufferedInputStream(url.openStream(), IO_BUFFER_SIZE);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            copy(in, out, downloadProgressListener, iFileSize);
            out.flush();
            final byte[] data = dataStream.toByteArray();
            bfs.write(data, 0, data.length);
            bfs.flush();
        } catch (IOException e) {
        } finally {
            closeStream(in);
            closeStream(out);
        }
        return full_local_file_path;
    }
