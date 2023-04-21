    public static String loadReadme(String filename) {
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line = null;
            StringBuilder vud = new StringBuilder();
            while ((line = in.readLine()) != null) {
                vud.append(line + "\n");
            }
            in.close();
            return vud.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
