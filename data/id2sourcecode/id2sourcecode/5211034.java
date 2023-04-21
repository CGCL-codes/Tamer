    public void dispose() {
        if (f != null) {
            f.setVisible(false);
        }
        f = null;
        System.out.print("Erasing http-site presence...");
        if (as_ftp) {
            xLinlyn xlin = new xLinlyn(site, user, pw, dir);
            try {
                xlin.erase(file);
                System.out.println("..Success!");
            } catch (Exception e) {
                System.out.println("...failed!");
            }
            System.out.println("Done");
        } else {
            try {
                URL url = new URL(httpdat + "REM:" + phpservname);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String s = "";
                System.out.println("Removing php-entry for:" + phpservname + "\nRESPONSE\n==================\n");
                while ((s = br.readLine()) != null) {
                    System.out.println(s);
                }
                br.close();
            } catch (Exception ez) {
                System.out.println("Error at performing remove-->failed");
            }
            System.out.println("Done w/ php");
        }
        System.exit(0);
    }
