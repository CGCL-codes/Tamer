    private void retrieveAndSave(String filename1, String filename2) {
        String filename = "j" + filename1 + "-" + filename2 + ".jpg";
        try {
            if (conn.getResponseCode() == 404) {
                System.out.println("404: " + filename);
                log404.write(filename + "\n");
                return;
            } else if (conn.getResponseCode() != 200) {
                System.out.println("HTTP code is not 404/200");
                log404.write("xx: " + filename + "\n");
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
