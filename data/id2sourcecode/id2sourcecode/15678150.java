    private void downloadFiles(ArrayList<String[]> fileList, ActionResponse resp) {
        String dir = System.getProperty("user.dir");
        File f = new File(dir);
        System.out.println("Installation directory: " + f.getAbsolutePath());
        try {
            for (String[] line : fileList) {
                System.out.println("line[1]" + line[1]);
                URL url = new URL(line[1]);
                System.out.println("URL:" + url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                int status = conn.getResponseCode();
                if (status == 200) {
                    InputStream in = conn.getInputStream();
                    File outfile = new File(line[0] + ".dnld");
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outfile));
                    System.out.println("Writing to file..." + outfile.getAbsolutePath());
                    int bytes = 0;
                    while (true) {
                        int c = in.read();
                        if (c < 0) break;
                        out.write(c);
                        bytes++;
                    }
                    out.close();
                    String local = getMD5Sum(outfile);
                    System.out.println("Bytes written: " + bytes);
                    if (local.equals(line[2])) {
                        System.out.println("MD5Sums match...");
                    } else {
                        resp.setErrorCode(ActionResponse.GENERAL_ERROR);
                        resp.setErrorMessage("Unable to validate all files. Please upgrade manually.");
                        break;
                    }
                } else {
                    resp.setErrorCode(ActionResponse.GENERAL_ERROR);
                    resp.setErrorMessage("HTTP Error [" + status + "]");
                    break;
                }
            }
        } catch (Exception e) {
            resp.setErrorCode(ActionResponse.GENERAL_ERROR);
            resp.setErrorMessage("Exception occured: " + e.getMessage());
        }
    }
