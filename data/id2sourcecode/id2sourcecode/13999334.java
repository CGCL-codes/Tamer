        public void save() throws IOException {
            if (url != null) {
                try {
                    OutputStream stream = null;
                    try {
                        stream = url.openConnection().getOutputStream();
                    } catch (UnknownServiceException ex) {
                        if ("file".equals(url.getProtocol())) {
                            File file = new File(url.toURI());
                            File dir = file.getParentFile();
                            if (!dir.exists()) dir.mkdirs();
                            stream = new FileOutputStream(file);
                        }
                    }
                    if (stream != null) {
                        properties.storeToXML(stream, "Automatically saved by " + Resources.class.getName(), "UTF-8");
                    }
                } catch (IOException ex) {
                    throw ex;
                } catch (Exception ex) {
                    throw new IOException(ex.toString());
                }
            }
        }
