    public void testUpload() throws Exception {
        ArrayList<byte[]> fileContents = new ArrayList<byte[]>();
        ArrayList<String> fileNamesAsArray = new ArrayList<String>();
        File file = null;
        FileInputStream fileStream = null;
        for (String fileName : fileNames) {
            file = new File(directory + File.separator + fileName);
            fileStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileStream.read(bytes);
            fileContents.add(bytes);
            fileNamesAsArray.add(fileName);
        }
        for (String host : hosts) {
            UploadServiceInterface service = new UploadService(host + Util.UPLOAD_SERVICE);
            service.upload(packageName, toByteArray(fileContents), toByteArray(fileNamesAsArray));
        }
    }
