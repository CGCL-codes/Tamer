    static void createmeta(lua_State thread) {
        luaL_Reg[] luaReg = new luaL_Reg[] { new luaL_Reg("close", new io_close()), new luaL_Reg("flush", new f_flush()), new luaL_Reg("lines", new f_lines()), new luaL_Reg("read", new f_read()), new luaL_Reg("seek", new f_seek()), new luaL_Reg("setvbuf", new f_setvbuf()), new luaL_Reg("write", new f_write()), new luaL_Reg("__gc", new io_gc()), new luaL_Reg("__tostring", new io_tostring()) };
        LuaAPI.luaL_newmetatable(thread, LuaAPI.LUA_FILEHANDLE);
        LuaAPI.lua_pushvalue(thread, -1);
        LuaAPI.lua_setfield(thread, -2, "__index");
        LuaAPI.luaL_register(thread, null, luaReg);
    }
