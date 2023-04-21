    static FedoraNode readResourcebyName(String resourceName) {
        System.out.println("resource URL is: " + resourceName);
        URL url = ClassLoader.getSystemClassLoader().getResource(resourceName);
        if (url == null) {
            System.out.println("Resource not found: " + resourceName);
            return (null);
        }
        try {
            fedoraNode = null;
            fedoraRoot = null;
            FedoraNode.model = null;
            return (readInputStream(url.openStream(), url.toString()));
        } catch (IOException ioe) {
            System.out.println("Problems Opening Resource: " + resourceName);
            return (null);
        }
    }
