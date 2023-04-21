    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
        for (File f : dir.toFile().listFiles()) {
            if (f.isDirectory() || (!f.toString().contains(".js") && !f.toString().contains(".rb") && !f.toString().contains(".py") && !f.toString().contains(".lua"))) continue;
            ScriptManager.addScript(f);
        }
    }
