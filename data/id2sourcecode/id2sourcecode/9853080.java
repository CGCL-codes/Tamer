    public static String readRegistry(String location, String key) {
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            try {
                Process p = Runtime.getRuntime().exec("reg query \"" + location + "\" /v \"" + key + "\"");
                final InputStream is = p.getInputStream();
                final StringWriter sw = new StringWriter();
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            int read;
                            while ((read = is.read()) != -1) {
                                sw.write(read);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                t.start();
                p.waitFor();
                t.join();
                String output = sw.toString();
                if (output.indexOf(REG_STRING) < 0) {
                    return "";
                }
                output = output.substring(output.indexOf(REG_STRING) + REG_STRING.length()).trim();
                return output.replaceAll("\\\\\\\\", "\\\\");
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }
