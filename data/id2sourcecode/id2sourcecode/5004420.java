    @Override
    public List<String> list(URL url, String path) throws IOException {
        InputStream is = null;
        try {
            List<String> resources = new ArrayList<String>();
            URL jarUrl = findJarForResource(url);
            if (jarUrl != null) {
                is = jarUrl.openStream();
                log.debug("Listing " + url);
                resources = listResources(new JarInputStream(is), path);
            } else {
                List<String> children = new ArrayList<String>();
                try {
                    if (isJar(url)) {
                        is = url.openStream();
                        JarInputStream jarInput = new JarInputStream(is);
                        log.debug("Listing " + url);
                        for (JarEntry entry; (entry = jarInput.getNextJarEntry()) != null; ) {
                            log.debug("Jar entry: " + entry.getName());
                            children.add(entry.getName());
                        }
                    } else {
                        is = url.openStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        List<String> lines = new ArrayList<String>();
                        for (String line; (line = reader.readLine()) != null; ) {
                            log.debug("Reader entry: " + line);
                            lines.add(line);
                            if (getResources(path + "/" + line).isEmpty()) {
                                lines.clear();
                                break;
                            }
                        }
                        if (!lines.isEmpty()) {
                            log.debug("Listing " + url);
                            children.addAll(lines);
                        }
                    }
                } catch (FileNotFoundException e) {
                    if ("file".equals(url.getProtocol())) {
                        File file = new File(url.getFile());
                        log.debug("Listing directory " + file.getAbsolutePath());
                        if (file.isDirectory()) {
                            log.debug("Listing " + url);
                            children = Arrays.asList(file.list());
                        }
                    } else {
                        throw e;
                    }
                }
                String prefix = url.toExternalForm();
                if (!prefix.endsWith("/")) prefix = prefix + "/";
                for (String child : children) {
                    String resourcePath = path + "/" + child;
                    resources.add(resourcePath);
                    URL childUrl = new URL(prefix + child);
                    resources.addAll(list(childUrl, resourcePath));
                }
            }
            return resources;
        } finally {
            try {
                if (is != null) is.close();
            } catch (Exception e) {
            }
        }
    }
