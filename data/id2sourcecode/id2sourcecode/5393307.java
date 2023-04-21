    public void readToByteArray(String file) {
        try {
            bytes = IOUtils.toByteArray(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
