    public boolean uploadFile(String pageName, String fileName, InputStream fileStream) {
        uploadData[0] = pageName;
        String urlString = getUploadFileURL();
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            return false;
        }
        try {
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + FORM_BOUNDARY);
            connection.setRequestProperty("User-Agent", "Wiki Poster");
            OutputStream outStream = connection.getOutputStream();
            PrintWriter out = new PrintWriter(outStream);
            for (int loop = 0; loop < uploadFields.length; loop++) {
                out.println("--" + FORM_BOUNDARY);
                out.println("Content-Disposition: form-data; name=\"" + uploadFields[loop] + "\"");
                out.println();
                out.println(uploadData[loop]);
            }
            out.println("--" + FORM_BOUNDARY);
            out.println("Content-Disposition: form-data; name=\"filename\"; filename=\"" + URLEncoder.encode(fileName) + "\"");
            out.println("Content-Type: application/octet-stream");
            out.println();
            out.flush();
            BufferedInputStream fileBuffer = new BufferedInputStream(fileStream);
            byte[] dataBuffer = new byte[1000];
            int size = fileBuffer.read(dataBuffer);
            while (size > 0) {
                outStream.write(dataBuffer, 0, size);
                size = fileBuffer.read(dataBuffer);
                outStream.flush();
            }
            fileStream.close();
            out.print("\r\n--" + FORM_BOUNDARY + "--");
            out.flush();
            String result = new String(FileHelper.readFile(connection.getInputStream()));
            System.out.println("Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
