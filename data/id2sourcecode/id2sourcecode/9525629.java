    String InputCommand() {
        int size = Shell.CMD_MAXLINE - 2;
        byte[] c = new byte[1];
        IntRef n = new IntRef(1);
        int str_len = 0;
        int str_index = 0;
        IntRef len = new IntRef(0);
        boolean current_hist = false;
        byte[] line = new byte[Shell.CMD_MAXLINE];
        int it_history = 0;
        int it_completion = 0;
        while (size != 0) {
            Dos.dos.echo = false;
            while (!Dos_files.DOS_ReadFile(input_handle, c, n)) {
                IntRef dummy = new IntRef(0);
                Dos_files.DOS_CloseFile(input_handle);
                Dos_files.DOS_OpenFile("con", 2, dummy);
                Log.log(LogTypes.LOG_MISC, LogSeverities.LOG_ERROR, "Reopening the input handle.This is a bug!");
            }
            if (n.value == 0) {
                size = 0;
                continue;
            }
            switch(c[0]) {
                case 0x00:
                    {
                        Dos_files.DOS_ReadFile(input_handle, c, n);
                        switch(c[0]) {
                            case 0x3d:
                                if (l_history.size() == 0) break;
                                it_history = 0;
                                if (l_history.size() > 0 && ((String) l_history.firstElement()).length() > str_len) {
                                    String reader = ((String) l_history.firstElement()).substring(str_len);
                                    for (int i = 0; i < reader.length(); i++) {
                                        c[0] = (byte) reader.charAt(0);
                                        line[str_index++] = (byte) reader.charAt(0);
                                        Dos_files.DOS_WriteFile(Dos_files.STDOUT, c, n);
                                    }
                                    str_len = str_index = ((String) l_history.firstElement()).length();
                                    size = Shell.CMD_MAXLINE - str_index - 2;
                                    line[str_len] = 0;
                                }
                                break;
                            case 0x4B:
                                if (str_index != 0) {
                                    outc(8);
                                    str_index--;
                                }
                                break;
                            case 0x4D:
                                if (str_index < str_len) {
                                    outc(line[str_index++]);
                                }
                                break;
                            case 0x47:
                                while (str_index != 0) {
                                    outc(8);
                                    str_index--;
                                }
                                break;
                            case 0x4F:
                                while (str_index < str_len) {
                                    outc(line[str_index++]);
                                }
                                break;
                            case 0x48:
                                if (l_history.size() == 0 || it_history == l_history.size()) break;
                                if (it_history == 0 && !current_hist) {
                                    current_hist = true;
                                    l_history.insertElementAt(new String(line, 0, str_len), 0);
                                    it_history++;
                                }
                                for (; str_index > 0; str_index--) {
                                    outc(8);
                                    outc(' ');
                                    outc(8);
                                }
                                StringHelper.strcpy(line, (String) l_history.elementAt(it_history));
                                len.value = ((String) l_history.elementAt(it_history)).length();
                                str_len = str_index = len.value;
                                size = Shell.CMD_MAXLINE - str_index - 2;
                                Dos_files.DOS_WriteFile(Dos_files.STDOUT, line, len);
                                it_history++;
                                break;
                            case 0x50:
                                if (l_history.size() == 0 || it_history == 0) break;
                                it_history--;
                                if (it_history == 0) {
                                    it_history++;
                                    if (current_hist) {
                                        current_hist = false;
                                        l_history.removeElementAt(0);
                                    }
                                    break;
                                } else it_history--;
                                for (; str_index > 0; str_index--) {
                                    outc(8);
                                    outc(' ');
                                    outc(8);
                                }
                                StringHelper.strcpy(line, (String) l_history.elementAt(it_history));
                                len.value = ((String) l_history.elementAt(it_history)).length();
                                str_len = str_index = len.value;
                                size = Shell.CMD_MAXLINE - str_index - 2;
                                Dos_files.DOS_WriteFile(Dos_files.STDOUT, line, len);
                                it_history++;
                                break;
                            case 0x53:
                                {
                                    if (str_index >= str_len) break;
                                    IntRef a = new IntRef(str_len - str_index - 1);
                                    byte[] text = new byte[a.value];
                                    System.arraycopy(line, str_index + 1, text, 0, a.value);
                                    Dos_files.DOS_WriteFile(Dos_files.STDOUT, text, a);
                                    outc(' ');
                                    outc(8);
                                    for (int i = str_index; i < str_len - 1; i++) {
                                        line[i] = line[i + 1];
                                        outc(8);
                                    }
                                    line[--str_len] = 0;
                                    size++;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case 0x08:
                    if (str_index != 0) {
                        outc(8);
                        int str_remain = str_len - str_index;
                        size++;
                        if (str_remain != 0) {
                            for (int i = 0; i < str_remain; i++) line[str_index - 1 + i] = line[str_index + i];
                            line[--str_len] = 0;
                            str_index--;
                            for (int i = str_index; i < str_len; i++) outc(line[i]);
                        } else {
                            line[--str_index] = '\0';
                            str_len--;
                        }
                        outc(' ');
                        outc(8);
                        while (str_remain-- != 0) outc(8);
                    }
                    if (l_completion.size() != 0) l_completion.clear();
                    break;
                case 0x0a:
                    break;
                case 0x0d:
                    outc('\n');
                    size = 0;
                    break;
                case '\t':
                    {
                        if (l_completion.size() != 0) {
                            it_completion++;
                            if (it_completion == l_completion.size()) it_completion = 0;
                        } else {
                            boolean dir_only = StringHelper.toString(line).toUpperCase().startsWith("CD ");
                            String sLine = StringHelper.toString(line);
                            int p_completion_start = sLine.lastIndexOf(' ');
                            if (p_completion_start >= 0) {
                                p_completion_start++;
                                completion_index = p_completion_start;
                            } else {
                                p_completion_start = 0;
                                completion_index = 0;
                            }
                            int path;
                            if ((path = sLine.substring(completion_index).lastIndexOf('\\')) >= 0) completion_index += path + 1;
                            if ((path = sLine.substring(completion_index).lastIndexOf('/')) >= 0) completion_index += path + 1;
                            String mask;
                            if (p_completion_start >= 0) {
                                mask = sLine.substring(p_completion_start);
                                int dot_pos = mask.lastIndexOf('.');
                                int bs_pos = mask.lastIndexOf('\\');
                                int fs_pos = mask.lastIndexOf('/');
                                int cl_pos = mask.lastIndexOf(':');
                                if ((dot_pos - bs_pos > 0) && (dot_pos - fs_pos > 0) && (dot_pos - cl_pos > 0)) mask += "*"; else mask += "*.*";
                            } else {
                                mask = "*.*";
                            }
                            int save_dta = Dos.dos.dta();
                            Dos.dos.dta((int) Dos.dos.tables.tempdta);
                            boolean res = Dos_files.DOS_FindFirst(mask, 0xffff & ~Dos_system.DOS_ATTR_VOLUME);
                            if (!res) {
                                Dos.dos.dta((int) save_dta);
                                break;
                            }
                            Dos_DTA dta = new Dos_DTA(Dos.dos.dta());
                            StringRef name = new StringRef();
                            LongRef sz = new LongRef(0);
                            IntRef date = new IntRef(0);
                            IntRef time = new IntRef(0);
                            ShortRef att = new ShortRef(0);
                            int extIndex = 0;
                            while (res) {
                                dta.GetResult(name, sz, date, time, att);
                                if (!name.value.equals(".") && !name.value.equals("..")) {
                                    if (dir_only) {
                                        if ((att.value & Dos_system.DOS_ATTR_DIRECTORY) != 0) l_completion.add(name.value);
                                    } else {
                                        int pos = name.value.lastIndexOf('.');
                                        String ext = null;
                                        if (pos >= 0) ext = name.value.substring(pos + 1);
                                        if (ext != null && (ext.equalsIgnoreCase("BAT") || ext.equalsIgnoreCase("COM") || ext.equalsIgnoreCase("EXE"))) l_completion.insertElementAt(name.value, extIndex++); else l_completion.add(name.value);
                                    }
                                }
                                res = Dos_files.DOS_FindNext();
                            }
                            it_completion = 0;
                            Dos.dos.dta((int) save_dta);
                        }
                        if (l_completion.size() != 0 && ((String) l_completion.elementAt(it_completion)).length() != 0) {
                            for (; str_index > completion_index; str_index--) {
                                outc(8);
                                outc(' ');
                                outc(8);
                            }
                            StringHelper.strcpy(line, completion_index, (String) l_completion.elementAt(it_completion));
                            len.value = ((String) l_completion.elementAt(it_completion)).length();
                            str_len = str_index = completion_index + len.value;
                            size = Shell.CMD_MAXLINE - str_index - 2;
                            Dos_files.DOS_WriteFile(Dos_files.STDOUT, ((String) l_completion.elementAt(it_completion)).getBytes(), len);
                        }
                    }
                    break;
                case 0x1b:
                    outc('\\');
                    outc('\n');
                    line[0] = 0;
                    if (l_completion.size() != 0) l_completion.clear();
                    StringHelper.strcpy(line, InputCommand());
                    size = 0;
                    str_len = 0;
                    break;
                default:
                    if (l_completion.size() != 0) l_completion.clear();
                    if (str_index < str_len && true) {
                        outc(' ');
                        IntRef a = new IntRef(str_len - str_index);
                        byte[] text = new byte[a.value];
                        System.arraycopy(line, str_index, text, 0, a.value);
                        Dos_files.DOS_WriteFile(Dos_files.STDOUT, text, a);
                        outc(8);
                        for (int i = str_len; i > str_index; i--) {
                            line[i] = line[i - 1];
                            outc(8);
                        }
                        line[++str_len] = 0;
                        size--;
                    }
                    line[str_index] = c[0];
                    str_index++;
                    if (str_index > str_len) {
                        line[str_index] = '\0';
                        str_len++;
                        size--;
                    }
                    Dos_files.DOS_WriteFile(Dos_files.STDOUT, c, n);
                    break;
            }
        }
        if (str_len == 0) return null;
        str_len++;
        if (current_hist) {
            current_hist = false;
            l_history.removeElementAt(0);
        }
        String sLine = StringHelper.toString(line);
        l_history.insertElementAt(sLine, 0);
        it_history = 0;
        if (l_completion.size() != 0) l_completion.clear();
        return sLine;
    }
