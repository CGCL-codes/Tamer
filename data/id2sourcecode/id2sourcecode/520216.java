    private void retrieveAndSave(String filename1, String filename2) {
        String filename = filename1 + "\\" + filename2 + ".jpg";
        try {
            System.out.print("Processing: " + filename);
            if (conn.getResponseCode() == 404) {
                System.out.println(" 404 Not Found");
                fail++;
            } else if (conn.getResponseCode() != 200) {
                System.out.println(" HTTP code is not 404/200");
                fail++;
            } else {
                InputStream stream = conn.getInputStream();
                FileOutputStream file = new FileOutputStream("G:\\Entertainment\\HumanNature\\mm52\\other\\" + filename);
                int c;
                while ((c = stream.read()) != -1) file.write(c);
                file.close();
                System.out.println(" OK! Complete");
                fail = 0;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(2);
        }
    }
