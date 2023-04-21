    public Class<?> loadFile(File fd) {
        try {
            FileInputStream is = new FileInputStream(fd);
            int sz = (int) is.getChannel().size();
            byte[] Data = new byte[sz];
            is.read(Data);
            is.close();
            Class<?> nClass = loadClassData("fireteam.orb.server.processors." + fd.getName().replaceAll(".class", ""), Data);
            g_classes.put(nClass.getName(), nClass);
            return nClass;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
