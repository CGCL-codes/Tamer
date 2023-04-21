    private static void writeFile(String resourcePath, File newProf) {
        byte[] buffer = new byte[4096];
        InputStream is = Profile.class.getResourceAsStream("profiles/" + resourcePath);
        if (is == null) {
            throw new RuntimeException("Could not find resource profiles/" + resourcePath);
        }
        OutputStream os = null;
        try {
            if (newProf.createNewFile()) {
                os = new FileOutputStream(newProf);
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
            } else {
                Logger.logMessage("Could not output " + resourcePath, Logger.ERROR);
            }
        } catch (IOException e) {
            Logger.logMessage("Could not output " + resourcePath + " - " + e, Logger.ERROR);
        } finally {
            if (os != null) try {
                os.close();
            } catch (IOException e) {
            }
            if (is != null) try {
                is.close();
            } catch (IOException e) {
            }
        }
    }
