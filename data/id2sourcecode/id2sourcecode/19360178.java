    private void parsePost() throws java.io.EOFException {
        file.pos = file.data.length - 1;
        while (file.read() == 223) {
            file.pos--;
            file.pos--;
        }
        file.pos -= 5;
        file.pos = (int) file.readUnsignedInt();
        Frame1.writelog("This should be 248: " + file.read());
        int prevPage = (int) file.readUnsignedInt();
        file.pos += 20;
        stackDepth = file.readUnsignedShort();
        page = new Page[file.readUnsignedShort()];
        Frame1.writelog(page.length + " pages");
        int head = file.read();
        double size;
        int num;
        String name;
        int l;
        while (head != 249) {
            switch(head) {
                case 138:
                    break;
                case 243:
                    num = file.read();
                    file.pos += 4;
                    size = (0.001 * file.readUnsignedInt() * mag * trueDviDpi / file.readUnsignedInt());
                    l = file.read();
                    l += file.read();
                    name = new String(file.data, file.pos, l);
                    file.pos += l;
                    if ((font[num] == null) || !(font[num].name.equals(name)) || !(font[num].size == size)) {
                        font[num] = null;
                        int i = 0;
                        while (i < 2 && font[num] == null) {
                            String template = context.getProperty("jdvi.font.nameformat", "%name%.%dpi%pk");
                            StringTokenizer tok = new StringTokenizer(template, "%");
                            String fname = "";
                            String tmp = null;
                            while (tok.hasMoreTokens()) {
                                tmp = tok.nextToken();
                                if (tmp.equals("name")) fname = fname + name; else if (tmp.equals("dpi")) fname = fname + ((int) (size) + i); else fname = fname + tmp;
                            }
                            font[num] = jdvi.font.Font.loadFont(fontBase, fname);
                            if (font[num] == null) {
                                if (i == 1) context.inform("Font " + num + ":" + fname + " not found.");
                            } else {
                                context.inform("Loaded font " + num + ":" + fname);
                                font[num].name = name;
                                font[num].size = size;
                                break;
                            }
                            i++;
                        }
                    }
                    break;
            }
            head = file.read();
        }
        int i = page.length - 1;
        textColor = JDviColor.getColor(context, "textColor", Color.black);
        while (prevPage != -1) {
            page[i] = new Page(prevPage, textColor);
            i--;
            file.pos = prevPage + 41;
            prevPage = (int) file.readUnsignedInt();
        }
        for (int j = 0; j < this.page.length; j++) scanPage(j);
    }
