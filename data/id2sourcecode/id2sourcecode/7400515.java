    public static void gpuInputCommand(int address, int val) {
        System.out.println("Called @" + Integer.toHexString(address) + "with val " + val);
        pvrRegs[address] = val;
        switch(address) {
            case PVR_ISP_START:
                System.out.println("Called TA render");
                TileAccelarator.render();
                break;
            case PVR_OPB_CFG:
                TileAccelarator.ppblocksize(val);
                break;
            case PVR_TA_INIT:
                System.out.println("disabling framebuffer");
                pvr_framebufferdisplay = false;
                Memory.write32(0xa05f8138, Memory.read32(0xa05f8128));
                break;
            case 0x0044:
                {
                    boolean reinit = false;
                    pvr_fb_r_ctrl = val;
                    if ((val & 0x02) != 0) System.out.println("line doubling enable\r\n");
                    switch((val >> 2) & 0x3) {
                        case 0x00:
                            System.out.println("ARGB0555\r\n");
                            if (screenbits != 16 || screenformat != FRAMEBUFFER_ARGB0555) reinit = true;
                            screenbits = 16;
                            screenformat = FRAMEBUFFER_ARGB0555;
                            FrameBufferFormat.internal_format = GL11.GL_RGBA;
                            FrameBufferFormat.format = GL11.GL_RGBA;
                            FrameBufferFormat.type = GL12.GL_UNSIGNED_SHORT_5_5_5_1;
                            break;
                        case 0x01:
                            System.out.println("RGB565\r\n");
                            if (screenbits != 16 || screenformat != FRAMEBUFFER_RGB565) reinit = true;
                            screenbits = 16;
                            screenformat = FRAMEBUFFER_RGB565;
                            FrameBufferFormat.internal_format = GL11.GL_RGB;
                            FrameBufferFormat.format = GL11.GL_RGB;
                            FrameBufferFormat.type = GL12.GL_UNSIGNED_SHORT_5_6_5;
                            break;
                        case 0x02:
                            System.out.println("RGB888\r\n");
                            if (screenbits != 24 || screenformat != FRAMEBUFFER_RGB888) reinit = true;
                            screenbits = 24;
                            screenformat = FRAMEBUFFER_RGB888;
                            FrameBufferFormat.internal_format = GL11.GL_RGBA;
                            FrameBufferFormat.format = GL11.GL_RGBA;
                            FrameBufferFormat.type = GL11.GL_UNSIGNED_BYTE;
                            break;
                        case 0x03:
                            System.out.println("ARGB0888\r\n");
                            if (screenbits != 32 || screenformat != FRAMEBUFFER_ARGB0888) reinit = true;
                            screenbits = 32;
                            screenformat = FRAMEBUFFER_ARGB0888;
                            FrameBufferFormat.internal_format = GL11.GL_RGB;
                            FrameBufferFormat.format = EXTBgra.GL_BGRA_EXT;
                            FrameBufferFormat.type = GL12.GL_UNSIGNED_INT_8_8_8_8_REV;
                            break;
                    }
                    if ((val & 0x00800000) != 0) System.out.println("pixel clock double enable\r\n");
                    System.out.println("screenbits: " + screenbits);
                    if ((val & 0x01) != 0) {
                        System.out.println("bitmap display enable\r\n");
                        pvr_framebufferdisplay = true;
                        if (reinit) screeninit();
                    } else pvr_framebufferdisplay = false;
                }
                break;
            case PVR_FB_CFG_2:
                switch(val & 0x7) {
                    case 0:
                        System.out.println("fb_packmode: 0555KRGB\n");
                        break;
                    case 1:
                        System.out.println("fb_packmode: 565RGB\n");
                        break;
                    case 2:
                        System.out.println("fb_packmode: 4444ARGB\n");
                        break;
                    case 3:
                        System.out.println("fb_packmode: 1555ARGB\n");
                        break;
                    case 4:
                        System.out.println("fb_packmode: 888RGB\n");
                        break;
                    case 5:
                        System.out.println("fb_packmode: 0888KRGB\n");
                        break;
                    case 6:
                        System.out.println("fb_packmode: 8888ARGB\n");
                        break;
                    case 7:
                        System.out.println("fb_packmode: reserved\n");
                        break;
                }
                break;
            case 0x0050:
                FrameBufferAddress = val;
                break;
            case 0x005C:
                screenwidth = (val & 0x3FF) + 1;
                System.out.println("Screenwidth (in 32 bits units) " + screenwidth);
                screenheight = ((val >> 10) & 0x3FF) + 1;
                System.out.println("screenheight: " + screenheight);
                if (screenheight == 0) {
                    System.out.println("Invalid value.Choosing default values.\n");
                    screenheight = 480;
                    screenwidth = 640;
                    screenbits = 16;
                }
                screeninit();
                break;
        }
    }
