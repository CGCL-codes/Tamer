    public void saveConfigFile(Properties props) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            props.store(baos, "Generated config file");
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), "ISO-8859-1"));
            List l = new ArrayList();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")) l.add(line);
            }
            br.close();
            br = new BufferedReader(new InputStreamReader(new FileInputStream(System.getProperty("jnewsgate.configfile", "jnewsgate.conf")), "ISO-8859-1"));
            List l2 = new ArrayList();
            while ((line = br.readLine()) != null) {
                if (!line.trim().startsWith("#") && line.indexOf("=") != -1) {
                    boolean found = false;
                    String pfx = line.substring(0, line.indexOf("=") + 1);
                    for (int i = 0; i < l.size(); i++) {
                        if (((String) l.get(i)).startsWith(pfx)) {
                            line = (String) l.get(i);
                            l.remove(i);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Not found: " + line);
                        line = "#" + line;
                    }
                }
                l2.add(line);
            }
            if (l.size() > 0) {
                l2.add("");
                l2.add("## New options:");
                l2.addAll(l);
            }
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("jnewsgate.configfile", "jnewsgate.conf")), "ISO-8859-1"));
            for (int i = 0; i < l2.size(); i++) {
                out.write((String) l2.get(i));
                out.newLine();
            }
            out.flush();
            out.close();
        } catch (IOException ex) {
            l.log(Level.SEVERE, "IOException while saving config file", ex);
        }
    }
