            @Override
            public boolean importData(JComponent comp, Transferable t) {
                Cursor oldCursor = comp.getCursor();
                comp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                    for (DataFlavor df : t.getTransferDataFlavors()) {
                        try {
                            if (df.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType)) {
                                Object data = t.getTransferData(df);
                                if (data != null) {
                                    initTree(comp, data);
                                    break;
                                }
                            } else if (df.isFlavorTextType()) {
                                Object data = t.getTransferData(df);
                                if (data instanceof String) {
                                    try {
                                        URL url = new URL((String) data);
                                        InputStream is = url.openStream();
                                        File file = File.createTempFile("__cache", "___");
                                        file.deleteOnExit();
                                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file), 1024 * 10);
                                        byte[] buf = new byte[1024 * 4];
                                        int c = 0;
                                        while ((c = is.read(buf)) > 0) {
                                            os.write(buf, 0, c);
                                        }
                                        is.close();
                                        os.close();
                                        if (loadData(comp, url.toString(), file)) {
                                            _files.clear();
                                            _files.add(file);
                                            break;
                                        }
                                    } catch (Throwable th) {
                                        th.printStackTrace();
                                    }
                                }
                            } else {
                                Object data = t.getTransferData(df);
                                if (data instanceof List && ((List) data).size() > 0) {
                                    List list = (List) data;
                                    Object item = list.get(0);
                                    if (item instanceof File) {
                                        if (loadData(comp, ((File) item).getAbsolutePath(), (File) item)) {
                                            _files.clear();
                                            _files.add((File) item);
                                            break;
                                        }
                                    }
                                }
                            }
                        } catch (UnsupportedFlavorException ufex) {
                        } catch (IOException ioex) {
                        }
                    }
                } finally {
                    comp.setCursor(oldCursor);
                }
                return true;
            }
