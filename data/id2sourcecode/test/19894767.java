    private static Properties getMessageBundle(URL url, Locale locale) {
        Properties prop = new Properties();
        String variant = locale.getVariant();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream in = url.openStream();
            int buf;
            while ((buf = in.read()) != -1) {
                if (buf == 10) {
                    byte[] b = bos.toByteArray();
                    bos.reset();
                    String line = null;
                    if (variant == null || variant.equals("")) line = new String(b); else line = new String(b, variant);
                    if (line.trim().length() == 0 || line.charAt(0) == '#') continue;
                    int i0 = line.indexOf('=');
                    if (i0 != -1) {
                        String key = line.substring(0, i0).trim();
                        String value = line.substring(i0 + 1).trim();
                        value = StringUtil.replaceString(value, "\\n", "\n");
                        prop.setProperty(key, value);
                    }
                } else bos.write(buf);
            }
            in.close();
            return prop;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
