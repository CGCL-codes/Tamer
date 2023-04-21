    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
        Entity entity = par1DamageSource.getEntity();
        aiSit.func_48407_a(false);
        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
            par2 = (par2 + 1) / 2;
        }
        return super.attackEntityFrom(par1DamageSource, par2);
    }
