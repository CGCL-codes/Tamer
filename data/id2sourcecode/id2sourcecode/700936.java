    public void saveGeneral(PrintWriter printwriter) {
        printwriter.println("function initialize()");
        String nclass = gettheClass();
        printwriter.println("me.class = " + nclass);
        printwriter.println("me.recoil = " + recoil);
        double tempd = Double.parseDouble(rounds);
        printwriter.println("me.rounds = " + (int) tempd);
        printwriter.println("me.recharge = " + recharge);
        printwriter.println("me.rof = " + rof);
        if (lasersight) {
            printwriter.println("me.lasersight = true");
        }
        printwriter.println("callbacks.realoding = \"reloading\"");
        printwriter.println("callbacks.onfire = \"onfire\"");
        printwriter.println("callbacks.oninput = \"oninput\"");
        printwriter.println("end");
        printwriter.println("function reloading()");
        printwriter.println("reloadtime = reloadtime + lx.deltatime");
        printwriter.println("if reloadtime > 1 then");
        printwriter.println("sample(\"" + reloadsound + "\"):play()");
        printwriter.println("me.ammo = me.ammo + 1");
        printwriter.println("reloadtime = 0");
        printwriter.println("end");
        printwriter.println("end");
        printwriter.println("function onfire()");
        printwriter.println("if me.ammo > 0 then");
        if (!usebeam) {
            if (!sound.trim().equalsIgnoreCase("")) printwriter.println("sample(\"" + sound + "\"):play()");
            if (!speedvar.trim().equalsIgnoreCase("")) {
                printwriter.println("me:spawn(\"" + proj + "\", " + amount + ", " + xoffset + ", " + yoffset + ", " + angle + ", " + speed + ", " + spread + ")");
            } else {
                printwriter.println("me:spawn(\"" + proj + "\", " + amount + ", " + xoffset + ", " + yoffset + ", " + angle + ",  vector(" + speed + "," + speedvar + "), " + spread + ")");
            }
        } else {
            printwriter.println("me:spawnbeam(" + bxoffset + ", " + byoffset + ", " + bangle + ", colour(" + colour + "), " + length + ", " + damage + ")");
        }
        printwriter.println("me.ammo = me.ammo - 1");
        if (pctrl) {
            printwriter.println("if me.inputfocus == false then");
            printwriter.println("me:focusinput()");
            printwriter.println("end");
        }
        printwriter.println("end");
        printwriter.println("end");
        if (pctrl) {
            printwriter.println("function oninput(input)");
            printwriter.println("if input.movement.x != 0 | input.movement.y != 0 then");
            printwriter.println("for i = 0, i > me.projectiles.count, i++");
            printwriter.println("me.projectiles.get(i):move(input.movement)");
            printwriter.println("end");
            printwriter.println("end");
            printwriter.println("end");
        }
    }
