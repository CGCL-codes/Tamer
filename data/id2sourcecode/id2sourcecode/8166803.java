    public void getUpdate() {
        this.jProgressBar.setIndeterminate(true);
        this.jProgressBar.setValue(0);
        this.setLocationRelativeTo(this.getOwner());
        this.setVisible(true);
        socket = new FishsSSLSocket();
        socket.setIp(addr);
        socket.setPort(port);
        String pDir;
        try {
            socket.connect();
        } catch (Exception e) {
            jLabel.setText("Error connecting to update server");
            jButton.setText("OK");
        }
        try {
            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            os.writeInt(1);
            String[] ver = Update.class.getPackage().getImplementationVersion().replaceAll("[0-9].+?:", "").split("\\.");
            for (int i = 0; i < ver.length; i++) os.writeInt(Integer.parseInt(ver[i]));
            if (is.readInt() == 0) {
                socket.close();
                return;
            }
            long size = is.readLong();
            System.out.println("Size: " + size);
            jProgressBar.setMaximum((int) (size / (1024)));
            jProgressBar.setIndeterminate(false);
            int part;
            byte[] chunk = new byte[65535];
            long total = 0;
            if ((pDir = System.getProperty("fishs.directory")) == null) pDir = "";
            if (pDir.length() != 0 && !pDir.endsWith("/")) pDir += "/";
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(pDir + "new.jar"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            while (total < size) {
                part = is.readInt();
                System.out.println("Got: " + part);
                is.readFully(chunk, 0, part);
                bos.write(chunk, 0, part);
                md.update(chunk, 0, part);
                total += part;
                System.out.println("Total/Max" + (total / 1024) + "/" + (size / 1024));
                jProgressBar.setValue((int) (total / 1024));
            }
            bos.flush();
            bos.close();
            is.readInt();
            int hSize = is.readInt();
            byte[] hash = new byte[hSize];
            is.read(hash, 0, hSize);
            byte[] digest = md.digest();
            if (!md.isEqual(digest, hash)) {
                System.out.println("Hash didn't match!");
                File f = new File(pDir + "new.jar");
                f.delete();
            } else {
                File f = new File(pDir + "FIShSClient.jar");
                if (!f.delete()) System.exit(0);
                f = new File(pDir + "new.jar");
                f.renameTo(new File(pDir + "FIShSClient.jar"));
            }
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        try {
            String minimize = "";
            String wd = "";
            if (System.getProperty("fishs.minimize") != null || !this.getOwner().isVisible()) minimize = "--minimize";
            if (pDir.length() > 0) wd = "-wd " + pDir;
            String cmd = "java -jar " + pDir + "FIShSClient.jar " + minimize + " " + wd;
            System.out.println(cmd);
            Runtime.getRuntime().exec(cmd);
            System.exit(0);
        } catch (Exception e) {
        }
    }
