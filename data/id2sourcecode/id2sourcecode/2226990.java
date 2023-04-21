    public static void main(String[] _args) {
        if ((_args.length < 2) || (_args[0].length() != 1)) syntax();
        char command = _args[0].charAt(0);
        String disk = _args[1];
        boolean dirty = false;
        if (command == 'c') {
            if (new File(disk).exists()) {
                System.err.println("Error: the disk already exists");
                System.exit(-2);
            }
            dirty = true;
        } else {
            if (!new File(disk).exists()) {
                System.err.println("Error: can not find the disk");
                System.exit(-3);
            }
            try {
                setInstance(loadDisk(disk));
            } catch (IOException ex) {
                System.err.println("Error: " + ex.getMessage());
                System.exit(-4);
            }
        }
        if (_args.length == 2) {
            String[] p = getInstance().getPaths();
            FuSort.sort(p);
            if (p.length > 0) {
                if (command == 't') tree(new FuFileRam(p[0])); else if (command == 'l') {
                    for (int i = 0; i < p.length; i++) list(new FuFileRam(p[i]));
                }
            } else if (command != 'c') System.out.println("empty disk");
        } else {
            for (int i = 2; i < _args.length; i++) {
                if ((command == 'c') || (command == 'i')) {
                    try {
                        FuFile g = new FuFileFile(_args[i]);
                        FuFile f = new FuFileRam(g.getAbsolutePath());
                        if (g.isDirectory()) {
                            System.err.println("create " + g);
                            f.mkdirs();
                        } else {
                            f.getParentFuFile().mkdirs();
                        }
                        if (g.isFile()) {
                            InputStream in = g.getInputStream();
                            OutputStream out = f.getOutputStream();
                            int c;
                            int n = 0;
                            while ((c = in.read()) != -1) {
                                n++;
                                out.write(c);
                            }
                            in.close();
                            out.close();
                            System.err.println("copy " + g + " [" + n + " octets]");
                        }
                    } catch (IOException ex) {
                        System.err.println("Error: can not create/copy " + _args[i]);
                        System.exit(-6);
                    }
                    dirty = true;
                } else {
                    FuFileRam f = new FuFileRam(_args[i]);
                    if (f.exists()) {
                        if (command == 'e') {
                            f.delete();
                            dirty = true;
                        } else if (command == 't') tree(f); else if (command == 'l') list(f);
                    } else {
                        System.err.println("Error: doesn't exist " + _args[i]);
                        System.exit(-7);
                    }
                }
            }
        }
        if (dirty) {
            try {
                saveDisk(disk, getInstance());
            } catch (IOException ex) {
                System.err.println("Error: " + ex.getMessage());
                System.exit(-5);
            }
        }
        System.exit(0);
    }
