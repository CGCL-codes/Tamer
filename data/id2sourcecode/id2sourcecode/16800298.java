    @Override
    public URL uploadPicture(Picture picture, ConfigurationValue[] configurationValues) throws Exception {
        String username = ConfigurationValue.getConfigurationValue(FTP_USERNAME, configurationValues);
        if (username == null || username.length() == 0) {
            throw new CommonException(Language.translateStatic("ERROR_NOFTPUSERNAMESET"));
        }
        String password = ConfigurationValue.getConfigurationValue(FTP_PASSWORD, configurationValues);
        if (password == null || password.length() == 0) {
            throw new CommonException(Language.translateStatic("ERROR_NOFTPPASSOWRDSET"));
        }
        String ftpFolder = ConfigurationValue.getConfigurationValue(FTP_PICTUREFOLDER, configurationValues);
        if (ftpFolder != null) {
            if (!ftpFolder.startsWith("/")) {
                ftpFolder = "/" + ftpFolder;
            }
            if (!ftpFolder.endsWith("/")) {
                ftpFolder = ftpFolder + "/";
            }
        } else {
            ftpFolder = "/";
        }
        String host = ConfigurationValue.getConfigurationValue(FTP_HOSTNAME, configurationValues);
        if (host == null || host.length() == 0) {
            throw new CommonException(Language.translateStatic("ERROR_NOFTPHOSTSET"));
        }
        Integer port = ConfigurationValue.getConfigurationValue(FTP_PORT, configurationValues);
        if (port < 1) {
            port = 21;
        }
        String filename = picture.getFileName();
        URL url = new URL("ftp://" + URLEncoder.encode(username, "utf-8") + ":" + URLEncoder.encode(password, "utf-8") + "@" + host + ":" + port + ftpFolder + URLEncoder.encode(filename, "utf-8"));
        URLConnection connection = url.openConnection();
        OutputStream outputStream = connection.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(picture.getFile());
        int read = 0;
        byte[] buffer = new byte[1024];
        while (read != -1) {
            outputStream.write(buffer, 0, read);
            read = fileInputStream.read(buffer);
        }
        fileInputStream.close();
        outputStream.close();
        URL pictureURL = ConfigurationValue.getConfigurationValue(FTP_HTTPPREFIX, configurationValues);
        if (pictureURL == null) {
            return new URL(host + ftpFolder + filename);
        } else {
            return new URL(pictureURL + "/" + filename);
        }
    }
