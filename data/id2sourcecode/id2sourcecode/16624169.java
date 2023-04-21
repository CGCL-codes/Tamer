    public static void main(String[] args) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("org/da/expressionj/resources/expressionj.properties");
        try {
            PropertyResourceBundle prb = new PropertyResourceBundle(url.openStream());
            String version = prb.getString("version");
            String date = prb.getString("date");
            System.out.println("expresssionJ version " + version + " build on " + date);
            System.out.println("Distributed under LGPL 2.1 license");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
