    public static ArrayList<String> getPackagePreLoadClasses(java.lang.Package l_package) {
        DataInputStream dataIn = null;
        ArrayList<String> result = new ArrayList<String>();
        try {
            String pckgname = l_package.getName();
            String name = new String(pckgname);
            if (!name.startsWith("/")) {
                name = "/" + name;
            }
            name = name.replace('.', '/');
            name = name + "/PreLoadClasses.txt";
            URL url = Launcher.class.getResource(name);
            URLConnection conn = url.openConnection();
            dataIn = new DataInputStream(conn.getInputStream());
            String readLine = dataIn.readLine();
            while (null != readLine) {
                if (false == readLine.startsWith("#") && false == readLine.equals("")) {
                    result.add(readLine);
                }
                readLine = dataIn.readLine();
            }
        } catch (Exception ex) {
            return null;
        } finally {
            try {
                dataIn.close();
            } catch (IOException ex) {
            }
        }
        return result;
    }
