    private void urlToFile(URL url, File file) {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        ;
        try {
            URLConnection con = url.openConnection();
            con.connect();
            bis = new BufferedInputStream(con.getInputStream());
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int readBytes;
            while ((readBytes = bis.read(buffer)) > 0) {
                fos.write(buffer, 0, readBytes);
            }
            bis.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error loading tile " + url.toString());
        } catch (IOException e) {
            System.err.println("Error loading tile " + url.toString());
        } finally {
            if (fos != null) try {
                fos.close();
            } catch (IOException e) {
                System.err.println("Error loading tile " + url.toString());
            }
            if (bis != null) try {
                bis.close();
            } catch (IOException e) {
                System.err.println("Error loading tile " + url.toString());
            }
        }
    }
