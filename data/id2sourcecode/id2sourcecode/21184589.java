    public void downloadFile(FEData data) {
        int temp;
        int c;
        URL url;
        HttpURLConnection urlConn = null;
        DataInputStream dis = null;
        FileOutputStream fos = null;
        try {
            url = new URL(data.getDocumentURL());
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            urlConn.setAllowUserInteraction(false);
            urlConn.setContentHandlerFactory(null);
            dis = new DataInputStream(urlConn.getInputStream());
            fos = new FileOutputStream(data.getLocalFileName(), false);
            while ((c = dis.read()) != -1) fos.write(c);
            fos.close();
            LaunchDoc launch = new LaunchDoc(data);
            launch.start();
            updateScreenInfo();
        } catch (MalformedURLException ex) {
            System.err.println(ex);
        } catch (java.io.IOException iox) {
            System.out.println(iox);
        } catch (Exception generic) {
            System.out.println(generic.toString());
        } finally {
            try {
                dis.close();
            } catch (Exception ex) {
            }
            try {
                fos.close();
            } catch (Exception exe) {
            }
        }
        return;
    }
