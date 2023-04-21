    public void addAttachmentFromUrl(String httpUrl) {
        try {
            URL url = new URL(httpUrl);
            String fileName = url.getPath();
            int slashIndex = fileName.lastIndexOf("/");
            if (slashIndex >= 0) {
                fileName = fileName.substring(slashIndex + 1);
            }
            String oldFileName = fileName;
            String suffix = null;
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex >= 0) {
                suffix = fileName.substring(dotIndex);
                fileName = fileName.substring(0, dotIndex);
            }
            while (fileName.length() < 3) {
                fileName = "0" + fileName;
            }
            File tempFile = File.createTempFile(fileName, suffix);
            tempFile.deleteOnExit();
            this.tempFileNameMappings.put(tempFile.getAbsolutePath(), oldFileName);
            InputStream inputStream = url.openStream();
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[512];
            for (int length = 0; (length = inputStream.read(buffer)) != -1; ) {
                fileOutputStream.write(buffer, 0, length);
            }
            fileOutputStream.close();
            inputStream.close();
            this.addAttachmentFromFile(tempFile.getAbsolutePath());
        } catch (MalformedURLException exception) {
            log.error("The URL is invalid: " + httpUrl, exception);
        } catch (IOException exception) {
            log.error("Error opening the URL " + httpUrl, exception);
        }
    }
