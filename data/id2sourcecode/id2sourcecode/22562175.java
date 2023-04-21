    public static void main(String[] args) {
        GenericURLStreamHandlerFactory.getInstance().register("spk", new SPKHandlerFactory());
        try {
            InputStream is = new URL("spk:" + (new File("out.spk")).toURI().toASCIIString() + "|1A21A0BE14!/src/main/java/util/Random.java").openStream();
            int read;
            while ((read = is.read()) >= 0) {
                System.out.write(read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
