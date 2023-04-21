    public static void auxsort(lua_State thread, int l, int u) {
        while (l < u) {
            int i, j;
            LuaAPI.lua_rawgeti(thread, 1, l);
            LuaAPI.lua_rawgeti(thread, 1, u);
            if (sort_comp(thread, -1, -2)) {
                set2(thread, l, u);
            } else {
                LuaAPI.lua_pop(thread, 2);
            }
            if (u - l == 1) {
                break;
            }
            i = (l + u) / 2;
            LuaAPI.lua_rawgeti(thread, 1, i);
            LuaAPI.lua_rawgeti(thread, 1, l);
            if (sort_comp(thread, -2, -1)) {
                set2(thread, i, l);
            } else {
                LuaAPI.lua_pop(thread, 1);
                LuaAPI.lua_rawgeti(thread, 1, u);
                if (sort_comp(thread, -1, -2)) {
                    set2(thread, i, u);
                } else {
                    LuaAPI.lua_pop(thread, 2);
                }
            }
            if (u - l == 2) {
                break;
            }
            LuaAPI.lua_rawgeti(thread, 1, i);
            LuaAPI.lua_pushvalue(thread, -1);
            LuaAPI.lua_rawgeti(thread, 1, u - 1);
            set2(thread, i, u - 1);
            i = l;
            j = u - 1;
            for (; ; ) {
                LuaAPI.lua_rawgeti(thread, 1, ++i);
                while (sort_comp(thread, -1, -2)) {
                    if (i > u) {
                        LuaAPI.luaL_error(thread, "invalid order function for sorting");
                    }
                    LuaAPI.lua_pop(thread, 1);
                    LuaAPI.lua_rawgeti(thread, 1, ++i);
                }
                LuaAPI.lua_rawgeti(thread, 1, --j);
                while (sort_comp(thread, -3, -1)) {
                    if (j < l) {
                        LuaAPI.luaL_error(thread, "invalid order function for sorting");
                    }
                    LuaAPI.lua_pop(thread, 1);
                    LuaAPI.lua_rawgeti(thread, 1, --j);
                }
                if (j < i) {
                    LuaAPI.lua_pop(thread, 3);
                    break;
                }
                set2(thread, i, j);
            }
            LuaAPI.lua_rawgeti(thread, 1, u - 1);
            LuaAPI.lua_rawgeti(thread, 1, i);
            set2(thread, u - 1, i);
            if (i - l < u - i) {
                j = l;
                i = i - 1;
                l = i + 2;
            } else {
                j = i + 1;
                i = u;
                u = j - 2;
            }
            auxsort(thread, j, i);
        }
    }
