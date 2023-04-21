    public File getEventAttacchment(final String url) {
        String token = getToken();
        String urlwithtoken = url + "?ticket=" + token;
        File file = new File(url);
        String filename = file.getName();
        File result = null;
        String path = createDir();
        if (path != null) {
            result = new File(path + "//" + file.getName());
        }
        BufferedInputStream in;
        try {
            in = new BufferedInputStream(new URL(urlwithtoken).openStream());
            FileOutputStream fos = new FileOutputStream(result);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
            fos.close();
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return result;
    }
