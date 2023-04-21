    public boolean fetchURL2File(String updateName, String urlString, String updateFile) {
        boolean result = true;
        try {
            URL url = new URL("http://code.google.com/p/iwidget/" + urlString);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            byte buffer[] = new byte[8192];
            InputStream is = connection.getInputStream();
            int iCtr = is.read(buffer);
            int iTotal = iCtr;
            String testResult = new String(buffer);
            if (testResult.startsWith("<html><head>")) {
                result = false;
            } else {
                FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + updateFile);
                while (iCtr > -1) {
                    status.setText("Updating " + updateName + " bytes transferred:" + iTotal);
                    fos.write(buffer, 0, iCtr);
                    iCtr = is.read(buffer);
                    iTotal += iCtr;
                }
                fos.close();
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
