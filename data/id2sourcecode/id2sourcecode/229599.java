    public static void main(String[] args) {
        try {
            out = new FileWriter(System.getProperty("generateToDir") + "/OPT_Assembler.java");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        try {
            lowLevelAsm = Class.forName("VM_Assembler");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        emit("import instructionFormats.*;\n\n");
        emit("\n\n");
        emit("/**\n");
        emit(" *  This class is the automatically-generated assembler for\n");
        emit(" * the optimizing compiler.  It consists of methods that\n");
        emit(" * understand the possible operand combinations of each\n");
        emit(" * instruction type, and how to translate those operands to\n");
        emit(" * calls to the VM_Assember low-level emit method\n");
        emit(" *\n");
        emit(" * @see GenerateAssembler\n");
        emit(" *\n");
        emit(" * @author Julian Dolby\n");
        emit(" * @author {@link GenerateAssembler}\n");
        emit(" */\n");
        emit("class OPT_Assembler extends OPT_AssemblerBase {\n\n");
        emitTab(1);
        emit("/**\n");
        emitTab(1);
        emit(" *  This class requires no special construction;\n");
        emitTab(1);
        emit(" * this constructor simply invokes the\n");
        emitTab(1);
        emit(" * constructor for VM_Assembler\n");
        emitTab(1);
        emit(" *\n");
        emitTab(1);
        emit(" * @see VM_Assembler\n");
        emitTab(1);
        emit(" */\n");
        emitTab(1);
        emit("OPT_Assembler(int bcSize, boolean print) {\n");
        emitTab(2);
        emit("super(bcSize, print);\n");
        emitTab(1);
        emit("}");
        emit("\n\n");
        Method[] emitters = lowLevelAsm.getDeclaredMethods();
        Set opcodes = getOpcodes(emitters);
        Iterator i = opcodes.iterator();
        while (i.hasNext()) {
            String opcode = (String) i.next();
            setCurrentOpcode(opcode);
            emitTab(1);
            emit("/**\n");
            emitTab(1);
            emit(" *  Emit the given instruction, assuming that\n");
            emitTab(1);
            emit(" * it is a " + currentFormat + " instruction\n");
            emitTab(1);
            emit(" * and has a " + currentOpcode + " operator\n");
            emitTab(1);
            emit(" *\n");
            emitTab(1);
            emit(" * @param inst the instruction to assemble\n");
            emitTab(1);
            emit(" */\n");
            emitTab(1);
            emit("private void do" + opcode + "(OPT_Instruction inst) {\n");
            EmitterSet emitter = buildSetForOpcode(emitters, opcode);
            boolean[][] tp = new boolean[4][encoding.length];
            emitter.emitSet(opcode, tp, 2);
            emitTab(1);
            emit("}\n\n");
        }
        emitTab(1);
        emit("/**\n");
        emitTab(1);
        emit(" *  The number of instructions emitted so far\n");
        emitTab(1);
        emit(" */\n");
        emitTab(1);
        emit("private int instructionCount = 0;\n\n");
        emitTab(1);
        emit("/**\n");
        emitTab(1);
        emit(" *  Assemble the given instruction\n");
        emitTab(1);
        emit(" *\n");
        emitTab(1);
        emit(" * @param inst the instruction to assemble\n");
        emitTab(1);
        emit(" */\n");
        emitTab(1);
        emit("void doInst(OPT_Instruction inst) {\n");
        emitTab(2);
        emit("resolveForwardReferences(++instructionCount);\n");
        emitTab(2);
        emit("switch (inst.getOpcode()) {\n");
        Set emittedOpcodes = new HashSet();
        i = opcodes.iterator();
        while (i.hasNext()) {
            String opcode = (String) i.next();
            Iterator operators = getMatchingOperators(opcode).iterator();
            while (operators.hasNext()) {
                Object operator = operators.next();
                emitTab(3);
                emittedOpcodes.add(operator);
                emit("case IA32_" + operator + "_opcode:\n");
            }
            emitTab(4);
            emit("do" + opcode + "(inst);\n");
            emitTab(4);
            emit("break;\n");
        }
        emittedOpcodes.add("LOCK");
        emitTab(3);
        emit("case IA32_LOCK_opcode:\n");
        emitTab(4);
        emit("emitLockNextInstruction();\n");
        emitTab(4);
        emit("break;\n");
        Set errorOpcodes = getErrorOpcodes(emittedOpcodes);
        if (!errorOpcodes.isEmpty()) {
            i = errorOpcodes.iterator();
            while (i.hasNext()) {
                emitTab(3);
                emit("case IA32_" + i.next() + "_opcode:\n");
            }
            emitTab(4);
            emit("throw new OPT_OptimizingCompilerException(inst + \" has unimplemented IA32 opcode (check excludedOpcodes)\");\n");
        }
        emitTab(2);
        emit("}\n");
        emitTab(2);
        emit("inst.setmcOffset( mi );\n");
        emitTab(1);
        emit("}\n\n");
        emit("\n}\n");
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
