    private void retrieveAndSave(String filename) {
        try {
            if (conn.getResponseCode() == 404) {
                System.out.println("404: " + filename);
                log404.write(filename + "\n");
                return;
            } else if (conn.getResponseCode() != 200) {
                System.out.println("HTTP code is: " + conn.getResponseCode());
                return;
            } else {
                InputStream stream = conn.getInputStream();
                FileOutputStream file = new FileOutputStream(filename);
                int c;
                while ((c = stream.read()) != -1) file.write(c);
                System.out.println("Process " + filename);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(2);
        }
    }
