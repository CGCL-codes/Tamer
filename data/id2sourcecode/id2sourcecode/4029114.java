        public void installResource(boolean overwrite) throws IOException {
            createOutputDirectory();
            File file = new File(outputDirectory + "/" + resource);
            if (!file.exists() || overwrite) {
                String fullResource = resourcePrefix + resource;
                URL url = ClassLoader.getSystemResource(fullResource);
                if (url == null) {
                    throw new IOException("Resource '" + fullResource + "' not found !");
                }
                InputStream reader = url.openStream();
                if (file.createNewFile()) {
                    FileOutputStream writer = new FileOutputStream(file);
                    int b;
                    while ((b = reader.read()) >= 0) {
                        writer.write(b);
                    }
                    writer.close();
                } else {
                    throw new IOException("file to be created already exists or can not be created ");
                }
            } else {
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finer("File exists, not overwritten");
            }
        }
