    public String readTextDoc(String docName) {
        String line;
        URL url = null;
        try {
            url = new URL(getCodeBase(), docName);
        } catch (MalformedURLException e) {
        }
        try {
            InputStream in = url.openStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            StringBuffer strBuff = new StringBuffer();
            while ((line = bf.readLine()) != null) {
                strBuff.append(line + "\n");
            }
            return strBuff.toString();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
        return null;
    }
