    private void removeOldAllowedMethods() {
        String filename = pogo.projectFiles.getServerClass();
        try {
            PogoString readcode = new PogoString(PogoUtil.readFile(filename));
            for (int i = 0; i < pogo.commands.size(); i++) {
                Cmd cmd = pogo.commands.cmdAt(i);
                if (cmd.virtual_method) continue;
                String patern = cmd.cmd_class + "::is_allowed";
                int start = readcode.indexOf(patern);
                if (start < 0) continue;
                start = readcode.lastIndexOf("}", start);
                int end = readcode.inMethod(start);
                end = readcode.outMethod(end);
                end = readcode.nextCr(end);
                end = readcode.indexOf("//", end);
                start = readcode.lastIndexOf("//+-------", start);
                String method = readcode.substring(start, end);
                readcode.remove(method);
            }
            PogoUtil.writeFile(filename, readcode.str);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
