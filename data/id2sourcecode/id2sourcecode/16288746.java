    public static void main(String[] args) {
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        File log = new File(tmpDir, "Installer.log");
        try {
            OutputStream os = new FileOutputStream(log, true);
            PrintWriter pw = new PrintWriter(os, true);
            try {
                pw.println("--------------------------------------------------");
                pw.println("Running installation @ " + new java.util.Date());
                pw.println(" --- arg listing ---");
                for (String arg : args) {
                    pw.println(arg);
                }
                System.getProperties().list(pw);
                File dir = getWritableExtensionDirectory(pw);
                pw.println("Target directory: " + dir.getAbsolutePath());
                InputStream in = Installer.class.getResourceAsStream("/" + JAR_FILE);
                OutputStream out = new FileOutputStream(new File(dir, JAR_FILE));
                int bytesRead;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
            } catch (Throwable thrown) {
                JOptionPane.showMessageDialog(null, thrown.toString());
                thrown.printStackTrace(pw);
            } finally {
                pw.println("--------------------------------------------------");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
