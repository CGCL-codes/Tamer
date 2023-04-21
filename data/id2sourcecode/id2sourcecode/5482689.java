    public static String getSourceFrom(String adresse) {
        String toreturn = null;
        adresse = adresse.replaceAll(" ", "+");
        try {
            URL url = new URL(adresse);
            URLConnection uc = url.openConnection();
            InputStream in = uc.getInputStream();
            int c = in.read();
            StringBuilder build = new StringBuilder();
            while (c != -1) {
                build.append((char) c);
                c = in.read();
            }
            toreturn = build.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toreturn;
    }
