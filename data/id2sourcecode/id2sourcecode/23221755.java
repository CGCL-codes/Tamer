            @Override
            public boolean accept(File file) {
                try {
                    String urlString = "jar:" + file.toURI().toURL() + "!/tolven-plugin.xml";
                    URL url = null;
                    url = new URL(urlString);
                    return file.isFile() && url.openConnection().getContentLength() != -1;
                } catch (IOException e) {
                    throw new RuntimeException("Could not access manifest for: " + file.getPath(), e);
                }
            }
