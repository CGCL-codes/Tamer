    private static Object readFileOrUrl(String path, boolean convertToString) {
        URL url = null;
        if (path.indexOf(':') >= 2) {
            try {
                url = new URL(path);
            } catch (MalformedURLException ex) {
            }
        }
        InputStream is = null;
        int capacityHint = 0;
        if (url == null) {
            File file = new File(path);
            capacityHint = (int) file.length();
            try {
                is = new FileInputStream(file);
            } catch (IOException ex) {
                Context.reportError(ToolErrorReporter.getMessage("msg.couldnt.open", path));
                return null;
            }
        } else {
            try {
                URLConnection uc = url.openConnection();
                is = uc.getInputStream();
                capacityHint = uc.getContentLength();
                if (capacityHint > (1 << 20)) {
                    capacityHint = -1;
                }
            } catch (IOException ex) {
                Context.reportError(ToolErrorReporter.getMessage("msg.couldnt.open.url", url.toString(), ex.toString()));
                return null;
            }
        }
        if (capacityHint <= 0) {
            capacityHint = 4096;
        }
        byte[] data;
        try {
            try {
                data = Kit.readStream(is, capacityHint);
            } finally {
                is.close();
            }
        } catch (IOException ex) {
            Context.reportError(ex.toString());
            return null;
        }
        Object result;
        if (!convertToString) {
            result = data;
        } else {
            result = new String(data);
        }
        return result;
    }
