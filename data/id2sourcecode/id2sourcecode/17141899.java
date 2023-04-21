    private void setDevice() {
        try {
            File f = new File("og_description.xml");
            try {
                InputStream inputStream = getClass().getResourceAsStream(DESCRIPTION_FILE_URL);
                OutputStream out = new FileOutputStream(f);
                byte buf[] = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) out.write(buf, 0, len);
                out.close();
                inputStream.close();
            } catch (IOException e) {
            }
            ogDev = new OGDevice(f);
        } catch (InvalidDescriptionException e) {
            append("\n" + e.toString());
            e.printStackTrace();
        }
    }
