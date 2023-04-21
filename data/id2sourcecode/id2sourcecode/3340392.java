    public static InputStream postStream(URL url, String content) throws FileNotFoundException, IOException {
        content = content.replace('\r', ' ').replace('\n', ' ').replace('\t', ' ');
        content = content.trim();
        StringBuffer buf = new StringBuffer(content);
        for (int i = 0; i < buf.length(); i++) {
            if (buf.charAt(i) < '\n') {
                buf.setCharAt(i, ' ');
            }
        }
        content = buf.toString().trim();
        try {
            URLConnection uc = url.openConnection();
            uc.setRequestProperty("USER_AGENT", "HTTP Client");
            uc.setDoOutput(true);
            uc.setDoInput(true);
            uc.setUseCaches(false);
            uc.setAllowUserInteraction(true);
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream dos = new DataOutputStream(uc.getOutputStream());
            dos.writeBytes(content);
            dos.close();
            return uc.getInputStream();
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }
