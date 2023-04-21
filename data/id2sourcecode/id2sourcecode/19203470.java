        public ArrayList<String> getServers(String directoryName) {
            String filesep = System.getProperty("file.separator");
            ArrayList<String> servers = new ArrayList<String>();
            File dir = new File(directoryName);
            String[] children = dir.list();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    if (new File(directoryName + filesep + children[i]).list() != null) {
                        for (String s : new ServerLister().getServers(directoryName + filesep + children[i])) {
                            servers.add(s);
                        }
                    }
                    if (children[i].length() > 9) {
                        String n = children[i].substring(0, children[i].length() - 5);
                        String ext = children[i].substring(children[i].length() - 9);
                        if (ext.equals("Impl.java") && new File(directoryName + filesep + n + ".class").exists()) {
                            Pattern p = Pattern.compile("abstract[\n\t ]+public[\n\t ]+class[\n\t ]+" + n, Pattern.MULTILINE);
                            String fileName = children[i];
                            Pattern patMainMethod = Pattern.compile("public[\n\t ]+static[\n\t ]+void[\n\t ]main", Pattern.MULTILINE);
                            File f = new File(directoryName + filesep + fileName);
                            try {
                                FileInputStream fis = new FileInputStream(f);
                                FileChannel fc = fis.getChannel();
                                ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());
                                Charset cs = Charset.forName("8859_1");
                                CharsetDecoder cd = cs.newDecoder();
                                CharBuffer cb = cd.decode(bb);
                                Matcher m = p.matcher(cb);
                                Matcher m2 = patMainMethod.matcher(cb);
                                boolean keepLooking = false;
                                if (m2.find()) {
                                    keepLooking = true;
                                }
                                if (keepLooking && m.find()) {
                                    keepLooking = false;
                                }
                                if (keepLooking) {
                                    servers.add(directoryName.replace(filesep, ".") + "." + n);
                                }
                            } catch (Exception e) {
                                System.out.println("ServerLister Error: " + e.toString());
                            }
                        }
                    }
                }
            }
            return servers;
        }
