    public static void extractFile(String filename) {
        File file = new File(filename);
        if (!file.canRead()) {
            try {
                file.createNewFile();
                InputStream is = main.class.getResourceAsStream(file.getName());
                BufferedInputStream bis = new BufferedInputStream(is);
                FileOutputStream fos = new FileOutputStream(file);
                int ch;
                while ((ch = bis.read()) != -1) fos.write(ch);
                is.close();
                bis.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
