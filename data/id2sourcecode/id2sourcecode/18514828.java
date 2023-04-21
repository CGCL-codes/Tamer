    public static void main(String[] args) {
        try {
            System.out.println("Starting Launcher...");
            boolean openDefaultDocument = !Boolean.getBoolean("SAFE_MODE");
            URL url = null;
            if (openDefaultDocument) {
                try {
                    url = PreferenceController.getDefaultDocumentURL();
                    openDefaultDocument = url != null;
                    if (openDefaultDocument) {
                        int contentLength = url.openConnection().getContentLength();
                        openDefaultDocument = contentLength > 0;
                        if (!openDefaultDocument) {
                            throw new RuntimeException("Contents of \"" + url + "\" is missing.");
                        }
                    }
                } catch (MalformedURLException exception) {
                    openDefaultDocument = false;
                    System.err.println(exception);
                } catch (Exception exception) {
                    openDefaultDocument = false;
                    System.err.println(exception);
                    System.err.println("Default document \"" + url + "\" cannot be openned.");
                    Application.displayApplicationError("Launch Exception", "Default document \"" + url + "\" cannot be openned.", exception);
                }
            }
            URL[] urls = (openDefaultDocument) ? new URL[] { url } : new URL[] {};
            Application.launch(new Main(), urls);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            exception.printStackTrace();
            Application.displayApplicationError("Launch Exception", "Launch Exception", exception);
            System.exit(-1);
        }
    }
