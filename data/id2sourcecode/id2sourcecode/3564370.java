    public void setupCommands(ComponentRegistry registry, CommandHandler ch) {
        this.registry = registry;
        final MSP430 cpu = (MSP430) registry.getComponent(MSP430.class);
        final GenericNode node = (GenericNode) registry.getComponent("node");
        if (cpu != null) {
            ch.registerCommand("break", new BasicAsyncCommand("add a breakpoint to a given address or symbol", "<address or symbol>") {

                int address = 0;

                public int executeCommand(final CommandContext context) {
                    int baddr = context.getArgumentAsAddress(0);
                    if (baddr < 0) {
                        context.err.println("unknown symbol: " + context.getArgument(0));
                        return 1;
                    }
                    cpu.setBreakPoint(address = baddr, new CPUMonitor() {

                        public void cpuAction(int type, int adr, int data) {
                            context.out.println("*** Break at $" + Utils.hex16(adr));
                            cpu.stop();
                        }
                    });
                    context.err.println("Breakpoint set at $" + Utils.hex16(baddr));
                    return 0;
                }

                public void stopCommand(CommandContext context) {
                    cpu.clearBreakPoint(address);
                }
            });
            ch.registerCommand("watch", new BasicAsyncCommand("add a write/read watch to a given address or symbol", "<address or symbol> [length] [char | hex | break]") {

                int mode = 0;

                int address = 0;

                int length = 1;

                public int executeCommand(final CommandContext context) {
                    int baddr = context.getArgumentAsAddress(0);
                    if (baddr == -1) {
                        context.err.println("unknown symbol: " + context.getArgument(0));
                        return -1;
                    }
                    if (context.getArgumentCount() > 1) {
                        for (int i = 1; i < context.getArgumentCount(); i++) {
                            String modeStr = context.getArgument(i);
                            if (Character.isDigit(modeStr.charAt(0))) {
                                length = Integer.parseInt(modeStr);
                            } else if ("char".equals(modeStr)) {
                                mode = Utils.ASCII_UNMODIFIED;
                            } else if ("break".equals(modeStr)) {
                                mode = 10;
                            } else if ("hex".equals(modeStr)) {
                                mode = Utils.HEX;
                            }
                        }
                    }
                    CPUMonitor monitor = new CPUMonitor() {

                        public void cpuAction(int type, int adr, int data) {
                            if (mode == 0 || mode == 10) {
                                int pc = cpu.readRegister(0);
                                String adrStr = getSymOrAddr(context, adr);
                                String pcStr = getSymOrAddrELF(getELF(), pc);
                                String op = "op";
                                if (type == MEMORY_READ) {
                                    op = "Read";
                                } else if (type == MEMORY_WRITE) {
                                    op = "Write";
                                }
                                context.out.println("*** " + op + " from " + pcStr + ": " + adrStr + " = " + data);
                                if (mode == 10) {
                                    cpu.stop();
                                }
                            } else {
                                if (length > 1) {
                                    for (int i = address; i < address + length; i++) {
                                        context.out.print(Utils.toString(cpu.memory[i], Utils.BYTE, mode));
                                    }
                                    context.out.println();
                                } else {
                                    context.out.print(Utils.toString(data, Utils.BYTE, mode));
                                }
                            }
                        }
                    };
                    cpu.setBreakPoint(address = baddr, monitor);
                    if (length > 1) {
                        for (int i = 1; i < length; i++) {
                            cpu.setBreakPoint(address + i, monitor);
                        }
                    }
                    context.err.println("Watch set at $" + Utils.hex16(baddr));
                    return 0;
                }

                public void stopCommand(CommandContext context) {
                    cpu.clearBreakPoint(address);
                    context.exit(0);
                }
            });
            ch.registerCommand("watchreg", new BasicAsyncCommand("add a write watch to a given register", "<register> [int]") {

                int mode = 0;

                int register = 0;

                public int executeCommand(final CommandContext context) {
                    register = context.getArgumentAsRegister(0);
                    if (register < 0) {
                        return -1;
                    }
                    if (context.getArgumentCount() > 1) {
                        String modeStr = context.getArgument(1);
                        if ("int".equals(modeStr)) {
                            mode = 1;
                        } else {
                            context.err.println("illegal argument: " + modeStr);
                            return -1;
                        }
                    }
                    cpu.setRegisterWriteMonitor(register, new CPUMonitor() {

                        public void cpuAction(int type, int adr, int data) {
                            if (mode == 0) {
                                int pc = cpu.readRegister(0);
                                String adrStr = getRegisterName(register);
                                String pcStr = getSymOrAddrELF(getELF(), pc);
                                context.out.println("*** Write from " + pcStr + ": " + adrStr + " = " + data);
                            } else {
                                context.out.println(data);
                            }
                        }
                    });
                    context.err.println("Watch set for register " + getRegisterName(register));
                    return 0;
                }

                public void stopCommand(CommandContext context) {
                    cpu.clearBreakPoint(register);
                }
            });
            ch.registerCommand("clear", new BasicCommand("clear a breakpoint or watch from a given address or symbol", "<address or symbol>") {

                public int executeCommand(final CommandContext context) {
                    int baddr = context.getArgumentAsAddress(0);
                    cpu.setBreakPoint(baddr, null);
                    return 0;
                }
            });
            ch.registerCommand("symbol", new BasicCommand("list matching symbols", "<regexp>") {

                public int executeCommand(final CommandContext context) {
                    String regExp = context.getArgument(0);
                    MapEntry[] entries = context.getMapTable().getEntries(regExp);
                    boolean found = false;
                    for (int i = 0; i < entries.length; i++) {
                        MapEntry mapEntry = entries[i];
                        int address = mapEntry.getAddress();
                        context.out.println(" " + mapEntry.getName() + " at $" + Utils.hex16(address) + " (" + Utils.hex8(cpu.memory[address]) + " " + Utils.hex8(cpu.memory[address + 1]) + ") " + mapEntry.getType() + " in file " + mapEntry.getFile());
                        found = true;
                    }
                    if (!found) {
                        context.err.println("Could not find any symbols matching '" + regExp + '\'');
                    }
                    return 0;
                }
            });
            ch.registerCommand("line", new BasicCommand("print line number of address/symbol", "<address or symbol>") {

                public int executeCommand(final CommandContext context) {
                    int adr = context.getArgumentAsAddress(0);
                    DebugInfo di = getELF().getDebugInfo(adr);
                    if (di == null) {
                        di = getELF().getDebugInfo(adr + 1);
                    }
                    if (di != null) {
                        di.getLine();
                        context.out.println(di);
                    } else {
                        context.err.println("No line number found for: " + context.getArgument(0));
                    }
                    return 0;
                }
            });
            if (node != null) {
                ch.registerCommand("stop", new BasicCommand("stop the CPU", "") {

                    public int executeCommand(CommandContext context) {
                        node.stop();
                        context.out.println("CPU stopped at: $" + Utils.hex16(cpu.readRegister(0)));
                        return 0;
                    }
                });
                ch.registerCommand("start", new BasicCommand("start the CPU", "") {

                    public int executeCommand(CommandContext context) {
                        node.start();
                        return 0;
                    }
                });
                ch.registerCommand("throw", new BasicCommand("throw an Emulation Exception", "") {

                    public int executeCommand(CommandContext context) {
                        throw new EmulationException(context.getArgument(0));
                    }
                });
                ch.registerCommand("step", new BasicCommand("single step the CPU", "[number of instructions]") {

                    public int executeCommand(CommandContext context) {
                        int nr = context.getArgumentCount() > 0 ? context.getArgumentAsInt(0) : 1;
                        long cyc = cpu.cycles;
                        if (cpu.isRunning()) {
                            context.err.println("Can not single step when emulation is running.");
                            return -1;
                        }
                        try {
                            node.step(nr);
                        } catch (Exception e) {
                            e.printStackTrace(context.out);
                        }
                        context.out.println("CPU stepped to: $" + Utils.hex16(cpu.readRegister(0)) + " in " + (cpu.cycles - cyc) + " cycles (" + cpu.cycles + ")");
                        return 0;
                    }
                });
                ch.registerCommand("stepmicro", new BasicCommand("single the CPU specified no micros", "<micro skip> <micro step>") {

                    public int executeCommand(CommandContext context) {
                        long cyc = cpu.cycles;
                        if (cpu.isRunning()) {
                            context.err.println("Can not single step when emulation is running.");
                            return -1;
                        }
                        long nxt = 0;
                        try {
                            nxt = cpu.stepMicros(context.getArgumentAsLong(0), context.getArgumentAsLong(1));
                        } catch (Exception e) {
                            e.printStackTrace(context.out);
                        }
                        context.out.println("CPU stepped to: $" + Utils.hex16(cpu.readRegister(0)) + " in " + (cpu.cycles - cyc) + " cycles (" + cpu.cycles + ") - next exec time: " + nxt);
                        return 0;
                    }
                });
                ch.registerCommand("stack", new BasicCommand("show stack info", "") {

                    public int executeCommand(CommandContext context) {
                        int stackEnd = context.getMapTable().heapStartAddress;
                        int stackStart = context.getMapTable().stackStartAddress;
                        int current = cpu.readRegister(MSP430Constants.SP);
                        context.out.println("Current stack: $" + Utils.hex16(current) + " (" + (stackStart - current) + " used of " + (stackStart - stackEnd) + ')');
                        return 0;
                    }
                });
                ch.registerCommand("print", new BasicCommand("print value of an address or symbol", "<address or symbol>") {

                    public int executeCommand(CommandContext context) {
                        int adr = context.getArgumentAsAddress(0);
                        if (adr != -1) {
                            try {
                                context.out.println(context.getArgument(0) + " = $" + Utils.hex16(cpu.read(adr, adr >= 0x100)));
                            } catch (Exception e) {
                                e.printStackTrace(context.err);
                            }
                            return 0;
                        }
                        context.err.println("unknown symbol: " + context.getArgument(0));
                        return 1;
                    }
                });
                ch.registerCommand("printreg", new BasicCommand("print value of an register", "<register>") {

                    public int executeCommand(CommandContext context) {
                        int register = context.getArgumentAsRegister(0);
                        if (register >= 0) {
                            context.out.println(context.getArgument(0) + " = $" + Utils.hex16(cpu.readRegister(register)));
                            return 0;
                        }
                        return -1;
                    }
                });
                ch.registerCommand("reset", new BasicCommand("reset the CPU", "") {

                    public int executeCommand(CommandContext context) {
                        cpu.reset();
                        return 0;
                    }
                });
                ch.registerCommand("time", new BasicCommand("print the elapse time and cycles", "") {

                    public int executeCommand(CommandContext context) {
                        long time = ((long) (cpu.getTimeMillis()));
                        long wallDiff = System.currentTimeMillis() - lastWall;
                        context.out.println("Emulated time elapsed: " + time + "(ms)  since last: " + (time - lastCall) + " ms" + " wallTime: " + wallDiff + " ms speed factor: " + (wallDiff == 0 ? "N/A" : "" + (time - lastCall) / wallDiff));
                        lastCall = time;
                        lastWall = System.currentTimeMillis();
                        return 0;
                    }
                });
                ch.registerCommand("mem", new BasicCommand("dump memory", "<start address> <num_entries> [type] [hex|char]") {

                    public int executeCommand(final CommandContext context) {
                        int start = context.getArgumentAsAddress(0);
                        if (start < 0) {
                            context.err.println("Illegal start address: " + context.getArgument(0));
                            return 1;
                        }
                        int count = context.getArgumentAsInt(1);
                        int mode = Utils.DEC;
                        int type = Utils.UBYTE;
                        boolean signed = false;
                        if (context.getArgumentCount() > 2) {
                            int pos = 2;
                            int acount = context.getArgumentCount();
                            if (acount > 4) acount = 4;
                            while (pos < acount) {
                                String tS = context.getArgument(pos++);
                                if ("ubyte".equals(tS)) {
                                } else if ("byte".equals(tS)) {
                                    type = Utils.BYTE;
                                } else if ("word".equals(tS)) {
                                    type = Utils.WORD;
                                } else if ("uword".equals(tS)) {
                                    type = Utils.UWORD;
                                } else if ("hex".equals(tS)) {
                                    mode = Utils.HEX;
                                } else if ("char".equals(tS)) {
                                    mode = Utils.ASCII;
                                    type = Utils.BYTE;
                                }
                            }
                        }
                        for (int i = 0; i < count; i++) {
                            int data = 0;
                            data = cpu.memory[start++];
                            if (Utils.size(type) == 2) {
                                data = data + (cpu.memory[start++] << 8);
                            }
                            context.out.print((mode != Utils.ASCII ? " " : "") + Utils.toString(data, type, mode));
                        }
                        context.out.println();
                        return 0;
                    }
                });
                ch.registerCommand("mset", new BasicCommand("set memory", "<address> [type] <value> [value ...]") {

                    public int executeCommand(final CommandContext context) {
                        int count = context.getArgumentCount();
                        int adr = context.getArgumentAsAddress(0);
                        String arg2 = context.getArgument(1);
                        int type = Utils.BYTE;
                        int mode = Utils.DEC;
                        boolean typeRead = false;
                        if (count > 2) {
                            if ("char".equals(arg2)) {
                                mode = Utils.ASCII;
                                typeRead = true;
                            }
                            if ("word".equals(arg2)) {
                                type = Utils.WORD;
                                typeRead = true;
                            }
                        }
                        for (int i = typeRead ? 2 : 1; i < count; i++) {
                            if (mode == Utils.DEC) {
                                int val = context.getArgumentAsInt(i);
                                boolean word = Utils.size(type) == 2 | val > 0xff;
                                try {
                                    cpu.write(adr, val, word);
                                    adr += word ? 2 : 1;
                                } catch (EmulationException e) {
                                    e.printStackTrace(context.out);
                                }
                            } else if (mode == Utils.ASCII) {
                                String data = context.getArgument(i);
                                for (int j = 0; j < data.length(); j++) {
                                    cpu.write(adr++, data.charAt(j) & 0xff, false);
                                }
                            }
                        }
                        return 0;
                    }
                });
                ch.registerCommand("xmem", new BasicCommand("dump flash memory", "<start address> <num_entries> [type]") {

                    public int executeCommand(final CommandContext context) {
                        Memory xmem = (Memory) DebugCommands.this.registry.getComponent("xmem");
                        if (xmem == null) {
                            context.err.println("No xmem component registered");
                            return 0;
                        }
                        int start = context.getArgumentAsAddress(0);
                        int count = context.getArgumentAsInt(1);
                        int size = 1;
                        boolean signed = false;
                        if (context.getArgumentCount() > 2) {
                            String tS = context.getArgument(2);
                            if ("byte".equals(tS)) {
                                signed = true;
                            } else if ("word".equals(tS)) {
                                signed = true;
                                size = 2;
                            } else if ("uword".equals(tS)) {
                                size = 2;
                            }
                        }
                        for (int i = 0; i < count; i++) {
                            int data = 0;
                            data = xmem.readByte(start++);
                            if (size == 2) {
                                data = data + (xmem.readByte(start++) << 8);
                            }
                            context.out.print(" " + data);
                        }
                        context.out.println();
                        return 0;
                    }
                });
                ch.registerCommand("xmset", new BasicCommand("set memory", "<address> <value> [type]") {

                    public int executeCommand(final CommandContext context) {
                        Memory xmem = (Memory) DebugCommands.this.registry.getComponent("xmem");
                        if (xmem == null) {
                            context.err.println("No xmem component registered");
                            return 0;
                        }
                        int adr = context.getArgumentAsAddress(0);
                        int val = context.getArgumentAsInt(1);
                        boolean word = val > 0xff;
                        if (word) {
                            xmem.writeByte(adr, val >> 8);
                            val = val & 0xff;
                            adr++;
                        }
                        xmem.writeByte(adr, val & 0xff);
                        return 0;
                    }
                });
                ch.registerCommand("gdbstubs", new BasicCommand("open up a gdb stubs server for GDB remote debugging", "port") {

                    private GDBStubs stubs = null;

                    public int executeCommand(CommandContext context) {
                        if (stubs != null) {
                            context.err.println("GDBStubs already open");
                        } else {
                            int port = context.getArgumentAsInt(0);
                            stubs = new GDBStubs();
                            stubs.setupServer(cpu, port);
                        }
                        return 0;
                    }
                });
                ch.registerCommand("log", new BasicAsyncCommand("log a loggable object", "[loggable...]") {

                    private Loggable[] loggables = null;

                    @Override
                    public int executeCommand(CommandContext context) {
                        if (context.getArgumentCount() == 0) {
                            Loggable[] loggable = cpu.getLoggables();
                            for (Loggable unit : loggable) {
                                String id = unit.getID();
                                String name = unit.getName();
                                if (id == name) {
                                    context.out.println("  " + id);
                                } else {
                                    context.out.println("  " + id + " (" + name + ')');
                                }
                            }
                            context.exit(0);
                            return 0;
                        }
                        Loggable[] logs = new Loggable[context.getArgumentCount()];
                        for (int i = 0, n = context.getArgumentCount(); i < n; i++) {
                            logs[i] = cpu.getLoggable(context.getArgument(i));
                            if (logs[i] == null) {
                                context.err.println("Can not find loggable '" + context.getArgument(i) + '\'');
                                return 1;
                            }
                        }
                        for (Loggable l : logs) {
                            l.setLogStream(context.out);
                        }
                        this.loggables = logs;
                        return 0;
                    }

                    public void stopCommand(CommandContext context) {
                        if (loggables != null) {
                            for (Loggable l : loggables) {
                                l.clearLogStream();
                            }
                        }
                    }
                });
                ch.registerCommand("trace", new BasicCommand("store a trace of execution positions.", "<trace size | show>") {

                    @Override
                    public int executeCommand(CommandContext context) {
                        if ("show".equals(context.getArgument(0))) {
                            int size = cpu.getTraceSize();
                            DisAsm disAsm = cpu.getDisAsm();
                            for (int i = 0; i < size; i++) {
                                int pc = cpu.getBackTrace(size - 1 - i);
                                DbgInstruction inst = disAsm.getDbgInstruction(pc, cpu);
                                inst.setPos(pc);
                                System.out.println(inst);
                            }
                        } else {
                            cpu.setTrace(context.getArgumentAsInt(0));
                        }
                        return 0;
                    }
                });
                ch.registerCommand("events", new BasicCommand("print event queues", "") {

                    @Override
                    public int executeCommand(CommandContext context) {
                        cpu.printEventQueues(context.out);
                        return 0;
                    }
                });
            }
        }
    }
