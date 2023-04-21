        private File scanUrl(String ressourcePath) {
            for (String contentPath : urlContent.keySet()) {
                if (ressourcePath.startsWith(contentPath)) {
                    String path = ressourcePath.substring(contentPath.length(), ressourcePath.length());
                    for (String sourcePath : urlContent.get(contentPath)) {
                        try {
                            URL url = new URL(sourcePath + path);
                            InputStream from = url.openStream();
                            if (LOG.isLoggable(Level.INFO)) {
                                LOG.info("loading " + url.toString());
                            }
                            File file = File.createTempFile("page", "tmp");
                            file.deleteOnExit();
                            FileOutputStream to = new FileOutputStream(file);
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = from.read(buffer)) != -1) {
                                to.write(buffer, 0, bytesRead);
                            }
                            from.close();
                            to.close();
                            return file;
                        } catch (Exception ignore) {
                        }
                    }
                }
            }
            return null;
        }
