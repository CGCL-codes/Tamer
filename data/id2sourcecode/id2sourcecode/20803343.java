    public boolean uploadPage(String pageName, String pageData) {
        if (pageName == null) {
            pageName = SANDBOX;
        }
        EditTransfer transfer = getPageEditTransfer(pageName);
        if (transfer == null) {
            return false;
        }
        saveData[0] = pageName;
        saveData[2] = "" + transfer.edittime;
        saveData[3] = transfer.text + "\n" + pageData;
        String urlString = getUploadPageURL(pageName);
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
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("User-Agent", "Wiki Poster");
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            StringBuffer dataString = new StringBuffer();
            for (int loop = 0; loop < saveFields.length; loop++) {
                if (loop > 0) dataString.append('&');
                dataString.append(saveFields[loop] + "=" + URLEncoder.encode(saveData[loop]));
            }
            out.print(dataString.toString());
            out.flush();
            String result = new String(FileHelper.readFile(connection.getInputStream()));
            System.out.println("Result: " + result);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
