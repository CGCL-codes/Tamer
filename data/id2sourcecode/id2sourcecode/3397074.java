    public static void saveLocal(String url) {
        try {
            String sourceName = new URL(url).getPath();
            int slash = sourceName.lastIndexOf("/");
            if (slash > 0) sourceName = sourceName.substring(slash + 1);
            InputStream is = new URL(url).openStream();
            OutputStream os = new FileOutputStream(getNextFreeFile(System.getProperty("user.home") + "/Vis_" + sourceName));
            byte[] buffer = new byte[0x4000];
            int bytesRead;
            while (true) {
                bytesRead = is.read(buffer);
                if (bytesRead <= 0) break;
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            System.out.println(url + " saved.");
        } catch (Exception e) {
            System.out.println("VisHUD.saveLocal:" + e);
        }
    }
