    private void updateVersion(String version, String jarUrl) {
        System.out.println("There is a new version of OpenRDS available: " + version);
        if (confirm("Do you want to update this node? (Will restart the application)")) {
            System.out.println("Connecting to update server...");
            InputStream in = null;
            OutputStream out = null;
            try {
                final URL url = new URL(URL_BASE + jarUrl);
                final URLConnection con = url.openConnection();
                in = new BufferedInputStream(con.getInputStream());
                System.out.println("Downloading... (" + (con.getContentLength() / 1024) + " KB)");
                final byte[] data = readInput(in);
                System.out.println("Writing file '" + UPDT_FILE + "'.");
                out = new FileOutputStream(UPDT_FILE);
                out.write(data);
                out.close();
                System.out.println("Update process finished!");
                commandRestart();
            } catch (Exception e) {
                final String message = "" + "An error happened while performing the update process.\n" + "Error message: " + e.getMessage() + "\n" + "Error class: " + e.getClass().getName();
                System.out.println(message);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception ignored) {
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception ignored) {
                    }
                }
            }
        } else {
            System.out.println("Operation canceled.");
        }
    }
