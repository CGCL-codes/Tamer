    private void outputNfsV3(String header, Object nfs) {
        println(header + ":");
        printnfs(nfs, new String[] { "null", "getattr", "setattr", "lookup", "access", "readlink" });
        printnfs(nfs, new String[] { "read", "write", "create", "mkdir", "symlink", "mknod" });
        printnfs(nfs, new String[] { "remove", "rmdir", "rename", "link", "readdir", "readdirplus" });
        printnfs(nfs, new String[] { "fsstat", "fsinfo", "pathconf", "commit", "", "" });
        println("");
        flush();
    }
