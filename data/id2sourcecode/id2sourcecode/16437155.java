    public boolean downloadUpdate(Update upd) {
        upd.addListener(this);
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        try {
            URL url = new URL(Constants.UPDATE_DIR + upd.getPath());
            String path = upd.getPath();
            if (path.startsWith("./")) {
                path = path.substring(2, path.length());
            }
            path = "updates/" + path;
            int index = path.lastIndexOf("/");
            if (index != -1) {
                String dirs = path.substring(0, path.lastIndexOf("/"));
                System.out.println(dirs);
                File file = new File(dirs);
                if (!file.exists()) {
                    System.out.println(file.mkdirs());
                }
            }
            File file = new File(path);
            if (!file.exists()) {
                if (file.isDirectory()) {
                    file.mkdirs();
                } else {
                    file.createNewFile();
                }
            }
            System.out.println(11);
            out = new BufferedOutputStream(new FileOutputStream(file));
            conn = url.openConnection();
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            int numWritten = 0;
            System.out.println(12);
            while ((numRead = in.read(buffer)) != -1) {
                System.out.println(13);
                out.write(buffer, 0, numRead);
                numWritten += numRead;
                upd.setProgress(numWritten);
            }
        } catch (Exception exception) {
            org.opencdspowered.opencds.core.logging.Logger.getInstance().logException(exception);
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
                org.opencdspowered.opencds.core.logging.Logger.getInstance().logException(ioe);
                return false;
            }
        }
        upd.removeListener(this);
        return true;
    }
