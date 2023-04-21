    private boolean downloadFile(String source, String target) {
        try {
            echoJs("Copy file to target: " + target);
            URL dl = new URL(source);
            ReadableByteChannel rbc = Channels.newChannel(dl.openStream());
            FileOutputStream fos = new FileOutputStream(target);
            fos.getChannel().transferFrom(rbc, 0, 1 << 24);
            fos.close();
            rbc.close();
        } catch (MalformedURLException e) {
            echoJs("URL error 1 " + e.toString());
            return false;
        } catch (IOException e) {
            echoJs("URL error 2 " + e.toString());
            for (int i = 0; i < e.getStackTrace().length; i++) {
                echoJs(e.getStackTrace()[i] + "");
            }
            return false;
        }
        return true;
    }
