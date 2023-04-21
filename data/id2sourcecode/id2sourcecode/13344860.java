    public static File downloadFile(String url, File targetFile) {
        URL uURL;
        InputStream is = null;
        DataInputStream dis = null;
        BufferedOutputStream outWriter = null;
        if (!targetFile.exists()) try {
            targetFile.createNewFile();
        } catch (IOException e) {
            ExceptionHandlingService.INSTANCE.handleException(e);
            return null;
        }
        try {
            uURL = new URL(url);
            is = uURL.openStream();
            dis = new DataInputStream(new BufferedInputStream(is));
            outWriter = new BufferedOutputStream(new FileOutputStream(targetFile), 4096);
            int c;
            while ((c = dis.read()) != -1) outWriter.write(c);
        } catch (MalformedURLException mue) {
            ExceptionHandlingService.INSTANCE.handleException(mue);
        } catch (IOException ioe) {
            ExceptionHandlingService.INSTANCE.handleException(ioe);
        } finally {
            try {
                if (is != null) is.close();
                if (dis != null) dis.close();
                if (outWriter != null) outWriter.close();
            } catch (IOException ioe) {
            }
        }
        return targetFile;
    }
