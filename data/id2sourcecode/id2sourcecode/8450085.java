    public static void register(URL codeBase) throws MalformedURLException, IOException, ProxoolException {
        Properties properties = new Properties();
        try {
            URL url = new URL(codeBase + CONFIG_FILE_PATH);
            properties.load(url.openStream());
            register(properties);
        } catch (MalformedURLException e) {
            log.fatal(e);
            throw e;
        } catch (IOException e) {
            log.fatal(e);
            throw e;
        } catch (ProxoolException e) {
            log.fatal(e);
            throw e;
        }
    }
