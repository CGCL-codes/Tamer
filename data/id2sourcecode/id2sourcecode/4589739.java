        public MemStickTableModel(File path) {
            if (!path.isDirectory()) {
                Emulator.log.error(path + Resource.get("nodirectory"));
                return;
            }
            programs = path.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    String lower = file.getName().toLowerCase();
                    if (lower.endsWith(".pbp")) return true;
                    if (file.isDirectory() && !file.getName().startsWith("%") && !file.getName().endsWith("%")) {
                        File eboot[] = file.listFiles(new FileFilter() {

                            @Override
                            public boolean accept(File arg0) {
                                return arg0.getName().equalsIgnoreCase("eboot.pbp");
                            }
                        });
                        return eboot.length != 0;
                    }
                    return false;
                }
            });
            icons = new ImageIcon[programs.length];
            pbps = new PBP[programs.length];
            psfs = new PSF[programs.length];
            for (int i = 0; i < programs.length; ++i) {
                try {
                    File metapbp = programs[i];
                    if (programs[i].isDirectory()) {
                        File eboot[] = programs[i].listFiles(new FileFilter() {

                            @Override
                            public boolean accept(File arg0) {
                                return arg0.getName().equalsIgnoreCase("eboot.pbp");
                            }
                        });
                        metapbp = programs[i] = eboot[0];
                        File metadir = new File(programs[i].getParentFile().getParentFile().getPath() + File.separatorChar + "%" + programs[i].getParentFile().getName());
                        if (metadir.exists()) {
                            eboot = metadir.listFiles(new FileFilter() {

                                @Override
                                public boolean accept(File arg0) {
                                    return arg0.getName().equalsIgnoreCase("eboot.pbp");
                                }
                            });
                            if (eboot.length > 0) metapbp = eboot[0];
                        }
                        metadir = new File(programs[i].getParentFile().getParentFile().getPath() + File.separatorChar + programs[i].getParentFile().getName() + "%");
                        if (metadir.exists()) {
                            eboot = metadir.listFiles(new FileFilter() {

                                @Override
                                public boolean accept(File arg0) {
                                    return arg0.getName().equalsIgnoreCase("eboot.pbp");
                                }
                            });
                            if (eboot.length > 0) metapbp = eboot[0];
                        }
                        File[] icon0file = programs[i].getParentFile().listFiles(new FileFilter() {

                            @Override
                            public boolean accept(File arg0) {
                                return arg0.getName().equalsIgnoreCase("icon0.png");
                            }
                        });
                        if (icon0file.length > 0) {
                            icons[i] = new ImageIcon(icon0file[0].getPath());
                        }
                        File[] psffile = programs[i].getParentFile().listFiles(new FileFilter() {

                            @Override
                            public boolean accept(File arg0) {
                                return arg0.getName().equalsIgnoreCase("param.sfo");
                            }
                        });
                        if (psffile.length > 0) {
                            FileChannel roChannel = new RandomAccessFile(psffile[0], "r").getChannel();
                            ByteBuffer readbuffer = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) roChannel.size());
                            psfs[i] = new PSF();
                            psfs[i].read(readbuffer);
                            roChannel.close();
                        }
                    }
                    if (programs[i].getName().toLowerCase().endsWith(".pbp")) {
                        FileChannel roChannel = new RandomAccessFile(metapbp, "r").getChannel();
                        ByteBuffer readbuffer = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) roChannel.size());
                        pbps[i] = new PBP(readbuffer);
                        PSF psf = pbps[i].readPSF(readbuffer);
                        if (psf != null) psfs[i] = psf;
                        if (pbps[i].getSizeIcon0() > 0) {
                            byte[] icon0 = new byte[pbps[i].getSizeIcon0()];
                            readbuffer.position((int) pbps[i].getOffsetIcon0());
                            readbuffer.get(icon0);
                            icons[i] = new ImageIcon(icon0);
                        }
                        roChannel.close();
                    }
                    if (icons[i] == null) icons[i] = new ImageIcon(getClass().getResource("/jpcsp/images/icon0.png"));
                    if (icons[i] != null) {
                        Image image = icons[i].getImage();
                        if (image.getWidth(null) > 144 || image.getHeight(null) > 80) {
                            image = image.getScaledInstance(144, 80, Image.SCALE_SMOOTH);
                            icons[i].setImage(image);
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
