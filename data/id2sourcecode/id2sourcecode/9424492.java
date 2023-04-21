    static FedoraNode readURLbyName(String urlName) {
        try {
            URL url = new URL(urlName);
            return (readInputStream(url.openStream(), url.toString()));
        } catch (MalformedURLException e) {
            return (null);
        } catch (IOException ioe) {
            System.out.println("Problems Opening URL: " + urlName);
            return (null);
        }
    }
