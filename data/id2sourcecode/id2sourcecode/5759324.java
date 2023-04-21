    static int g_write(lua_State thread, FileStruct f, int arg) {
        int nargs = LuaAPI.lua_gettop(thread) - 1;
        int status = 1;
        for (; (nargs--) > 0; arg++) {
            if (LuaAPI.lua_type(thread, arg) == LuaAPI.LUA_TNUMBER) {
                String str = "";
                try {
                    str = new Printf("%.14g").sprintf(LuaAPI.lua_tonumber(thread, arg));
                } catch (Exception ex) {
                    status = 0;
                }
                status = status == 1 && f.fwrite(str, 1, str.length()) > 0 ? 1 : 0;
            } else {
                String s = LuaAPI.luaL_checklstring(thread, arg);
                int iStrLen = LuaStringLib.StringLength(s);
                status = status == 1 && (f.fwrite(s, 1, iStrLen) == iStrLen) ? 1 : 0;
            }
        }
        return pushresult(thread, status, null);
    }
