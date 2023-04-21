    public void parse(InputStream data, String site) {
        if ((repository != null) && (repository.exists())) {
            File dest = null;
            if ((Boolean) properties.get("URL2Filename").get()) {
                try {
                    dest = new File(repository.getAbsolutePath() + File.separator + URLEncoder.encode(site, "UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(ToFileParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (filename == null) {
                synchronized (number) {
                    dest = new File(repository.getAbsolutePath() + File.separator + subDirectory + number.toString());
                    number = new Integer(number.intValue() + 1);
                }
            } else {
                File dir = new File(repository.getAbsolutePath() + File.separator + subDirectory);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                dest = new File(repository.getAbsolutePath() + File.separator + subDirectory + filename);
            }
            if (dest != null) {
                try {
                    java.io.BufferedOutputStream output = new java.io.BufferedOutputStream(new java.io.FileOutputStream(dest));
                    byte[] buffer = new byte[1024];
                    int num_read = 0;
                    while ((num_read = data.read(buffer)) > 0) {
                        output.write(buffer, 0, num_read);
                    }
                    output.flush();
                    output.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
