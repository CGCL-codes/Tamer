    public static Item save(Event event, Item item) throws Event, IOException {
        String type = event.query().header("content-type");
        String boundary = "--" + unquote(type.substring(type.indexOf("boundary=") + 9));
        Input in = event.input();
        String line = in.line();
        while (line != null) {
            if (line.equals(boundary + "--")) {
                Sprout.redirect(event, "/");
            }
            if (line.equals(boundary)) {
                line = in.line();
                while (line != null && !line.equals("")) {
                    int colon = line.indexOf(":");
                    if (colon > -1) {
                        String name = line.substring(0, colon).toLowerCase();
                        String value = line.substring(colon + 1).trim();
                        if (name.equals("content-disposition")) {
                            item.name = unpath(unquote(value.substring(value.indexOf("filename=") + 9).trim()));
                        }
                        if (name.equals("content-type")) {
                            item.type = value;
                        }
                    }
                    line = in.line();
                }
                if (item.name == null || item.name.length() == 0) {
                    Sprout.redirect(event, "/");
                }
                java.io.File path = new java.io.File(Sprout.ROOT + "/" + item.path);
                if (!path.exists()) {
                    path.mkdirs();
                }
                item.file = new java.io.File(Sprout.ROOT + "/" + item.path + "/" + item.name);
                FileOutputStream out = new FileOutputStream(item.file);
                Boundary bound = new Boundary();
                bound.value = ("\r\n" + boundary).getBytes();
                byte[] data = new byte[SIZE];
                int read = in.read(data);
                while (read > -1) {
                    try {
                        out.write(data, 0, bound.find(read, data, out));
                    } catch (Boundary.EOB eob) {
                        out.write(data, 0, eob.index);
                        out.flush();
                        out.close();
                        return item;
                    }
                    read = in.read(data);
                }
                throw new IOException("Boundary not found. (trailing)");
            }
            line = in.line();
        }
        throw new IOException("Boundary not found. (initing)");
    }
