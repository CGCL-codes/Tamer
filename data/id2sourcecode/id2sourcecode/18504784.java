    private void populateTipsList(String tipRoot, List items) throws IOException {
        try {
            URL url = getClass().getResource(tipRoot);
            if (url.toString().startsWith("jar")) {
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                JarFile jar = conn.getJarFile();
                for (Enumeration e = jar.entries(); e.hasMoreElements(); ) {
                    JarEntry entry = (JarEntry) e.nextElement();
                    String name = entry.getName();
                    if ((!entry.isDirectory()) && name.indexOf("/res/tips/") != -1 && (name.endsWith(".html") || name.endsWith(".html"))) {
                        items.add("/" + name);
                    }
                }
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.indexOf('.') == -1) {
                        populateTipsList(tipRoot + "/" + inputLine, items);
                    } else if ((inputLine.endsWith(".htm")) || inputLine.endsWith(".html")) {
                        items.add(tipRoot + "/" + inputLine);
                    }
                }
                in.close();
            }
        } catch (IOException e) {
            throw e;
        }
    }
