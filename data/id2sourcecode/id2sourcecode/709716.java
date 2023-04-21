    public void saveGeneral(PrintWriter printwriter) {
        if (puseangle) pangle = "me.owner.angle";
        printwriter.println("function initialize()");
        printwriter.println("me.style = " + getType());
        if (getType().equalsIgnoreCase("projectiles.image")) {
            printwriter.println("me.image.source = surface(\"" + image + "\", true)");
            if (!angleimages.trim().equalsIgnoreCase("") && useangle) printwriter.println("me.image.angles = " + angleimages);
        } else if (getType().equalsIgnoreCase("projectiles.pixel")) {
            if (!colour1.trim().equalsIgnoreCase("")) {
                printwriter.println("me.pixel.colour1 = colour(" + colour1 + ")");
                if (!colour2.trim().equalsIgnoreCase("")) printwriter.println("me.pixel.colour2 = colour(" + colour2 + ")");
            }
        } else if (getType().equalsIgnoreCase("projectiles.line")) {
        }
        printwriter.println("me.trail = " + getTrail());
        printwriter.println("-- me.gravity(" + gravity + ")");
        printwriter.println("-- me.dampening(" + dampening + ")");
        if (!timer.equalsIgnoreCase("")) {
            printwriter.println("me.timers.add(" + timer + ", \"tmr_explode\")");
            istiming = true;
            if (!timervar.trim().equalsIgnoreCase("")) printwriter.println("--me.timervar(" + timervar + ")");
        }
        if (rotating) {
            printwriter.println("me.rotating = true");
            printwriter.println("me.rotincrement = " + rotincrement);
            printwriter.println("me.rotspeed = " + rotspeed);
        }
        if (animating) {
            printwriter.println("me.animating = true");
            printwriter.println("me.animrate = " + animrate);
            printwriter.println("me.animtype = " + getanimtype());
        }
        printwriter.println("callbacks.onhit = \"onhit\"");
        printwriter.println("callbacks.onplayerhit = \"onplayerhit\"");
        printwriter.println("end");
        if (!htype.equalsIgnoreCase("Nothing") && !getH().equalsIgnoreCase("Nothing")) {
            printwriter.println("function onhit()");
            printwriter.println(getH());
            if (!hsound.trim().equalsIgnoreCase("")) {
                printwriter.println("sample(\"" + hsound + "\"):play()");
            }
            if (hprojectiles) {
                if (!pspeedvar.equalsIgnoreCase("")) printwriter.println("me:spawn(\"" + pprojectile + "\", " + pamount + ", " + pxoffset + ", " + pyoffset + ", " + pangle + ", " + pspeed + ", " + pspread + ")"); else printwriter.println("me:spawn(\"" + pprojectile + "\", " + pamount + ", " + pxoffset + ", " + pyoffset + ", " + pangle + ", vector(" + pspeed + "," + pspeedvar + "), " + pspread + ")");
            }
            if (hprojectiles2) {
                if (!p2speedvar.equalsIgnoreCase("")) printwriter.println("me:spawn(\"" + p2projectile + "\", " + p2amount + ", " + p2xoffset + ", " + p2yoffset + ", " + p2angle + ", " + p2speed + ", " + p2spread + ")"); else printwriter.println("me:spawn(\"" + p2projectile + "\", " + p2amount + ", " + p2xoffset + ", " + p2yoffset + ", " + p2angle + ", vector(" + p2speed + "," + p2speedvar + "), " + p2spread + ")");
            }
            if (hprojectiles3) {
                if (!p3speedvar.equalsIgnoreCase("")) printwriter.println("me:spawn(\"" + p3projectile + "\", " + p3amount + ", " + p3xoffset + ", " + p3yoffset + ", " + p3angle + ", " + p3speed + ", " + p3spread + ")"); else printwriter.println("me:spawn(\"" + p3projectile + "\", " + p3amount + ", " + p3xoffset + ", " + p3yoffset + ", " + p3angle + ", vector(" + p3speed + "," + p3speedvar + "), " + p3spread + ")");
            }
            if (hprojectiles4) {
                if (!p4speedvar.equalsIgnoreCase("")) printwriter.println("me:spawn(\"" + p4projectile + "\", " + p4amount + ", " + p4xoffset + ", " + p4yoffset + ", " + p4angle + ", " + p4speed + ", " + p4spread + ")"); else printwriter.println("me:spawn(\"" + p4projectile + "\", " + p4amount + ", " + p4xoffset + ", " + p4yoffset + ", " + p4angle + ", vector(" + p4speed + "," + p4speedvar + "), " + p4spread + ")");
            }
            printwriter.println("end");
        }
        if (!phtype.equalsIgnoreCase("Nothing") && !getPH().equalsIgnoreCase("Nothing")) {
            printwriter.println("function onplayerhit(player)");
            printwriter.println(getPH());
            if (!phsound.trim().equalsIgnoreCase("")) {
                printwriter.println("sample(\"" + phsound + "\"):play()");
            }
            if (phprojectiles) {
                if (!pspeedvar.equalsIgnoreCase("")) printwriter.println("me:spawn(\"" + pprojectile + "\", " + pamount + ", " + pxoffset + ", " + pyoffset + ", " + pangle + ", " + pspeed + ", " + pspread + ")"); else printwriter.println("me:spawn(\"" + pprojectile + "\", " + pamount + ", " + pxoffset + ", " + pyoffset + ", " + pangle + ", vector(" + pspeed + "," + pspeedvar + "), " + pspread + ")");
            }
            if (phprojectiles2) {
                if (!p2speedvar.equalsIgnoreCase("")) printwriter.println("me:spawn(\"" + p2projectile + "\", " + p2amount + ", " + p2xoffset + ", " + p2yoffset + ", " + p2angle + ", " + p2speed + ", " + p2spread + ")"); else printwriter.println("me:spawn(\"" + p2projectile + "\", " + p2amount + ", " + p2xoffset + ", " + p2yoffset + ", " + p2angle + ", vector(" + p2speed + "," + p2speedvar + "), " + p2spread + ")");
            }
            if (phprojectiles3) {
                if (!p3speedvar.equalsIgnoreCase("")) printwriter.println("me:spawn(\"" + p3projectile + "\", " + p3amount + ", " + p3xoffset + ", " + p3yoffset + ", " + p3angle + ", " + p3speed + ", " + p3spread + ")"); else printwriter.println("me:spawn(\"" + p3projectile + "\", " + p3amount + ", " + p3xoffset + ", " + p3yoffset + ", " + p3angle + ", vector(" + p3speed + "," + p3speedvar + "), " + p3spread + ")");
            }
            if (phprojectiles4) {
                if (!p4speedvar.equalsIgnoreCase("")) printwriter.println("me:spawn(\"" + p4projectile + "\", " + p4amount + ", " + p4xoffset + ", " + p4yoffset + ", " + p4angle + ", " + p4speed + ", " + p4spread + ")"); else printwriter.println("me:spawn(\"" + p4projectile + "\", " + p4amount + ", " + p4xoffset + ", " + p4yoffset + ", " + p4angle + ", vector(" + p4speed + "," + p4speedvar + "), " + p4spread + ")");
            }
            printwriter.println("end");
        }
        if (istiming) {
            printwriter.println("function tmr_explode");
            if (!ttype.trim().equalsIgnoreCase("Nothing")) {
                printwriter.println(getT());
                if (tprojectiles) {
                    if (!pspeedvar.equalsIgnoreCase("")) printwriter.println("me:spawn(\"" + pprojectile + "\", " + pamount + ", " + pxoffset + ", " + pyoffset + ", " + pangle + ", " + pspeed + ", " + pspread + ")"); else printwriter.println("me:spawn(\"" + pprojectile + "\", " + pamount + ", " + pxoffset + ", " + pyoffset + ", " + pangle + ", vector(" + pspeed + "," + pspeedvar + "), " + pspread + ")");
                }
                if (tprojectiles2) {
                    if (!p2speedvar.equalsIgnoreCase("")) printwriter.println("me:spawn(\"" + p2projectile + "\", " + p2amount + ", " + p2xoffset + ", " + p2yoffset + ", " + p2angle + ", " + p2speed + ", " + p2spread + ")"); else printwriter.println("me:spawn(\"" + p2projectile + "\", " + p2amount + ", " + p2xoffset + ", " + p2yoffset + ", " + p2angle + ", vector(" + p2speed + "," + p2speedvar + "), " + p2spread + ")");
                }
                if (tprojectiles3) {
                    if (!p3speedvar.equalsIgnoreCase("")) printwriter.println("me:spawn(\"" + p3projectile + "\", " + p3amount + ", " + p3xoffset + ", " + p3yoffset + ", " + p3angle + ", " + p3speed + ", " + p3spread + ")"); else printwriter.println("me:spawn(\"" + p3projectile + "\", " + p3amount + ", " + p3xoffset + ", " + p3yoffset + ", " + p3angle + ", vector(" + p3speed + "," + p3speedvar + "), " + p3spread + ")");
                }
                if (tprojectiles4) {
                    if (!p4speedvar.equalsIgnoreCase("")) printwriter.println("me:spawn(\"" + p4projectile + "\", " + p4amount + ", " + p4xoffset + ", " + p4yoffset + ", " + p4angle + ", " + p4speed + ", " + p4spread + ")"); else printwriter.println("me:spawn(\"" + p4projectile + "\", " + p4amount + ", " + p4xoffset + ", " + p4yoffset + ", " + p4angle + ", vector(" + p4speed + "," + p4speedvar + "), " + p4spread + ")");
                }
                if (!tsound.trim().equalsIgnoreCase("")) {
                    printwriter.println("sample(\"" + tsound + "\"):play()");
                }
            }
            printwriter.println("end");
        }
    }
