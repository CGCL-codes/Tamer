    private static void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPut httpput = new HttpPut(SugarSync_File_Upload_URL);
        httpput.setHeader("Authorization", auth_token);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody bin = new FileBody(file);
        reqEntity.addPart("", bin);
        httpput.setEntity(reqEntity);
        System.out.println("Now uploading your file into sugarsync........ Please wait......................");
        System.out.println("Now executing......." + httpput.getRequestLine());
        HttpResponse response = httpclient.execute(httpput);
        System.out.println(response.getStatusLine());
        if (response.getStatusLine().getStatusCode() == 204) {
            System.out.println("File uploaded successfully :)");
        } else {
            throw new Exception("There might be problem with your internet connection or server error. Please try again some after time :(");
        }
    }
