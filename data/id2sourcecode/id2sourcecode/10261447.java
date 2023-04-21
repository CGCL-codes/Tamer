    static void writeLocation(lejos.nxt.debug.PacketStream ps, VM.VMMethod method, int pc) {
        int methodId = method.getMethodNumber();
        VM.VMClasses classes = VM.getVM().getImage().getVMClasses();
        int typeTag = 0;
        int classId = 0;
        int low = 0;
        int high = VM.getVM().getImage().lastClass & 0xFF;
        while (low <= high) {
            classId = (low + high) / 2;
            VM.VMClass cls = classes.get(classId);
            VMMethods methods = cls.getMethods();
            while (methods.size() == 0) {
                if (classId > ((VM.getVM().getImage().lastClass & 0xFF) * 3 / 4)) {
                    classId--;
                } else {
                    classId++;
                }
                cls = classes.get(classId);
                methods = cls.getMethods();
            }
            int firstClassMethod = methods.get(0).getMethodNumber();
            if (firstClassMethod > methodId) high = classId - 1; else if ((firstClassMethod + methods.size()) <= methodId) low = classId + 1; else {
                typeTag = getTypeTagFromClass(cls);
                break;
            }
        }
        ps.writeByte(typeTag);
        ps.writeClassId(classId);
        ps.writeMethodId(methodId);
        ps.writeLong(pc);
    }
