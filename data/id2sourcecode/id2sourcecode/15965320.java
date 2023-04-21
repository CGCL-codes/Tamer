    public void transferJarFiles() throws IOException {
        HashSet<GridNode> nodes = (HashSet) batchTask.returnNodeCollection();
        String directory = "../repast.simphony.distributedBatch/transferFiles/";
        InetAddress addLocal = InetAddress.getLocalHost();
        String hostnameLocal = addLocal.getHostName();
        File dir = new File(directory);
        String[] children = dir.list();
        Iterator<GridNode> ic = nodes.iterator();
        while (ic.hasNext()) {
            GridNode node = ic.next();
            String address = node.getPhysicalAddress();
            InetAddress addr = InetAddress.getByName(address);
            if (addr.getHostName().equals(hostnameLocal)) continue;
            byte[] rawAddr = addr.getAddress();
            Map<String, String> attributes = node.getAttributes();
            InetAddress hostname = InetAddress.getByAddress(rawAddr);
            String gridPath = attributes.get("GRIDGAIN_HOME");
            FtpClient ftp = new FtpClient();
            ftp.setConnectTimeout(3000);
            try {
                ftp.openServer(hostname.getHostName());
                String[] usernamePass = inputNodes.get(hostname.getHostName());
                ftp.login(usernamePass[0], usernamePass[1]);
                ftp.binary();
                ftp.cd(gridPath + "/libs/ext/");
                for (int i = 0; i < children.length; i++) {
                    if (children[i].equals(".svn")) continue;
                    File file = new File(directory + children[i]);
                    System.out.println(children[i]);
                    OutputStream out = ftp.put(file.getName());
                    InputStream in = new FileInputStream(file);
                    byte c[] = new byte[4096];
                    int read = 0;
                    while ((read = in.read(c)) != -1) {
                        out.write(c, 0, read);
                    }
                    in.close();
                    out.close();
                }
                ftp.closeServer();
            } catch (Exception e) {
                MessageCenter.getMessageCenter(BatchMainSetup.class).error("Problems with the FTP connection." + "A file has not been succesfully transfered", e);
                e.printStackTrace();
            }
        }
    }
