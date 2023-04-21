    public static File writeStreamToFile(File file, InputStream stream) {
        BufferedInputStream in = null;
        BufferedOutputStream outWriter = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e1) {
                ExceptionHandlingService.INSTANCE.handleException(e1);
                return null;
            }
        }
        try {
            in = new BufferedInputStream(stream);
            outWriter = new BufferedOutputStream(new FileOutputStream(file));
            int c;
            while ((c = in.read()) != -1) outWriter.write(c);
            in.close();
            outWriter.close();
        } catch (FileNotFoundException e) {
            ExceptionHandlingService.INSTANCE.handleException(e);
            return null;
        } catch (IOException e) {
            ExceptionHandlingService.INSTANCE.handleException(e);
            return null;
        }
        return file;
    }
