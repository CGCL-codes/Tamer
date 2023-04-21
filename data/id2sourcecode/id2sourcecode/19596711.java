    private URL findResource(String resourceName, URL[] search) {
        for (int i = 0; i < search.length; i++) {
            URL currentUrl = search[i];
            if (currentUrl == null) {
                continue;
            }
            try {
                String protocol = currentUrl.getProtocol();
                if (protocol.equals("jar")) {
                    URL jarURL = ((JarURLConnection) currentUrl.openConnection()).getJarFileURL();
                    JarFile jarFile;
                    JarURLConnection juc;
                    try {
                        juc = (JarURLConnection) new URL("jar", "", jarURL.toExternalForm() + "!/").openConnection();
                        jarFile = juc.getJarFile();
                    } catch (IOException e) {
                        search[i] = null;
                        throw e;
                    }
                    try {
                        juc = (JarURLConnection) new URL("jar", "", jarURL.toExternalForm() + "!/").openConnection();
                        jarFile = juc.getJarFile();
                        String entryName;
                        if (currentUrl.getFile().endsWith("!/")) {
                            entryName = resourceName;
                        } else {
                            String file = currentUrl.getFile();
                            int sepIdx = file.lastIndexOf("!/");
                            if (sepIdx == -1) {
                                search[i] = null;
                                continue;
                            }
                            sepIdx += 2;
                            StringBuffer sb = new StringBuffer(file.length() - sepIdx + resourceName.length());
                            sb.append(file.substring(sepIdx));
                            sb.append(resourceName);
                            entryName = sb.toString();
                        }
                        if (entryName.equals("META-INF/") && jarFile.getEntry("META-INF/MANIFEST.MF") != null) {
                            return targetURL(currentUrl, "META-INF/MANIFEST.MF");
                        }
                        if (jarFile.getEntry(entryName) != null) {
                            return targetURL(currentUrl, resourceName);
                        }
                    } finally {
                        if (!juc.getUseCaches()) {
                            try {
                                jarFile.close();
                            } catch (Exception e) {
                            }
                        }
                    }
                } else if (protocol.equals("file")) {
                    String baseFile = currentUrl.getFile();
                    String host = currentUrl.getHost();
                    int hostLength = 0;
                    if (host != null) {
                        hostLength = host.length();
                    }
                    StringBuffer buf = new StringBuffer(2 + hostLength + baseFile.length() + resourceName.length());
                    if (hostLength > 0) {
                        buf.append("//").append(host);
                    }
                    buf.append(baseFile);
                    String fixedResName = resourceName;
                    while (fixedResName.startsWith("/") || fixedResName.startsWith("\\")) {
                        fixedResName = fixedResName.substring(1);
                    }
                    buf.append(fixedResName);
                    String filename = buf.toString();
                    File file = new File(filename);
                    File file2 = new File(URLDecoder.decode(filename));
                    if (file.exists() || file2.exists()) {
                        return targetURL(currentUrl, fixedResName);
                    }
                } else {
                    URL resourceURL = targetURL(currentUrl, resourceName);
                    URLConnection urlConnection = resourceURL.openConnection();
                    try {
                        urlConnection.getInputStream().close();
                    } catch (SecurityException e) {
                        return null;
                    }
                    if (!resourceURL.getProtocol().equals("http")) {
                        return resourceURL;
                    }
                    int code = ((HttpURLConnection) urlConnection).getResponseCode();
                    if (code >= 200 && code < 300) {
                        return resourceURL;
                    }
                }
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            } catch (SecurityException e) {
            }
        }
        return null;
    }
