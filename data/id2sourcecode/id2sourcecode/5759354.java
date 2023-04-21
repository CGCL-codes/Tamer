        public int Call(lua_State thread) {
            createmeta(thread);
            luaL_Reg[] luaReg = new luaL_Reg[] { new luaL_Reg("close", new io_close()), new luaL_Reg("flush", new io_flush()), new luaL_Reg("input", new io_input()), new luaL_Reg("lines", new io_lines()), new luaL_Reg("open", new io_open()), new luaL_Reg("output", new io_output()), new luaL_Reg("popen", new io_popen()), new luaL_Reg("read", new io_read()), new luaL_Reg("tmpfile", new io_tmpfile()), new luaL_Reg("type", new io_type()), new luaL_Reg("write", new io_write()) };
            newfenv(thread, new io_fclose());
            LuaAPI.lua_replace(thread, LuaAPI.LUA_ENVIRONINDEX);
            LuaAPI.luaL_register(thread, LUA_IOLIBNAME, luaReg);
            newfenv(thread, new io_noclose());
            createstdfile(thread, new FileStruct(), IO_INPUT, "stdin");
            createstdfile(thread, new FileStruct(), IO_OUTPUT, "stdout");
            createstdfile(thread, new FileStruct(), 0, "stderr");
            LuaAPI.lua_pop(thread, 1);
            LuaAPI.lua_getfield(thread, -1, "popen");
            newfenv(thread, new io_pclose());
            LuaAPI.lua_setfenv(thread, -2);
            LuaAPI.lua_pop(thread, 1);
            return 1;
        }
