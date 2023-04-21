    private void listModules() {
        if (motdFile != null) {
            try {
                FileInputStream motd = new FileInputStream(motdFile);
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = motd.read(buf)) != -1) out.write(buf, 0, len);
                out.write('\n');
            } catch (IOException ioe) {
                logger.warn("error sending MOTD: " + ioe.getMessage());
            }
        }
        for (Iterator it = modules.values().iterator(); it.hasNext(); ) {
            Module m = (Module) it.next();
            if (m.list) {
                try {
                    StringBuffer name = new StringBuffer(SPACES);
                    name.replace(0, m.name.length(), m.name);
                    Util.writeASCII(out, name + m.comment + "\n");
                } catch (IOException ioe) {
                    logger.warn("error listing module " + m.name + ": " + ioe.getMessage());
                }
            }
        }
    }
