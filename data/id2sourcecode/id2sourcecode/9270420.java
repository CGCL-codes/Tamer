    boolean isShellCommand(String input_line) {
        ArrayList al = new ArrayList();
        if (-1 != input_line.indexOf('(')) {
            return false;
        }
        if (-1 == input_line.indexOf('\"')) {
            StringTokenizer st = new StringTokenizer(input_line, " ");
            while (st.hasMoreElements()) {
                al.add(st.nextToken());
            }
        } else {
            int num_quotes = 0;
            int p = input_line.indexOf('\"');
            while (-1 != p) {
                num_quotes++;
                p = input_line.indexOf('\"', p + 1);
            }
            if (0 != num_quotes % 2.0) {
                println("\n-->  Wrong number of quotes!");
                return true;
            }
            String a_command = null;
            int first_space = input_line.indexOf(' ');
            if (-1 != first_space) {
                a_command = input_line.substring(0, first_space);
            }
            if (null != a_command) {
                if (-1 == a_command.indexOf('(') && -1 == a_command.indexOf('+') && -1 == a_command.indexOf('\"') && -1 == a_command.indexOf('-') && -1 == a_command.indexOf('*') && -1 == a_command.indexOf('/')) {
                } else {
                    return false;
                }
            } else {
                if (-1 == input_line.indexOf('(') && -1 == input_line.indexOf('+') && -1 == input_line.indexOf('\"') && -1 == input_line.indexOf('-') && -1 == input_line.indexOf('*') && -1 == input_line.indexOf('/')) {
                    a_command = input_line;
                } else {
                    return false;
                }
            }
            al.add(a_command);
            String args = null;
            if (-1 != first_space) {
                args = input_line.substring(first_space + 1).trim();
            }
            if (null != args && 0 != args.length()) {
                int start_quote = args.indexOf('\"');
                int end_quote = args.indexOf('\"', start_quote + 1);
                if (0 != start_quote) {
                    putTokens(args.substring(0, start_quote), " ", al);
                }
                int stopper = 3;
                while (-1 != start_quote) {
                    String one_arg = args.substring(start_quote + 1, end_quote);
                    if (null != one_arg && 0 != one_arg.length()) {
                        al.add(one_arg);
                    }
                    if (args.length() <= end_quote + 1) {
                        break;
                    }
                    int next_quote = args.indexOf('\"', end_quote + 1);
                    if (-1 != next_quote && (next_quote - 2) != end_quote) {
                        putTokens(args.substring(end_quote + 1, next_quote).trim(), " ", al);
                        start_quote = next_quote;
                        end_quote = args.indexOf('\"', next_quote + 1);
                    } else if (-1 == next_quote) {
                        putTokens(args.substring(end_quote + 1).trim(), " ", al);
                        start_quote = -1;
                    } else {
                        start_quote = next_quote;
                        end_quote = args.indexOf('\"', start_quote + 1);
                    }
                    stopper--;
                    if (stopper == 0) break;
                }
            }
        }
        String command = (String) al.get(0);
        if (equal(command, "cd")) {
            if (1 == al.size()) {
                println("\n-->  Usage:  cd <directory_name>");
                return true;
            }
            String dir = (String) al.get(1);
            int dir_last_index = dir.length() - 1;
            if (dir_last_index != 0 && dir.lastIndexOf('/') == dir_last_index) {
                dir = dir.substring(0, dir_last_index);
            }
            String new_dir = null;
            if (equal(dir, "..")) {
                if (IJ.isWindows()) {
                    if (equal(dir, current_root_dir + file_separator)) {
                        println("\n-->  Such directory doesn't make sense.");
                        return true;
                    }
                } else {
                    if (equal(dir, current_root_dir)) {
                        println("\n-->  Such directory doesn't make sense.");
                        return true;
                    }
                }
                int last_slash = user_dir.lastIndexOf("/");
                new_dir = user_dir.substring(0, last_slash);
                if (-1 == new_dir.indexOf('/')) {
                    new_dir += "/";
                }
            } else if (IJ.isWindows() && 4 < dir.length() && dir.startsWith(current_root_dir + file_separator + "..")) {
                println("\n-->  Such directory doesn't make sense.");
                return true;
            } else if (2 < dir.length() && dir.startsWith(current_root_dir + "..")) {
                println("\n-->  Such directory doesn't make sense.");
                return true;
            } else if (-1 != dir.indexOf("..")) {
                String target_dir = null;
                if (dir.startsWith("/") || 1 == dir.indexOf(":/")) {
                    target_dir = dir;
                } else {
                    target_dir = user_dir + file_separator + dir;
                }
                int two_points = target_dir.indexOf("..");
                while (-1 != two_points) {
                    String temp = target_dir.substring(0, two_points - 1);
                    int ending_slash = temp.lastIndexOf('/');
                    String parent_dir = temp.substring(0, ending_slash);
                    String trailing_stuff = "";
                    if (two_points + 3 < target_dir.length()) {
                        trailing_stuff = file_separator + target_dir.substring(two_points + 3);
                    }
                    target_dir = parent_dir + trailing_stuff;
                    two_points = target_dir.indexOf("..");
                }
                new_dir = target_dir;
            } else if (equal(dir, ".")) {
                return true;
            } else if (dir.startsWith("/") || 1 == dir.indexOf(":/")) {
                new_dir = dir;
            } else {
                new_dir = user_dir + file_separator + dir;
            }
            File f_new_dir = new File(new_dir);
            if (f_new_dir.exists() && f_new_dir.isDirectory()) {
                user_dir = new_dir;
            } else {
                println("\n-->  No such directory.");
                return true;
            }
            if (IJ.isWindows()) {
                current_root_dir = user_dir.substring(0, 2);
            } else {
                current_root_dir = "/";
            }
            String dir_name = user_dir;
            if (-1 == user_dir.lastIndexOf('/')) {
                dir_name += "/";
            }
            println("\n-->  changed directory to: " + dir_name);
            return true;
        } else if (equal(command, "pwd")) {
            println("\n-->  current directory:");
            String dir_name = user_dir;
            if (-1 == user_dir.lastIndexOf('/')) {
                dir_name += "/";
            }
            println("--> " + dir_name);
            return true;
        } else if (equal(command, "lsi")) {
            File f = new File(user_dir);
            String[] image_name;
            if (2 == al.size()) {
                image_name = f.list(new ImageFileFilter((String) al.get(1)));
            } else {
                image_name = f.list(new ImageFileFilter());
            }
            if (0 < image_name.length) {
                println("-->  Images in " + user_dir + " :");
                char space = ' ';
                for (int im = 0; im < image_name.length; im++) {
                    StringBuffer data = new StringBuffer();
                    String path = user_dir + file_separator + image_name[im];
                    data.append("\n-->  " + new File(path).length() / 1000.0 + " KB");
                    while (data.length() < 30) {
                        data.append(space);
                    }
                    data.append(image_name[im]);
                    screen.append(data.toString());
                }
            } else {
                screen.append(l + "-->  No [such] images in " + user_dir);
            }
            println("");
            return true;
        } else if (equal(command, "lsd")) {
            println("-->  Directories in " + user_dir + " :");
            File f = new File(user_dir);
            File[] all;
            if (2 == al.size()) {
                all = f.listFiles(new CustomFileFilter((String) al.get(1)));
            } else {
                all = f.listFiles();
            }
            boolean no_dirs = true;
            for (int i = 0; i < all.length; i++) {
                if (all[i].isDirectory()) {
                    screen.append("\n-->  \t" + all[i].getName() + file_separator);
                    no_dirs = false;
                }
            }
            if (no_dirs) {
                screen.append("\n-->  There are no [such] directories in " + user_dir);
            }
            println("");
            return true;
        } else if (equal(command, "ls")) {
            String dir = user_dir;
            if (-1 == user_dir.lastIndexOf('/')) {
                dir += "/";
            }
            println("-->  Files in " + dir + " :");
            File f = new File(dir);
            String[] file_name;
            if (2 == al.size()) {
                file_name = f.list(new CustomFileFilter((String) al.get(1)));
            } else {
                file_name = f.list();
            }
            if (0 == file_name.length) {
                println("-->  No [such] file/s.");
                return true;
            }
            String slash = "/";
            String nothing = "";
            char space = ' ';
            for (int im = 0; im < file_name.length; im++) {
                StringBuffer data = new StringBuffer();
                String path = user_dir + file_separator + file_name[im];
                File file = new File(path);
                if (file.isDirectory()) {
                    data.append("\n-->                          ");
                } else {
                    data.append("\n-->  " + file.length() / 1000.0 + " KB");
                    while (data.length() < 30) {
                        data.append(space);
                    }
                }
                data.append(file_name[im]);
                if (file.isDirectory()) {
                    data.append(slash);
                }
                screen.append(data.toString());
            }
            println("");
            return true;
        } else if (equal(command, "record")) {
            if (1 == al.size()) {
                println("-->  A name must be provided: 'record macroname'");
                return true;
            }
            MacroRecord.setRecording(true);
            String macro_name = (String) al.get(1);
            MacroRecord.makeNew(macro_name);
            println("-->  Recording to: " + macro_name);
            return true;
        } else if (equal(command, "stop")) {
            if (MacroRecord.isRecording()) {
                MacroRecord.setRecording(false);
                println("\n-->  Finished recording.");
            } else {
                println("\n-->  Nothing is being recorded.");
            }
            return true;
        } else if (equal(command, "exec")) {
            String the_macro = null;
            if (1 == al.size()) {
                the_macro = MacroRecord.getCurrentCode();
            } else {
                the_macro = MacroRecord.getCode((String) al.get(1));
            }
            if (the_macro != null) {
                println("\n-->  Executing" + ((al.size() > 1) ? " " + MacroRecord.autoCompleteName((String) al.get(1)) : MacroRecord.getCurrentName()) + ".");
                println(the_macro);
                execMacro(the_macro);
            } else if (1 < al.size()) {
                String[] the_macro2 = new String[2];
                if ((the_macro2 = findMacro((String) al.get(1))) != null) {
                    println("-->  Executing " + the_macro2[0]);
                    println(the_macro2[1]);
                    execMacro(the_macro2[1]);
                } else {
                    println("\n-->  No such macro: " + (String) al.get(1));
                }
            } else {
                println("n-->  No macros recorded.");
            }
            return true;
        } else if (equal(command, "list")) {
            String[] macro_name = MacroRecord.getList();
            if (0 == macro_name.length) {
                println("\n-->  Zero recorded macros.");
                return true;
            }
            println("\n-->  Recorded macros:");
            for (int i = 0; i < macro_name.length; i++) {
                println("\n-->  \t" + macro_name[i]);
            }
            println("\n");
            return true;
        } else if (equal(command, "save")) {
            if (1 == al.size()) {
                println("\n-->  A macro name must be specified.");
                return true;
            }
            MacroRecord mcr = MacroRecord.find((String) al.get(1));
            if (null == mcr) {
                println("\n-->  No recorded macro named " + (String) al.get(1));
                return true;
            }
            String macro_code = mcr.getCodeForSystem();
            saveMacro(macro_code);
            return true;
        } else if (equal(command, "rm")) {
            if (1 == al.size()) {
                println("\n-->  Usage:  rm <file_name>\n-->    No asterisks allowed.");
                return true;
            }
            File trashcan = new File(trash_can);
            if (!trashcan.exists()) {
                boolean check = trashcan.mkdir();
                if (!check) {
                    println("\n-->  Trash Can does not exist and could not be created.");
                }
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException ie) {
                }
            }
            if (1 == al.size()) {
                println("\n-->  rm : A file name must be specified.");
                return true;
            }
            String file_name = (String) al.get(1);
            if (-1 != file_name.indexOf('*')) {
                println("\n--> Wild cards '*' not allowed in rm command.");
                return true;
            }
            File f;
            if (file_name.startsWith("/")) {
                f = new File(file_name);
            } else {
                file_name = user_dir + file_separator + file_name;
                f = new File(file_name);
            }
            if (f.exists()) {
                if (f.isDirectory()) {
                    String[] list = f.list();
                    if (0 != list.length) {
                        if (2 == list.length) {
                            if (equal(list[0], ".") && equal(list[1], "..")) {
                            } else {
                                println("\n-->  " + file_name + " is a non-empty directory! Deleting stopped");
                                return true;
                            }
                        }
                    }
                }
                String trashed_name = trash_can + file_separator + f.getName();
                File file_trashed = new File(trashed_name);
                int i = 1;
                while (file_trashed.exists()) {
                    trashed_name = trash_can + file_separator + f.getName() + "_" + i;
                    file_trashed = new File(trashed_name);
                    i++;
                }
                if (f.renameTo(file_trashed)) {
                    println("\n-->  " + file_name.substring(file_name.lastIndexOf(file_separator) + 1) + " successfully moved to the trash can.");
                } else {
                    println("\n-->  " + file_name.substring(file_name.lastIndexOf(file_separator)) + " could NOT be trashed.");
                }
            } else {
                println("\n-->  " + file_name + " does not exist!");
            }
            return true;
        } else if (equal(command, "mkdir")) {
            if (1 == al.size()) {
                println("\n-->  Usage : mkdir <new_dir_name>");
                return true;
            }
            File f;
            String dir_name = (String) al.get(1);
            if (dir_name.startsWith("/")) {
                f = new File(dir_name);
            } else {
                dir_name = user_dir + file_separator + dir_name;
                f = new File(dir_name);
            }
            if (f.exists()) {
                println("\n-->  Directory " + dir_name + " already exists!");
            } else {
                if (f.mkdir()) {
                    println("\n-->  Directory " + dir_name + " sucessfully created");
                } else {
                    println("\n-->  Could NOT create the directory!");
                }
            }
            return true;
        } else if (equal(command, "magic")) {
            magic = !magic;
            println("\n-->  magic is " + (magic ? "ON" : "OFF"));
            return true;
        } else if (equal(command, "erase")) {
            if (al.size() < 2) {
                MacroRecord.eraseLinesFromCurrent(1);
                return true;
            }
            if (equal((String) al.get(1), "-l") && 2 == al.size()) {
                println("\n--> Line number not specified!");
                return true;
            }
            if (equal((String) al.get(1), "-l")) {
                try {
                    int line = Integer.parseInt((String) al.get(2));
                    if (MacroRecord.eraseLineFromCurrent(line)) {
                        println("\n-->  line " + line + " erased.");
                    } else {
                        println("\n--> line " + line + " out of range.\n");
                    }
                } catch (Exception e) {
                    println("\n--> Supplied argument is not a valid number.\n");
                }
            } else {
                try {
                    int num_lines = Integer.parseInt((String) al.get(1));
                    int erased_lines = MacroRecord.eraseLinesFromCurrent(num_lines);
                    if (-1 == erased_lines) {
                        println("\n-->  All lines erased.");
                    } else if (-2 == erased_lines) {
                        println("\n-->  No recorded macro to edit.");
                    } else {
                        println("\n-->  " + erased_lines + " lines erased.");
                    }
                } catch (Exception e) {
                    println("\n--> Supplied argument is not a valid number.");
                }
            }
            return true;
        } else if (equal(command, "front")) {
            boolean activate = false;
            if (al.size() < 2) {
                String[] list = MacroRecord.getList();
                if (list.length == 0) {
                    println("\n-->  No recorded macro.");
                } else {
                    println("\n-->  Front macro is " + MacroRecord.getCurrentName() + " and it is " + ((MacroRecord.isRecording()) ? "" : "not") + " being edited.");
                }
            } else {
                activate = MacroRecord.setActive((String) al.get(1));
            }
            if (activate) {
                println("\n-->  Now recording on: " + (String) al.get(1) + l);
            }
            return true;
        } else if (equal(command, "view")) {
            MacroRecord mc;
            if (al.size() == 1) {
                mc = MacroRecord.getCurrent();
            } else {
                mc = MacroRecord.find((String) al.get(1));
            }
            if (null != mc) {
                println("\n-->  Macro : " + mc.getName());
                println("\n" + mc.getCode());
            } else if (1 < al.size()) {
                String[] a_macro = null;
                if ((a_macro = findMacro((String) al.get(1))) != null) {
                    println("\n-->  Macro : " + a_macro[0]);
                    println(a_macro[1]);
                } else {
                    println("\n-->  No such macro: " + (String) al.get(1));
                }
            } else {
                println("\n-->  No macro recorded or no such macro.");
            }
            return true;
        } else if (equal(command, "help")) {
            println("\n-->  Command line interface for ImageJ");
            println("-->  -- Albert Cardona 2004 --");
            println("-->  Just type in any ImageJ macro code and it will be executed after pushing intro.");
            println("-->  Multiline macro commands can be typed by adding an ending \\");
            println("-->  Unix-like basic shell functions available.");
            println("-->  TAB key expands file names and macro functions names.");
            println("-->  UP and DOWN arrows bring back entered commands.");
            println("-->  Mouse selecting text brings contextual menu.");
            println("-->  \n-->  Macro Commands:");
            println("-->    record <macro_name> : start recording a macro.");
            println("-->    stop : stop the recording.");
            println("-->    view [<macro_name>] : print the macro code from macro macro_name without executing it, or from the front macro.\n-->       An attempt will be made to match uncompleted names\n-->       from the recorded list, the current directory, and the ImageJ macros directory.");
            println("-->    list : list all recorded macros.");
            println("-->    save <macro_name>: save recorded macro to a file.");
            println("-->    exec [<macro_name>] : execute a recorded macro macro_name, or the front macro.\n-->       An attempt will be made to match uncompleted names\n-->       from the recorded list, the current directory, and the ImageJ macros directory.");
            println("-->    front [<macro_name>] : start editing macro macro_name, or just print who is the front macro.");
            println("-->    erase [-l line_number]|[num_lines] : erase line line_number or erase num_lines starting from the end, or just the last line.");
            println("-->    toggle_edit : enable/disable direct screen editing.");
            println("-->    magic : toggle magic ON/OFF. When on, the program attempts to guess several things \n-->       and transform the input. Example: dc invert  -> doCommand(\"Invert\") ,\n-->       or makeRectangle 10,10,30,40 -> makeRectangle(10,10,30,40)");
            println("-->    doc [<url>]: show ImageJ website macro documentation pages, or a given url.");
            println("-->  \n-->  Shell-like Commands:");
            println("-->    open <image_file/s>|<directory> : open an image file or a list of space-separated image names or paths.\n-->      Accepts wildcard (*) at start, end, or both.\n-->      Will print the correct macro code to open the image.\n-->      Alternatively, it will open as a stack all images in the specified directory.\n-->      Without arguments, opens current directory images as a stack.");
            println("-->    ls [<file_name>]: list all files in working directory.");
            println("-->    lsi [<file_name>]: list images in working directory.");
            println("-->    lsd [<file_name>]: list directories in the working directory.");
            println("-->    pwd : print working directory.");
            println("-->    cd <directory> : change directory.");
            println("-->    rm <file_name> : move file_name to the trash can located at this plugin folder.");
            println("-->    empty_trash : empty the CLI Trash Can.");
            println("-->    clear : clear screen.");
            println("-->    screenshot [window_name [target_file_name [delay_in_seconds]]] : idem.");
            println("-->    show [directory [file [time]]]: slide show on current or specified directory,\n-->      of files <file> (accepts *) and every <time> (in seconds).");
            println("-->  \n-->  Contextual Menu:");
            println("-->    Select any piece of text from the screen.\n-->    Lines starting with '-->  ' will be ignored,\n-->    as well as the starting '> ' and ending '\\' characters.");
            println("-->      Execute Selection : idem");
            println("-->      Record : make a new macro from selection.");
            println("-->      Copy : copy selection to system paste buffer.");
            println("-->      Save Selection : open file dialog to save selection as a macro text file.");
            println("-->      Save & Exec Selection : idem.");
            println("-->  ");
            return true;
        } else if (equal(command, "empty_trash")) {
            File trash = new File(trash_can);
            File[] f = trash.listFiles();
            int failed = 0;
            for (int i = 0; i < f.length; i++) {
                String file_name = f[i].getName();
                boolean check = false;
                if (!equal(file_name, ".") && !equal(file_name, "..")) {
                    check = f[i].delete();
                }
                if (false == check) {
                    println("\n-->  Could not delete file " + file_name);
                    failed++;
                }
            }
            if (failed == 0) {
                println("\n-->  Trash can successfully emptied.");
            } else {
                println("\n-->  Some files may have not been deleted.");
            }
            return true;
        } else if (equal(command, "clear")) {
            screen.setText("");
            return true;
        } else if (equal(command, "screenshot")) {
            Screenshot s = null;
            if (1 == al.size()) {
                s = new Screenshot(null, 0, user_dir, null);
            } else if (2 == al.size()) {
                java.awt.Frame frame = WindowManager.getFrame((String) al.get(1));
                if (null != frame) {
                    s = new Screenshot(frame, 0, user_dir, null);
                } else {
                    println("\n-->  No such window: " + (String) al.get(1));
                }
            } else if (3 == al.size()) {
                java.awt.Frame frame = WindowManager.getFrame((String) al.get(1));
                if (null != frame) {
                    s = new Screenshot(frame, 0, user_dir, (String) al.get(2));
                } else {
                    println("\n-->  No such window: " + (String) al.get(1));
                }
            } else if (4 == al.size()) {
                java.awt.Frame frame = WindowManager.getFrame((String) al.get(1));
                if (null != frame) {
                    try {
                        s = new Screenshot(frame, Integer.parseInt((String) al.get(3)), user_dir, (String) al.get(2));
                    } catch (NumberFormatException nfe) {
                        println("\n-->  Wrong number format for seconds. Stopping.");
                    }
                } else {
                    println("\n-->  No such window: " + (String) al.get(1));
                }
            }
            s.setOut(screen);
            new Thread(s).start();
            println(s.getReport());
            return true;
        } else if (equal(command, "mv")) {
            try {
                if (2 < al.size()) {
                    String file_name = (String) al.get(1);
                    if (0 == file_name.indexOf('/') || 1 == file_name.indexOf(':')) {
                    } else {
                        file_name = user_dir + file_separator + file_name;
                    }
                    String new_file_name = fixDir((String) al.get(2));
                    if (null == new_file_name) {
                        println("\n-->  Incorrect target file_name or dir. File/s not moved.");
                        return true;
                    }
                    if (-1 != new_file_name.indexOf('*')) {
                        println("\n-->  No wildcards allowed in target file_name or dir");
                        return true;
                    }
                    File new_file = new File(new_file_name);
                    String files_dir = file_name.substring(0, file_name.lastIndexOf('/'));
                    File f_files_dir = new File(files_dir);
                    String[] file_names = f_files_dir.list(new CustomFileFilter(file_name.substring(file_name.lastIndexOf('/') + 1)));
                    if (0 == file_names.length) {
                        println("\n-->  No such file/s: \n-->  " + file_name);
                        return true;
                    }
                    if (new_file.exists() && new_file.isDirectory()) {
                        for (int i = 0; i < file_names.length; i++) {
                            File source_file = new File(files_dir + file_separator + file_names[i]);
                            String target_file_name = new_file_name + file_separator + file_names[i];
                            File target_file = new File(target_file_name);
                            if (target_file.exists()) {
                                println("\n-->  A file named \n-->  " + target_file.getName() + "\n-->  already exists in target directory \n-->  " + new_file_name);
                                continue;
                            }
                            boolean check = source_file.renameTo(target_file);
                            if (check) {
                                println("\n-->  File successfully moved to:\n-->  " + target_file_name);
                            } else {
                                println("\n-->  Could not move the file \n-->  " + target_file_name + "\n-->       into directory " + new_file_name);
                            }
                        }
                    } else if (1 == file_names.length && !new_file.isDirectory()) {
                        if (new_file.exists()) {
                            println("\n-->  A file named " + new_file.getName() + " already exists!\n-->  Not moving the file " + file_names[0]);
                            return true;
                        } else {
                            File source_file = new File(files_dir + file_separator + file_names[0]);
                            boolean check = source_file.renameTo(new_file);
                            if (check) {
                                println("\n-->  File successfully moved to:\n-->  " + new_file_name);
                            } else {
                                println("\n-->  Could not move the file \n-->  " + file_names[0] + "\n--> to file " + new_file_name);
                            }
                        }
                    }
                    return true;
                } else {
                    println("\n-->  Usage: mv <file_name> <dir | new_file_name>");
                    return true;
                }
            } catch (Exception e) {
                IJ.write("Some error ocurred:\n" + new TraceError(e));
            }
            return true;
        } else if (equal(command, "doc")) {
            String url = "http://rsb.info.nih.gov/ij/developer/macro/macros.html";
            if (al.size() == 2) {
                url = (String) al.get(1);
            }
            println("\n-->  Opening " + url);
            try {
                JEditorPane jep = new JEditorPane(url);
                jep.setPreferredSize(new Dimension(500, 600));
                jep.setEditable(false);
                jep.addHyperlinkListener(new HyperlinkAdapter(jep));
                JScrollPane scroll = new JScrollPane(jep);
                scroll.setPreferredSize(new Dimension(500, 600));
                JFrame f = new JFrame("Macro Functions List");
                f.setSize(new Dimension(500, 600));
                f.getContentPane().add(scroll);
                f.pack();
                f.show();
            } catch (Exception ioe) {
                println("\n-->  Dictionary could not be found at url:\n-->  " + url);
            }
            return true;
        } else if (equal(command, "open")) {
            if (al.size() < 2) {
                return false;
            }
            String dir_path = fixDir((String) al.get(1));
            File dir = new File(dir_path);
            if (!(dir.exists() && dir.isDirectory())) {
                return false;
            }
            OpenDirectory od = new OpenDirectory(dir_path, OpenDirectory.STACK);
            println("\n-->  " + od.getMessage());
            return true;
        } else if (equal(command, "show")) {
            String the_macro = null;
            if (al.size() < 2) {
                the_macro = "run(\"Slide Show\", \"folder=" + user_dir + " file=* time=4\")\n";
            } else {
                the_macro = "run(\"Slide Show\", \"folder=" + fixDir((String) al.get(1));
                if (al.size() > 2) {
                    the_macro += " file=" + (String) al.get(2);
                }
                if (al.size() > 3) {
                    the_macro += " time=" + (String) al.get(3);
                }
                the_macro += "\")\n";
            }
            println(the_macro);
            execMacro(the_macro);
            return true;
        }
        return false;
    }
