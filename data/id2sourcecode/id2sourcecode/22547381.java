        public ColorProfileDesc execute() {
            log.debug("CreateProfile#execute: " + name);
            ODMGXAWrapper txw = new ODMGXAWrapper();
            ColorProfileDesc p = new ColorProfileDesc();
            txw.lock(p, Transaction.WRITE);
            p.setName(name);
            p.setDescription(description);
            VolumeBase defvol = VolumeBase.getDefaultVolume();
            File f = defvol.getFilingFname(profileFile);
            log.debug("Copying to default volume: " + f.getAbsolutePath());
            try {
                FileUtils.copyFile(profileFile, f);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            byte[] hash = FileUtils.calcHash(f);
            p.setHash(hash);
            txw.flush();
            ColorProfileInstance i = new ColorProfileInstance();
            i.fname = defvol.mapFileToVolumeRelativeName(f);
            i.volumeId = defvol.getName();
            i.profileId = p.id;
            p.addInstance(i);
            txw.lock(i, Transaction.WRITE);
            txw.commit();
            return p;
        }
