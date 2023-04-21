    public String installCode(String id, String codeStoreLocation) throws DynasoarExceptionType {
        System.out.println("TomcatWarInstaller.installCode, id = " + id);
        try {
            FileOutputStream out = null;
            uk.ac.ncl.neresc.dynasoar.client.codestore.messages.ServiceCodeType serviceCodeType = null;
            boolean overwriteWarFile = hpConfigValues.getTomcatWarConfig().isOverwriteWars();
            System.out.println("TomcatWarInstaller.installCode, overwriteWarFile = " + overwriteWarFile);
            System.out.println("id = " + id);
            String fileData = null;
            try {
                CodeStoreClient codeStoreClient = new CodeStoreClient(codeStoreLocation);
                String location = codeStoreClient.getServiceCodeLocation(id);
                System.out.println("location = " + location);
                CodeStoreService css = new CodeStoreServiceLocator();
                CodeStorePortType cspt = css.getCodeStoreService(new URL(codeStoreLocation));
                System.out.println("TomcatWarInstaller.installCode, codeStoreLocation = " + new URL(codeStoreLocation));
                serviceCodeType = cspt.getServiceCode(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (serviceCodeType == null) {
                DynasoarExceptionType exception = new DynasoarExceptionType();
                exception.setDescription("no service for id " + id);
                throw exception;
            }
            fileData = serviceCodeType.getCodeStoreEndpoint().toString();
            if (fileData.equals("ERROR")) {
                DynasoarExceptionType exception = new DynasoarExceptionType();
                exception.setDescription("file not found");
                throw exception;
            }
            String name = id;
            String filepath = fileData;
            String[] splitString = filepath.split("/");
            String filename = splitString[splitString.length - 1];
            int fileNameLength = filename.length();
            warname = filename.substring(0, fileNameLength - 4);
            System.out.println("TomcatWarInstaller.installCode, warname = " + warname);
            String filepath2 = warDesination + File.separator + filename;
            ret = "http://" + containerAddress + "/" + warname + "/services/" + warname;
            System.out.println("TomcatWarInstaller.installCode, filepath2 = " + filepath2);
            System.out.println("TomcatWarInstaller.installCode, ret = " + ret);
            System.out.println("TomcatWarInstaller.installCode, filepath = " + filepath);
            boolean warExisits = new File(filepath2).exists();
            boolean webAppExists = true;
            try {
                String webAppName = filepath2.substring(0, (filepath2.length() - 4));
                System.out.println("TomcatWarInstaller.installCode, webAppName = " + webAppName);
                webAppExists = new File(webAppName).isDirectory();
            } catch (Exception e) {
                webAppExists = false;
            }
            System.out.println("TomcatWarInstaller.installCode, webAppExists = " + webAppExists);
            if (!webAppExists) {
                URL url = new URL(filepath);
                File targetFile = new File(filepath2);
                if (!targetFile.exists()) targetFile.createNewFile();
                InputStream in = null;
                try {
                    in = url.openStream();
                    out = new FileOutputStream(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DynasoarExceptionType(0, null, "couldn't open stream due to: " + e.getMessage());
                }
                URLConnection con = url.openConnection();
                int fileLength = con.getContentLength();
                ReadableByteChannel channelIn = Channels.newChannel(in);
                FileChannel channelOut = out.getChannel();
                channelOut.transferFrom(channelIn, 0, fileLength);
                channelIn.close();
                channelOut.close();
                out.flush();
                out.close();
                in.close();
                long time = System.currentTimeMillis();
                check(ret, time, STARTCONTROL);
            }
            return (ret);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DynasoarExceptionType(0, null, e.getMessage());
        }
    }
