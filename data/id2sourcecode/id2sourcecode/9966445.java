    public static Element resolveXLink(String uri) throws IOException, JDOMException, CacheException {
        cleanFailures();
        JeevesJCS xlinkCache = JeevesJCS.getInstance(XLINK_JCS);
        Element remoteFragment = (Element) xlinkCache.get(uri);
        if (remoteFragment == null) {
            URL url = new URL(uri.replaceAll("&amp;", "&"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000);
            try {
                BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                try {
                    remoteFragment = Xml.loadStream(in);
                    if (conn.getResponseCode() >= 400) {
                        remoteFragment = null;
                    } else if (remoteFragment != null && remoteFragment.getChildren().size() > 0) {
                        xlinkCache.put(uri, remoteFragment);
                    }
                } finally {
                    in.close();
                }
            } catch (IOException e) {
                synchronized (Processor.class) {
                    failures.add(new Failure(uri, e, System.currentTimeMillis()));
                    if (failures.size() > MAX_FAILURES) {
                        StringBuilder builder = new StringBuilder("There have been " + failures.size() + " timeouts resolving xlinks in the last " + ELAPSE_TIME + " ms\n");
                        for (Failure failure : failures) {
                            if (LOGGER.isDebugEnabled()) {
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                PrintWriter writer = new PrintWriter(out);
                                failure.t.printStackTrace(writer);
                                writer.close();
                                builder.append('\n').append(failure.uri).append(" -> ").append(out).append("\n================");
                            } else {
                                builder.append('\n').append(failure.uri).append(" -> " + failure.t.getMessage());
                            }
                        }
                        builder.append('\n');
                        failures.clear();
                        throw new RuntimeException(builder.toString(), e);
                    }
                }
            }
            if (LOGGER.isDebugEnabled()) LOGGER.debug("cache miss for " + uri);
        }
        if (remoteFragment == null) {
            return new Element("ERROR").setText("Error resolving element: " + uri);
        } else {
            return (Element) remoteFragment.clone();
        }
    }
