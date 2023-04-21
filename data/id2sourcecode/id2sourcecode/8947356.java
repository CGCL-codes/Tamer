    private void initHandler() {
        if (handler == null) {
            try {
                String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(WAJAS_SERVER_HANDLER_FILE);
                URL url = ResourceUtils.getURL(resolvedLocation);
                Properties props = new Properties();
                props.load(url.openStream());
                String clazzName = props.getProperty(WAJAS_SERVER_HANDLER_PROPERTY_KEY);
                if (clazzName != null) {
                    handler = (WajasServerHandler) Class.forName(clazzName).newInstance();
                }
            } catch (FileNotFoundException fnfe) {
            } catch (Exception e) {
                System.out.println("Load wajasServerHandler error.");
                e.printStackTrace();
            }
            if (handler == null) handler = new WajasServerHandlerSupport();
        }
    }
