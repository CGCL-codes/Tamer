        public ColorSwitch(Animation anim) {
            super(anim);
            bOn = true;
            active = true;
            touched = false;
            timeWaited = 0;
            affectedHeight = 9;
            affectedWidth = 7;
            originOffsetX = (affectedWidth + 1) / 2;
            originOffsetY = (affectedHeight + 1) / 2;
        }
