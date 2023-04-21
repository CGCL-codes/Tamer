        String describe() {
            String s = isWriteAccess() ? "write" : "read";
            s += " from thread: \"";
            s += ti.getName();
            s += "\", holding locks {";
            for (int i = 0; i < locksHeld.length; i++) {
                if (i > 0) s += ',';
                s += locksHeld[i];
            }
            s += "} in ";
            s += finsn.getSourceLocation();
            return s;
        }
