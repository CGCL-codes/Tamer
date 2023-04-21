    private boolean getWave(String url) {
        BufferedOutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        try {
            File FF = new File(f.getParent() + "/" + f.getName() + "pron");
            FF.mkdir();
            URL url2 = new URL(url);
            out = new BufferedOutputStream(new FileOutputStream(f.getParent() + "/" + f.getName() + "pron/" + Word + ".wav"));
            conn = url2.openConnection();
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException ioe) {
            }
        }
        return true;
    }
