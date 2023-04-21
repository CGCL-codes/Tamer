            public void run() {
                try {
                    if (group != null && url != null) {
                        String url_string = url.getPath();
                        Pattern p0 = Pattern.compile("%20");
                        Matcher m0 = p0.matcher(url_string);
                        url_string = m0.replaceAll(" ");
                        File io_file = new File(url_string);
                        if (!url.sameFile(io_file.toURL()) && group.getLocation() != null) {
                            String location = group.getLocation().toString();
                            File directory = new File(location);
                            File existing = new File(url.getFile());
                            String url_file_name = existing.getName();
                            p0 = Pattern.compile("%20");
                            m0 = p0.matcher(url_file_name);
                            url_file_name = m0.replaceAll(" ");
                            io_file = new File(directory, url_file_name);
                            if (!io_file.exists()) {
                                BufferedInputStream input = new BufferedInputStream(url.openStream());
                                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(io_file));
                                byte[] bytes = new byte[4096];
                                while (true) {
                                    int read = input.read(bytes);
                                    if (read < 0) break;
                                    out.write(bytes, 0, read);
                                }
                                input.close();
                                out.flush();
                                out.close();
                            }
                        }
                        LibraryButtonActions.addFiles(new File[] { io_file }, group);
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
