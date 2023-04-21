    private void returnImage() {
        FileOutputStream fos;
        InputStream is;
        Resource res;
        String dname;
        String name;
        String path;
        String time;
        Image image;
        byte[] buffer;
        File dir;
        int startOffset;
        int endOffset;
        int read;
        try {
            res = Mobility.getContext().getResource();
            is = res.getInputStream(AgentStructure.PATH_MUTABLE + "secret/" + context_.get(PROP_IMAGE_NAME));
            image = Images.newImage(is);
            name = context_.get(PROP_IMAGE_NAME);
            is.close();
            returnToController(image, name);
            dname = Mobility.getContext().getCard().getCertificate().getSubjectDN().getName();
            startOffset = dname.indexOf("CN=");
            if (startOffset != -1) {
                endOffset = dname.indexOf(",", startOffset);
                if (endOffset == -1) {
                    dname = dname.substring(startOffset + 3);
                } else {
                    dname = dname.substring(startOffset + 3, endOffset);
                }
                dname = dname.replace(' ', '_');
            }
            time = DateString.get();
            path = System.getProperty("user.home") + File.separator + dname + File.separator + CBRConfig.getProperty("cbr.fetchagent.dir");
            dir = new File(path);
            if (!dir.isDirectory()) {
                System.out.println("Creating " + path);
                dir.mkdirs();
            }
            path += File.separator + time + "-" + name;
            System.out.print("Autosaving image to " + path + "...");
            fos = new FileOutputStream(path);
            buffer = new byte[1000];
            is = res.getInputStream(AgentStructure.PATH_MUTABLE + "secret/" + context_.get(PROP_IMAGE_NAME));
            while ((read = is.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            is.close();
            fos.close();
            System.out.println("Done!");
            path = System.getProperty("user.home") + File.separator + dname + File.separator + CBRConfig.getProperty("cbr.receipts.dir");
            dir = new File(path);
            if (!dir.isDirectory()) {
                System.out.println("Creating " + path);
                dir.mkdirs();
            }
            path += File.separator + time + "-Receipt.rcp";
            System.out.print("Autosaving receipt to " + path + "...");
            fos = new FileOutputStream(path);
            buffer = new byte[1000];
            is = res.getInputStream(AgentStructure.PATH_MUTABLE + RECEIPT_FILE);
            while ((read = is.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            is.close();
            fos.close();
            System.out.println("Done!");
        } catch (Exception e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }
