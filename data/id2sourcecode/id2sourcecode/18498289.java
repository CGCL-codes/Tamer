    public void run() {
        System.err.println(PREFIX + "running");
        while (waitForInterval(INTERVAL)) {
            try {
                URLConnection connection;
                URL url = new URL("http://localhost:" + port + "/index.html");
                connection = url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int bytesRead;
                byte[] buffer = new byte[500];
                while ((bytesRead = in.read(buffer)) >= 0) out.write(buffer, 0, bytesRead);
                String data = new String(out.toByteArray());
                notifyAlive();
            } catch (ConnectException ignored) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
