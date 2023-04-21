    private void install(String[] resources) {
        for (String resource : resources) {
            File resourceFile = AppProperties.GetSettingsFilePath(resource);
            if (resourceFile.exists()) continue;
            String jarpath = "/" + resource;
            InputStream is = this.getClass().getResourceAsStream(jarpath);
            if (is == null) {
                System.err.println("Failed to find " + jarpath + " in the JAR file");
                continue;
            }
            try {
                FileOutputStream os = new FileOutputStream(resourceFile);
                byte bytes[] = new byte[2048];
                int read;
                while (true) {
                    read = is.read(bytes);
                    if (read == -1) break;
                    os.write(bytes, 0, read);
                }
                is.close();
                os.close();
                System.out.println("Installed " + resourceFile + " onto the local disk from JAR file");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to install " + resourceFile);
            }
        }
    }
