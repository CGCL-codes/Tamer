    private void reader(JarFile jar_file, String jar_path) {
        int line = 0;
        for (Enumeration<JarEntry> entries = jar_file.entries(); entries.hasMoreElements(); ) {
            JarEntry je = entries.nextElement();
            String entry = je.toString();
            if (entry.startsWith(jar_path) && !entry.equals(jar_path)) {
                String[] s = entry.split("/");
                String sname = s[s.length - 1];
                if (je.isDirectory()) {
                    Section section = new Section(this, sname);
                    this.add(section);
                    ClassLoader class_loader = this.getClass().getClassLoader();
                    URL index_url = class_loader.getResource(entry + "index");
                    if (index_url != null) try {
                        InputStreamReader isr = new InputStreamReader(index_url.openStream());
                        BufferedReader br = new BufferedReader(isr);
                        String text;
                        while ((text = br.readLine()) != null) {
                            String[] tokens = text.split("[ \t]+");
                            line++;
                            if (tokens.length == 10) {
                                URL page_url = class_loader.getResource(entry + tokens[0]);
                                double atlas_x0 = Double.parseDouble(tokens[1]);
                                double atlas_y0 = Double.parseDouble(tokens[2]);
                                double atlas_x1 = Double.parseDouble(tokens[3]);
                                double atlas_y1 = Double.parseDouble(tokens[4]);
                                double pdf_x0 = Double.parseDouble(tokens[5]);
                                double pdf_y0 = Double.parseDouble(tokens[6]);
                                double pdf_x1 = Double.parseDouble(tokens[7]);
                                double pdf_y1 = Double.parseDouble(tokens[8]);
                                double offset = Double.parseDouble(tokens[9]);
                                Page page = new Page(section, page_url, line, atlas_x0, atlas_y0, atlas_x1, atlas_y1, pdf_x0, pdf_y0, pdf_x1, pdf_y1, offset);
                                section.put(tokens[9], page);
                            }
                        }
                        br.close();
                    } catch (Exception e) {
                        new ErrorEvent().send(e);
                    }
                } else if (sname.equals("space_name")) {
                    ClassLoader class_loader = this.getClass().getClassLoader();
                    URL space_name_url = class_loader.getResource(entry);
                    if (space_name_url != null) {
                        try {
                            InputStreamReader isr = new InputStreamReader(space_name_url.openStream());
                            BufferedReader br = new BufferedReader(isr);
                            this._space_name = br.readLine();
                            br.close();
                        } catch (Exception e) {
                            new ErrorEvent().send(e);
                        }
                    } else new ErrorEvent().send("Unable to open space_name");
                }
            }
        }
    }
