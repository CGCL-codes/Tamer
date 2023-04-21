    @SuppressWarnings("unchecked")
    public static void browse(URI uri, String sysBrowser) throws IOException, UnsupportedOperationException {
        sysBrowser = sysBrowser != null ? sysBrowser.trim() : "";
        if (isValid(sysBrowser)) {
            String quotation = sysBrowser.indexOf(' ') >= 0 ? "\"" : "";
            String command = quotation + sysBrowser + quotation + " " + uri.toString();
            Process child = Runtime.getRuntime().exec(command);
        } else try {
            Class desktopClass = Class.forName("java.awt.Desktop");
            final Method isSupported = desktopClass.getMethod("isDesktopSupported");
            final Method getDesktop = desktopClass.getMethod("getDesktop");
            final Method browse = desktopClass.getMethod("browse", uri.getClass());
            final Boolean supported = (Boolean) isSupported.invoke(null);
            if (supported) {
                Object desktop = getDesktop.invoke(null);
                browse.invoke(desktop, uri);
            } else {
                throw new UnsupportedOperationException();
            }
        } catch (Exception e) {
            String command = isWindowsOS() ? "rundll32 url.dll,FileProtocolHandler " : "firefox ";
            command += uri.toString();
            Process child = Runtime.getRuntime().exec(command);
        }
    }
