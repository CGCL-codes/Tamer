        public int Call(lua_State thread) {
            return g_write(thread, tofile(thread), 2);
        }
