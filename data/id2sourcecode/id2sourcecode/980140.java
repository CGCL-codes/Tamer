    private static int readShaderSource(Class context, URL url, StringBuffer result, int lineno) {
        try {
            if (DEBUG_CODE) {
                System.err.printf("%3d: // %s\n", lineno, url);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                lineno++;
                if (DEBUG_CODE) {
                    System.err.printf("%3d: %s\n", lineno, line);
                }
                if (line.startsWith("#include ")) {
                    String includeFile = line.substring(9).trim();
                    URL nextURL = null;
                    String next = IOUtil.getRelativeOf(url, includeFile);
                    if (null != next) {
                        nextURL = IOUtil.getResource(context, next);
                    }
                    if (nextURL == null) {
                        nextURL = IOUtil.getResource(context, includeFile);
                    }
                    if (nextURL == null) {
                        throw new FileNotFoundException("Can't find include file " + includeFile);
                    }
                    lineno = readShaderSource(context, nextURL, result, lineno);
                } else {
                    result.append(line + "\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lineno;
    }
