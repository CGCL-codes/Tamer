                public Object run() throws IOException {
                    OutputStream out = null;
                    try {
                        File tmpFile = File.createTempFile("jar_cache", null);
                        tmpFile.deleteOnExit();
                        out = new FileOutputStream(tmpFile);
                        int read = 0;
                        byte[] buf = new byte[BUF_SIZE];
                        while ((read = in.read(buf)) != -1) {
                            out.write(buf, 0, read);
                        }
                        out.close();
                        out = null;
                        return new URLJarFile(tmpFile);
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                    }
                }
