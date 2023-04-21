    public void setInputStream(InputStreamType type, String value) throws IOException {
        switch(type) {
            case STANDARD_INPUT:
                inputStream = System.in;
                break;
            case FILE_PATH:
                inputStream = new FileInputStream(value);
                break;
            case URL:
                URL url = new URL(value);
                URLConnection urlConn = url.openConnection();
                inputStream = urlConn.getInputStream();
                break;
            default:
                throw new IllegalArgumentException("Invalid input stream type");
        }
    }
