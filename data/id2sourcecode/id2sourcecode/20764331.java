    public ArrayList<String> readURL() throws MalformedURLException, IOException {
        ArrayList<String> ret = new ArrayList<String>();
        String str;
        URL url = new URL(xmlpath);
        URLConnection urlc = null;
        DataInputStream dis;
        urlc = url.openConnection();
        urlc.setDoInput(true);
        urlc.setUseCaches(false);
        dis = new DataInputStream(urlc.getInputStream());
        while ((str = dis.readLine()) != null) {
            ret.add(str.trim());
        }
        dis.close();
        return ret;
    }
