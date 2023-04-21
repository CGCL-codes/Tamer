        public int Call(lua_State thread) {
            luaL_Buffer b = new luaL_Buffer();
            LuaAPI.luaL_checktype(thread, 1, LuaAPI.LUA_TFUNCTION);
            LuaAPI.lua_settop(thread, 1);
            LuaAPI.luaL_buffinit(thread, b);
            if (LuaAPI.lua_dump(thread, new writer(), b) != 0) {
                LuaAPI.luaL_error(thread, "unable to dump given function");
            }
            LuaAPI.luaL_pushresult(b);
            return 1;
        }
