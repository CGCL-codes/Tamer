        public Void run() throws IOException {
            String fname = System.getProperty(configFile);
            if (fname == null) {
                fname = System.getProperty(java_home);
                if (fname == null) throw new Error("Can't find " + java_home + " ??");
                File f = new File(fname, libDir);
                f = new File(f, configFileName);
                fname = f.getCanonicalPath();
            }
            InputStream in = new FileInputStream(fname);
            BufferedInputStream bin = new BufferedInputStream(in);
            Properties props = new Properties();
            try {
                props.load(bin);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                    }
                }
            }
            props.putAll(override);
            String toInstall = props.getProperty(installHandlers, "").trim();
            if (toInstall.length() > 0) {
                String hndls = props.getProperty(handlers, "").trim();
                Set<String> hndlSet = new HashSet<String>();
                StringBuilder hndlList = new StringBuilder();
                for (String cn : parseClassNames(hndls)) if (hndlSet.add(cn)) hndlList.append(cn).append('.');
                for (String cn : parseClassNames(toInstall)) if (hndlSet.add(cn)) hndlList.append(cn).append('.');
                if (hndlList.length() > 0) hndlList.deleteCharAt(hndlList.length() - 1);
                props.setProperty(handlers, hndlList.toString());
            }
            props.remove(installHandlers);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(buf);
            props.list(ps);
            ps.flush();
            ps.close();
            ps = null;
            ByteArrayInputStream inbuf = new ByteArrayInputStream(buf.toByteArray());
            LogManager.getLogManager().readConfiguration(inbuf);
            return null;
        }
