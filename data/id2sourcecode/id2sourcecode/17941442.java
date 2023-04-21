    public static void downloadFile(String adresse, File dest) {
        BufferedReader reader = null;
        FileOutputStream fos = null;
        InputStream in = null;
        try {
            URL url = new URL(adresse);
            URLConnection conn = url.openConnection();
            String FileType = conn.getContentType();
            int FileLenght = conn.getContentLength();
            if (FileLenght == -1) {
                throw new IOException("Fichier non valide.");
            }
            in = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            if (dest == null) {
                String FileName = url.getFile();
                FileName = FileName.substring(FileName.lastIndexOf('/') + 1);
                dest = new File(FileName);
            }
            fos = new FileOutputStream(dest);
            byte[] buff = new byte[1024];
            int l = in.read(buff);
            while (l > 0) {
                fos.write(buff, 0, l);
                l = in.read(buff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
