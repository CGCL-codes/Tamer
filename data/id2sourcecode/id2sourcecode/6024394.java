    public static String webService(String siteUrl, String login, String password, String table, String station, String element, String dayFrom, String dayTo, String format, String sampling, String filePath) throws Exception {
        Service service = new Service();
        Call call = (Call) service.createCall();
        if (login != null) {
            call.setUsername(login);
            if (password != null) {
                call.setPassword(password);
            }
            System.err.println("Info: authentication user=" + login + " passwd=" + password + " at " + siteUrl);
        }
        call.setTargetEndpointAddress(new URL(siteUrl));
        call.setOperationName("getData");
        call.setTimeout(new Integer(60 * 1000 * 30));
        String url = (String) call.invoke(new Object[] { table, station, element, dayFrom, dayTo, format, sampling });
        String fileName = null;
        if (url == null) {
            throw new Exception("Error: result URL is null");
        } else {
            System.err.println("Info: result URL is " + url);
            URL dataurl = new URL(url);
            String filePart = dataurl.getFile();
            if (filePart == null) {
                throw new Exception("Error: file part in the data URL is null");
            } else {
                fileName = filePart.substring(filePart.lastIndexOf("/") < 0 ? 0 : filePart.lastIndexOf("/") + 1);
                if (filePath != null) {
                    fileName = filePath + fileName;
                }
                System.err.println("Info: local file name is " + fileName);
            }
            FileOutputStream file = new FileOutputStream(fileName);
            if (file == null) {
                throw new Exception("Error: file output stream is null");
            }
            InputStream strm = dataurl.openStream();
            if (strm == null) {
                throw new Exception("Error: data input stream is null");
            } else {
                int c;
                while ((c = strm.read()) != -1) {
                    file.write(c);
                }
            }
        }
        return fileName;
    }
