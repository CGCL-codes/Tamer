    private static void download(String filename, String url) {
        File f = new File(filename);
        if (f.exists()) {
            System.out.println("File " + filename + " allready exists, will not download this");
            return;
        }
        System.out.println("Downloading " + filename + " from " + url);
        InputStream in = null;
        FileOutputStream out = null;
        try {
            URL u = new URL(url);
            in = u.openStream();
            out = new FileOutputStream(f);
            byte[] buf = new byte[4 * 1024];
            int bytesRead;
            while ((bytesRead = in.read(buf)) != -1) out.write(buf, 0, bytesRead);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
            }
            if (out != null) try {
                out.close();
            } catch (IOException e) {
            }
        }
    }
