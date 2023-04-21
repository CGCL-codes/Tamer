    public boolean SetEnv(String entry, String new_string) {
        int env_read = Memory.PhysMake(psp.GetEnvironment(), 0);
        int env_write = env_read;
        String env_string;
        do {
            env_string = Memory.MEM_StrCopy(env_read, 1024);
            if (env_string.length() == 0) break;
            env_read += env_string.length() + 1;
            int pos = env_string.indexOf('=');
            if (pos < 0) continue;
            String key = env_string.substring(0, pos);
            if (key.equalsIgnoreCase(entry)) {
                continue;
            }
            Memory.MEM_BlockWrite(env_write, env_string, env_string.length() + 1);
            env_write += env_string.length() + 1;
        } while (true);
        if (new_string.length() > 0) {
            new_string = entry.toUpperCase() + "=" + new_string;
            Memory.MEM_BlockWrite(env_write, new_string, new_string.length() + 1);
            env_write += new_string.length() + 1;
        }
        Memory.mem_writed(env_write, 0);
        return true;
    }
