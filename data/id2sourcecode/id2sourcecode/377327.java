    public final void cycle(int cv) {
        cycles += cv;
        if (cycles > cyclesLeft) {
            div -= cyclesLeft;
            if (div < 0) {
                div = 0xFF * 0x100;
            }
            if (timerOn) {
                timeCounter += cyclesLeft;
                if (timeCounter >= timeLimit) {
                    timeCounter = 0;
                    tima++;
                }
                if (tima > 255) {
                    tima = tma;
                    IF |= INT_TIMER;
                }
            }
            {
                IF |= net.cycle(cyclesLeft);
            }
            if (video.lcd_on) {
                IF |= video.cycle(cyclesLeft, this);
            }
            if (!hdmaDone) {
                if (hdmaLastMode != PgbVideo.STAT_HBLANK && (byte) (video.getStat() & 0x03) == PgbVideo.STAT_HBLANK) {
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    write(hdmaDst++, read(hdmaSrc++));
                    if (hdmaDst >= hdmaStop) {
                        hdmaDone = true;
                    }
                }
                hdmaLastMode = (byte) (video.getStat() & 0x03);
            }
            cycles -= cyclesLeft;
            recalcCyclesLeft();
        }
    }
