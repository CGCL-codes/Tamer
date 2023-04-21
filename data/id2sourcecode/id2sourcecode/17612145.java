    public static void init() {
        if (didInit) {
            return;
        }
        didInit = true;
        prefs = ViManager.getViFactory().getPreferences();
        prefs.addPreferenceChangeListener(new PreferenceChangeListener() {

            public void preferenceChange(PreferenceChangeEvent evt) {
                Option opt = optionsMap.get(evt.getKey());
                if (opt != null) {
                    if (evt.getNewValue() != null) {
                        opt.preferenceChange(evt.getNewValue());
                    }
                }
            }
        });
        platformList.add("jViVersion");
        G.redoTrack = createBooleanOption(redoTrack, true);
        setupOptionDesc(platformList, redoTrack, "\".\" magic redo tracking", "Track magic document changes during input" + " mode for the \".\" commnad. These" + " changes are often the result of IDE code completion");
        G.pcmarkTrack = createBooleanOption(pcmarkTrack, true);
        setupOptionDesc(platformList, pcmarkTrack, "\"``\" magic pcmark tracking", "Track magic cursor " + " movments for the \"``\" command. These movement are" + " often the result of IDE actions invoked external" + " to jVi.");
        createColorOption(searchColor, new Color(0xffb442), false);
        setupOptionDesc(platformList, searchColor, "'hl-search' color", "The color used for search highlight.");
        createColorOption(searchFgColor, new Color(0x000000), true);
        setupOptionDesc(platformList, searchFgColor, "'hl-search' foreground color", "The color used for search highlight foreground.");
        setExpertHidden(searchFgColor, false, false);
        createColorOption(selectColor, new Color(0xffe588), false);
        setupOptionDesc(platformList, selectColor, "'hl-visual' color", "The color used for a visual mode selection.");
        createColorOption(selectFgColor, null, true);
        setupOptionDesc(platformList, selectFgColor, "'hl-visual' foreground color", "The color used for a visual mode selection foreground.");
        setExpertHidden(selectFgColor, false, false);
        G.isClassicUndo = createBooleanOption(classicUndoOption, true);
        setupOptionDesc(platformList, classicUndoOption, "classic undo", "When false, undo is done according to the" + " underlying platform; usually tiny chunks.");
        setExpertHidden(classicUndoOption, true, false);
        G.isHideVersion = createBooleanOption(hideVersionOption, false);
        setupOptionDesc(platformList, hideVersionOption, "hide version", "When true, display of initial version information" + " does not bring up output window.");
        setExpertHidden(hideVersionOption, true, false);
        G.useFrame = createBooleanOption(commandEntryFrame, true);
        setupOptionDesc(platformList, commandEntryFrame, "use modal frame", "Use modal frame for command/search entry." + " Change takes affect after restart.");
        setExpertHidden(commandEntryFrame, true, false);
        createBooleanOption(autoPopupFN, false);
        setupOptionDesc(platformList, autoPopupFN, "\":e#\" Auto Popup", "When doing \":\" command line entry, if \"e#\" is" + " entered then automatically popup a file" + " name completion window. NB6 only; post 07/07/22");
        G.isCoordSkip = createBooleanOption(coordSkip, true);
        setupOptionDesc(platformList, coordSkip, "Code Folding Compatible", "When false revert some navigation algorithms, e.g. ^F," + " to pre code folding behavior. A just in case option;" + " if needed, please file a bug report.");
        setExpertHidden(coordSkip, true, false);
        createBooleanOption(platformPreferences, false);
        setupOptionDesc(platformList, platformPreferences, "Store init (\"vimrc\") with Platform", "Store user preferences/options in platform location." + " Change occurs after next application startup." + " For example, on NetBeans store in userdir." + " NOTE: except for the first switch to platform," + " changes made in one area" + " are not propogated to the other.");
        setExpertHidden(platformPreferences, true, true);
        G.usePlatformInsertTab = createBooleanOption(platformTab, false);
        setupOptionDesc(platformList, platformTab, "Use the platform's TAB handling", "When false, jVi processes the TAB character according" + " to the expandtab and softtabstop options. Otherwise" + " the TAB is passed to the platform, e.g. IDE, for handling." + " The only reason to set this true is if a bug is discovered" + " in the jVi tab handling.");
        setExpertHidden(platformTab, true, false);
        G.p_so = createIntegerOption(scrollOff, 0);
        setupOptionDesc(generalList, scrollOff, "'scrolloff' 'so'", "visible context around cursor (scrolloff)" + "	Minimal number of screen lines to keep above and below the" + " cursor. This will make some context visible around where you" + " are working.  If you set it to a very large value (999) the" + " cursor line will always be in the middle of the window" + " (except at the start or end of the file)");
        G.p_smd = createBooleanOption(showMode, true);
        setupOptionDesc(generalList, showMode, "'showmode' 'smd'", "If in Insert or Replace mode display that information.");
        G.p_sc = createBooleanOption(showCommand, true);
        setupOptionDesc(generalList, showCommand, "'showcmd' 'sc'", "Show (partial) command in status line.");
        G.p_report = createIntegerOption(report, 2);
        setupOptionDesc(generalList, report, "'report'", "Threshold for reporting number of lines changed.  When the" + " number of changed lines is more than 'report' a message will" + " be given for most \":\" commands.  If you want it always, set" + " 'report' to 0.  For the \":substitute\" command the number of" + " substitutions is used instead of the number of lines.");
        G.p_ml = createBooleanOption(modeline, true);
        setupOptionDesc(generalList, modeline, "'modeline' 'ml'", "Enable/disable modelines option");
        G.p_mls = createIntegerOption(modelines, 5);
        setupOptionDesc(generalList, modelines, "'modelines' 'mls'", " If 'modeline' is on 'modelines' gives the number of lines" + " that is checked for set commands.  If 'modeline' is off" + " or 'modelines' is zero no lines are checked.");
        G.p_cb = createBooleanOption(unnamedClipboard, false);
        setupOptionDesc(generalList, unnamedClipboard, "'clipboard' 'cb' (unnamed)", "use clipboard for unamed yank, delete and put");
        G.p_notsol = createBooleanOption(notStartOfLine, false);
        setupOptionDesc(generalList, notStartOfLine, "(not)'startofline' (not)'sol'", "After motion try to keep column position." + " NOTE: state is opposite of vim.");
        G.viminfoMaxBuf = createIntegerOption(persistedBufMarks, 25, new IntegerOption.Validator() {

            @Override
            public void validate(int val) throws PropertyVetoException {
                if (val < 0 || val > 100) {
                    throw new PropertyVetoException("Only 0 - 100 allowed." + " Not '" + val + "'.", new PropertyChangeEvent(opt, opt.getName(), opt.getInteger(), val));
                }
            }
        });
        setupOptionDesc(generalList, persistedBufMarks, "max persisted buf-marks", "Maximum number of previously edited files for which the marks" + " are remembered. Set to 0 and no marks are persisted.");
        G.p_sel = createEnumStringOption(selection, "inclusive", new String[] { "old", "inclusive", "exclusive" });
        setupOptionDesc(generalList, selection, "'selection' 'sel'", "This option defines the behavior of the selection." + " It is only used in Visual and Select mode." + "Possible values: 'old', 'inclusive', 'exclusive'");
        setExpertHidden(selection, false, false);
        G.p_slm = createEnumStringOption(selectMode, "", new String[] { "mouse", "key", "cmd" });
        setupOptionDesc(generalList, selectMode, "'selectmode' 'slm'", "This is a comma separated list of words, which specifies when to" + " start Select mode instead of Visual mode, when a selection is" + " started. Possible values: 'mouse', key' or 'cmd'");
        setExpertHidden(selectMode, true, true);
        G.p_to = createBooleanOption(tildeOperator, false);
        setupOptionDesc(modifyList, tildeOperator, "'tildeop' 'top'", "tilde \"~\" acts like an operator, e.g. \"~w\" works");
        G.p_cpo_w = createBooleanOption(changeWordBlanks, true);
        setupOptionDesc(modifyList, changeWordBlanks, "'cpoptions' 'cpo' \"w\"", "\"cw\" affects sequential white space");
        G.p_js = createBooleanOption(joinSpaces, true);
        setupOptionDesc(modifyList, joinSpaces, "'joinspaces' 'js'", "\"J\" inserts two spaces after a \".\", \"?\" or \"!\"");
        G.p_sr = createBooleanOption(shiftRound, false);
        setupOptionDesc(modifyList, shiftRound, "'shiftround' 'sr'", "\"<\" and \">\" round indent to multiple of shiftwidth");
        G.p_bs = createEnumIntegerOption(backspace, 0, new Integer[] { 0, 1, 2 });
        setupOptionDesc(modifyList, backspace, "'backspace' 'bs'", "Influences the working of <BS>, <Del> during insert." + "\n  0 - no special handling." + "\n  1 - allow backspace over <EOL>." + "\n  2 - allow backspace over start of insert.");
        createBooleanOption(expandTabs, false);
        setupOptionDesc(modifyList, expandTabs, "'expandtab' 'et'", "In Insert mode: Use the appropriate number of spaces to" + " insert a <Tab>. Spaces are used in indents with the '>' and" + " '<' commands.");
        createIntegerOption(shiftWidth, 8);
        setupOptionDesc(modifyList, shiftWidth, "'shiftwidth' 'sw'", "Number of spaces to use for each step of indent. Used for '>>'," + " '<<', etc.");
        createIntegerOption(tabStop, 8);
        setupOptionDesc(modifyList, tabStop, "'tabstop' 'ts'", "Number of spaces that a <Tab> in the file counts for.");
        createIntegerOption(softTabStop, 0);
        setupOptionDesc(modifyList, softTabStop, "'softtabstop' 'sts'", "Number of spaces that a <Tab> in the file counts for" + " while performing editing operations," + " like inserting a <Tab> or using <BS>." + " It \"feels\" like <Tab>s are being inserted, while in fact" + " a mix of spaces and <Tab>s is used (<Tabs>s only if" + " 'expandtabs' is false).  When 'sts' is zero, this feature" + " is off. If 'softtabstop' is non-zero, a <BS> will try to" + " delete as much white space to move to the previous" + " 'softtabstop' position.");
        createIntegerOption(textWidth, 79);
        setupOptionDesc(modifyList, textWidth, "'textwidth' 'tw'", "This option currently only used in conjunction with the" + " 'gq' and 'Q' format command. This value is substituted" + " for " + twMagic + " in formatprg option string.");
        createStringOption(nrFormats, "octal,hex");
        setupOptionDesc(modifyList, nrFormats, "'nrformats' 'nf'", "Defines bases considered for numbers with the" + " 'CTRL-A' and 'CTRL-X' commands for adding to and subtracting" + " from a number respectively. Value is comma separated list;" + " 'octal,hex,alpha' is all possible values.");
        G.p_is = createBooleanOption(incrSearch, true);
        setupOptionDesc(searchList, incrSearch, "'incsearch' 'is'", "While typing a search command, show where the pattern, as it was" + " typed so far, matches. If invalid pattern, no match" + " or abort then the screen returns to its original location." + " You still need to finish the search with" + " <ENTER> or abort it with <ESC>.");
        G.p_hls = createBooleanOption(highlightSearch, true);
        setupOptionDesc(searchList, highlightSearch, "'hlsearch' 'hls'", "When there is a previous search pattern, highlight" + " all its matches");
        G.p_ic = createBooleanOption(ignoreCase, false);
        setupOptionDesc(searchList, ignoreCase, "'ignorecase' 'ic'", "Ignore case in search patterns.");
        G.p_ws = createBooleanOption(wrapScan, true);
        setupOptionDesc(searchList, wrapScan, "'wrapscan' 'ws'", "Searches wrap around the end of the file.");
        G.p_cpo_search = createBooleanOption(searchFromEnd, true);
        setupOptionDesc(searchList, searchFromEnd, "'cpoptions' 'cpo' \"c\"", "search continues at end of match");
        G.p_cpo_j = createBooleanOption(endOfSentence, false);
        setupOptionDesc(searchList, endOfSentence, "'cpoptions' 'cpo' \"j\"", "A sentence has to be followed by two spaces after" + " the '.', '!' or '?'.  A <Tab> is not recognized as" + " white space.");
        G.p_pbm = createBooleanOption(platformBraceMatch, true);
        setupOptionDesc(searchList, platformBraceMatch, "Platform Brace Matching", "Use the platform/IDE for brace matching" + " and match highlighting. This may enable additional" + " match characters, words and features.");
        G.p_meta_equals = createBooleanOption(metaEquals, true);
        setupOptionDesc(searchList, metaEquals, "RE Meta Equals", "In a regular expression allow" + " '=', in addition to '?', to indicate an optional atom.");
        setExpertHidden(metaEquals, true, false);
        G.p_meta_escape = createStringOption(metaEscape, G.metaEscapeDefault, new StringOption.Validator() {

            @Override
            public void validate(String val) throws PropertyVetoException {
                for (int i = 0; i < val.length(); i++) {
                    if (G.metaEscapeAll.indexOf(val.charAt(i)) < 0) {
                        throw new PropertyVetoException("Only characters from '" + G.metaEscapeAll + "' are RE metacharacters." + " Not '" + val.substring(i, i + 1) + "'.", new PropertyChangeEvent(opt, opt.getName(), opt.getString(), val));
                    }
                }
            }
        });
        setupOptionDesc(searchList, metaEscape, "RE Meta Escape", "Regular expression metacharacters requiring escape:" + " any of: '(', ')', '|', '+', '?', '{'." + " By default vim requires escape, '\\', for these characters.");
        setExpertHidden(metaEscape, true, false);
        G.p_ww_bs = createBooleanOption(backspaceWrapPrevious, true);
        setupOptionDesc(cursorWrapList, backspaceWrapPrevious, "'whichwrap' 'ww'  b - <BS>", "<backspace> wraps to previous line");
        G.p_ww_h = createBooleanOption(hWrapPrevious, false);
        setupOptionDesc(cursorWrapList, hWrapPrevious, "'whichwrap' 'ww'  h - \"h\"", "\"h\" wraps to previous line (not recommended, see vim doc)");
        G.p_ww_larrow = createBooleanOption(leftWrapPrevious, false);
        setupOptionDesc(cursorWrapList, leftWrapPrevious, "'whichwrap' 'ww'  < - <Left>", "<left> wraps to previous line");
        G.p_ww_sp = createBooleanOption(spaceWrapNext, true);
        setupOptionDesc(cursorWrapList, spaceWrapNext, "'whichwrap' 'ww'  s - <Space>", "<space> wraps to next line");
        G.p_ww_l = createBooleanOption(lWrapNext, false);
        setupOptionDesc(cursorWrapList, lWrapNext, "'whichwrap' 'ww'  l - \"l\"", "\"l\" wraps to next line (not recommended, see vim doc)");
        G.p_ww_rarrow = createBooleanOption(rightWrapNext, false);
        setupOptionDesc(cursorWrapList, rightWrapNext, "'whichwrap' 'ww'  > - <Right>", "<right> wraps to next line");
        G.p_ww_tilde = createBooleanOption(tildeWrapNext, false);
        setupOptionDesc(cursorWrapList, tildeWrapNext, "'whichwrap' 'ww'  ~ - \"~\"", "\"~\" wraps to next line");
        G.p_ww_i_left = createBooleanOption(insertLeftWrapPrevious, false);
        setupOptionDesc(cursorWrapList, insertLeftWrapPrevious, "'whichwrap' 'ww'  [ - <Left>", "in Insert Mode <Left> wraps to previous line");
        G.p_ww_i_right = createBooleanOption(insertRightWrapNext, false);
        setupOptionDesc(cursorWrapList, insertRightWrapNext, "'whichwrap' 'ww'  ] - <Right>", "in Insert Mode <Right> wraps to next line");
        boolean inWindows = ViManager.getOsVersion().isWindows();
        String defaultShell = System.getenv("SHELL");
        String defaultXQuote = "";
        String defaultFlag = null;
        if (defaultShell == null) {
            if (inWindows) defaultShell = "cmd.exe"; else defaultShell = "sh";
        }
        if (defaultShell.contains("sh")) {
            defaultFlag = "-c";
            if (inWindows) defaultXQuote = "\"";
        } else defaultFlag = "/c";
        G.p_sh = createStringOption(shell, defaultShell);
        setupOptionDesc(processList, shell, "'shell' 'sh'", "Name of shell to use for ! and :! commands.  (default $SHELL " + "or \"sh\", MS-DOS and Win32: \"command.com\" or \"cmd.exe\").  " + "When changing also check 'shellcmndflag'.");
        G.p_shcf = createStringOption(shellCmdFlag, defaultFlag);
        setupOptionDesc(processList, shellCmdFlag, "'shellcmdflag' 'shcf'", "Flag passed to shell to execute \"!\" and \":!\" commands; " + "e.g., \"bash.exe -c ls\" or \"command.com /c dir\" (default: " + "\"-c\", MS-DOS and Win32, when 'shell' does not contain \"sh\" " + "somewhere: \"/c\").");
        G.p_sxq = createStringOption(shellXQuote, defaultXQuote);
        setupOptionDesc(processList, shellXQuote, "'shellxquote' 'sxq'", "Quoting character(s), put around the commands passed to the " + "shell, for the \"!\" and \":!\" commands (default: \"\"; for " + "Win32, when 'shell' contains \"sh\" somewhere: \"\\\"\").");
        G.p_ssl = createBooleanOption(shellSlash, false);
        setupOptionDesc(processList, shellSlash, "'shellslash' 'ssl'", "When set, a forward slash is used when expanding file names." + "This is useful when a Unix-like shell is used instead of " + "command.com or cmd.exe.");
        G.p_ep = createStringOption(equalProgram, "");
        setupOptionDesc(processList, equalProgram, "'equalprg' 'ep'", "External program to use for \"=\" command (default \"\").  " + "When this option is empty the internal formatting functions " + "are used.");
        G.p_fp = createStringOption(formatProgram, "");
        setupOptionDesc(processList, formatProgram, "'formatprg' 'fp'", "External program to use for \"qq\" or \"Q\" command (default \"\")." + " When this option is empty the internal formatting functions" + " are used." + "\n\n When specified, the program must take input on stdin and" + " send output to stdout. In Unix, \"fmt\" is such a program." + twMagic + " in the string is" + " substituted by the value of textwidth option. " + "\n\nTypically set to \"fmt -w #TEXT-WIDTH#\" to use external program.");
        G.readOnlyHack = createBooleanOption(readOnlyHack, true);
        setupOptionDesc(debugList, readOnlyHack, "enable read only hack", "A Java implementation issue, restricts the characters that jVi" + " recieves for a read only file. Enabling this, changes the file" + " editor mode to read/write so that the file can be viewed" + " using the Normal Mode vi commands.");
        setExpertHidden(readOnlyHack, true, true);
        G.dbgEditorActivation = createBooleanOption(dbgEditorActivation, false);
        setupOptionDesc(debugList, dbgEditorActivation, "debug activation", "Output info about editor switching between files/windows");
        createBooleanOption(dbgKeyStrokes, false);
        setupOptionDesc(debugList, dbgKeyStrokes, "debug KeyStrokes", "Output info for each keystroke");
        G.dbgRedo = createBooleanOption(dbgRedo, false);
        setupOptionDesc(debugList, dbgRedo, "debug redo buffer", "Output info on magic/tracking changes to redo buffer");
        createBooleanOption(dbgCache, false);
        setupOptionDesc(debugList, dbgCache, "debug cache", "Output info on text/doc cache");
        createBooleanOption(dbgBang, false);
        setupOptionDesc(debugList, dbgBang, "debug \"!\" cmds", "Output info about external processes");
        createBooleanOption(dbgBangData, false);
        setupOptionDesc(debugList, dbgBangData, "debug \"!\" cmds data", "Output data tranfers external processes");
        createBooleanOption(dbgCompletion, false);
        setupOptionDesc(debugList, dbgCompletion, "debug Completion", "Output info on completion, eg FileName.");
        G.dbgMouse = createBooleanOption(dbgMouse, false);
        setupOptionDesc(debugList, dbgMouse, "debug mouse events", "Output info about mouse events");
        G.dbgCoordSkip = createBooleanOption(dbgCoordSkip, false);
        setupOptionDesc(debugList, dbgCoordSkip, "debug coordinate skip", "");
    }
