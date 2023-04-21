    public static void generateFile(String text) {
        try {
            String dir = ("" + text.charAt(0)).toUpperCase();
            File file = new File(dir, text + ".mp3");
            if (file.exists()) {
                return;
            }
            String speechUrl = GoogleTextAPI.getSpeechUrl(text);
            URL url = new URL(speechUrl);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT)");
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            int i;
            while ((i = is.read()) != -1) {
                fos.write(i);
            }
            fos.flush();
            fos.close();
            is.close();
            System.out.println("file:" + file.getName() + " created!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
