    private static InputStream readFromServer(String filename) {
        InputStream is = null;
        try {
            if (filename.startsWith("http")) {
                URL url = new URL(filename);
                URLConnection uc = url.openConnection();
                is = uc.getInputStream();
            } else {
                is = (InputStream) (new FileInputStream(new File(filename)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }
