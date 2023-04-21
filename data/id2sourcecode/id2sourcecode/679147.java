    public void saveFile(String name, String author, String description) {
        if (description == null) description = "";
        String data = "name=" + URLEncoder.encode(name) + "&description=" + URLEncoder.encode(description) + "&author=" + URLEncoder.encode(author) + "&file=" + reader.saveFile();
        System.out.println("Saving file: " + data);
        try {
            URL url = getResource("prgup.php");
            URLConnection urlc = url.openConnection();
            urlc.setDoOutput(true);
            urlc.setUseCaches(false);
            HttpURLConnection httpConnection = (HttpURLConnection) urlc;
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream out = new DataOutputStream(httpConnection.getOutputStream());
            out.writeBytes(data);
            out.flush();
            out.close();
            InputStream is = httpConnection.getInputStream();
            System.out.println("Read back:");
            int c;
            while ((c = is.read()) != -1) {
                System.out.print((char) c);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
