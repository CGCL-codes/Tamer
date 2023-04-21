        public int Call(lua_State thread) {
            return g_write(thread, getiofile(thread, IO_OUTPUT), 1);
        }
