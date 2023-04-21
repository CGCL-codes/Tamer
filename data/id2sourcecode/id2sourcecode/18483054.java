        private static void copySchemaSource(String urlLoc, StscState state, boolean forceCopy) {
            if (state.getSchemasDir() != null) {
                String schemalocation = state.sourceNameForUri(urlLoc);
                File targetFile = new File(state.getSchemasDir(), schemalocation);
                if (forceCopy || !targetFile.exists()) {
                    try {
                        File parentDir = new File(targetFile.getParent());
                        IOUtil.createDir(parentDir, null);
                        InputStream in = null;
                        URL url = new URL(urlLoc);
                        try {
                            in = url.openStream();
                        } catch (FileNotFoundException fnfe) {
                            if (forceCopy && targetFile.exists()) targetFile.delete(); else throw fnfe;
                        }
                        if (in != null) {
                            FileOutputStream out = new FileOutputStream(targetFile);
                            IOUtil.copyCompletely(in, out);
                        }
                    } catch (IOException e) {
                        System.err.println("IO Error " + e);
                    }
                }
            }
        }
