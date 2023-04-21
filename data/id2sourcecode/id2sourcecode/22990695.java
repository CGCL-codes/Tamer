            protected boolean verify(String s) {
                boolean result = true;
                try {
                    java.net.URL url = new java.net.URL(s + "/allclasses-frame.html");
                    java.io.InputStream urls = url.openStream();
                    java.io.InputStreamReader is = null;
                    java.io.BufferedReader br = null;
                    try {
                        is = new java.io.InputStreamReader(urls);
                        br = new java.io.BufferedReader(is);
                        String line = br.readLine();
                        if (line == null) {
                            result = false;
                        }
                    } finally {
                        if (br != null) {
                            br.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                        if (urls != null) {
                            urls.close();
                        }
                    }
                } catch (java.io.IOException ioe) {
                    result = false;
                }
                if (!result) {
                    JOptionPane.showMessageDialog(ConfigFrame.this, "Could not find the Javadoc at the URL\n" + s, "Error Adding Javadoc", JOptionPane.ERROR_MESSAGE);
                }
                return result;
            }
