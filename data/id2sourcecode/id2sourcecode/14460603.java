    public static boolean downloadBinary(String filepath, String fileurl) {
        try {
            File file = new File(filepath);
            if (file.isFile()) {
                if (!file.delete()) {
                    log("Konnte Datei nicht löschen " + file);
                    return false;
                }
            }
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            int i = fileurl.lastIndexOf('/');
            fileurl = fileurl.substring(0, i + 1) + fileurl.substring(i + 1).replaceAll(" ", "%20");
            URL url = new URL(fileurl);
            URLConnection con = url.openConnection();
            BufferedInputStream input = new BufferedInputStream(con.getInputStream());
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file, true));
            byte[] b = new byte[1024];
            int len;
            while ((len = input.read(b)) != -1) {
                output.write(b, 0, len);
            }
            output.close();
            input.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
