    public String[] getParamNames(Class cl, String methodName, String[] paramTypes) throws MdnException {
        LOGGER.debug("Getting Parameter Names of Method '" + methodName + "'...");
        StringBuffer regex = new StringBuffer(".*");
        regex.append(methodName);
        regex.append("\\s*\\(");
        for (String param : paramTypes) {
            regex.append("\\s*");
            regex.append(param.replace("[", "\\[").replace("]", "\\]"));
            regex.append(" ([_\\w]+)\\s*,");
        }
        String[] paramNames = new String[paramTypes.length];
        for (int i = 0; i < paramNames.length; ++i) paramNames[i] = paramTypes[i];
        regex.delete(regex.length() - 1, regex.length());
        regex.append(".*");
        LOGGER.debug("Regex: " + regex + "<");
        Pattern pattern = Pattern.compile(regex.toString(), Pattern.MULTILINE | Pattern.DOTALL);
        String javaFile = cl.getName().replaceAll("\\.", "/") + ".java";
        LOGGER.debug("Javafile: " + javaFile);
        File file = new File(m_jarLoader.getURLs()[0].getFile() + "!/" + javaFile);
        InputStream in = null;
        try {
            URL url = file.toURI().toURL();
            url = new URL("jar", url.getHost(), url.toString());
            URLConnection conn = url.openConnection();
            conn.setDefaultUseCaches(false);
            in = url.openStream();
        } catch (IOException e) {
            return paramNames;
        }
        StringBuffer content = new StringBuffer();
        if (in != null) {
            try {
                byte[] buf = new byte[0xFFFF];
                int len;
                while ((len = in.read(buf)) > 0) {
                    content.append(new String(buf, 0, len));
                }
                in.close();
            } catch (IOException e) {
                throw new MdnException(Config.getProp("error.IOException") + Config.getProp("error.noSuchFileInWSJarfile"), e);
            }
            Matcher matcher = pattern.matcher(content.toString());
            if (matcher.matches()) {
                for (int i = 0; i < paramNames.length; ++i) {
                    paramNames[i] = matcher.group(i + 1);
                    LOGGER.debug("Parameter name: " + matcher.group(i + 1));
                }
                return paramNames;
            }
        }
        return paramNames;
    }
