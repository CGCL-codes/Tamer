    @Override
    public void resolve() {
        if (isBroken) {
            return;
        }
        float ang12 = (float) Math.atan2(particle2.position.y - particle1.position.y, particle2.position.x - particle1.position.x);
        float ang23 = (float) Math.atan2(particle3.position.y - particle2.position.y, particle3.position.x - particle2.position.x);
        float angDiff = ang12 - ang23;
        while (angDiff > MathUtil.PI) {
            angDiff -= MathUtil.TWO_PI;
        }
        while (angDiff < -MathUtil.PI) {
            angDiff += MathUtil.TWO_PI;
        }
        float sumInvMass = particle1.getInvMass() + particle2.getInvMass();
        float mult1 = particle1.getInvMass() / sumInvMass;
        float mult2 = particle2.getInvMass() / sumInvMass;
        float angChange = 0;
        float lowMid = (maxAngle - minAngle) / 2;
        float highMid = (maxAngle + minAngle) / 2;
        float breakAng = (maxBreakAngle - minBreakAngle) / 2;
        float newDiff = highMid - angDiff;
        while (newDiff > MathUtil.PI) {
            newDiff -= MathUtil.TWO_PI;
        }
        while (newDiff < -MathUtil.PI) {
            newDiff += MathUtil.TWO_PI;
        }
        if (newDiff > lowMid) {
            if (newDiff > breakAng) {
                float diff = newDiff - breakAng;
                isBroken = true;
                return;
            }
            angChange = newDiff - lowMid;
        } else if (newDiff < -lowMid) {
            if (newDiff < -breakAng) {
                float diff2 = newDiff + breakAng;
                isBroken = true;
                return;
            }
            angChange = newDiff + lowMid;
        }
        float finalAng = angChange * this.stiffness + ang12;
        float displaceX = particle1.position.x + (particle2.position.x - particle1.position.x) * mult1;
        float displaceY = particle1.position.y + (particle2.position.y - particle1.position.y) * mult1;
        particle1.position.x = displaceX + (float) Math.cos(finalAng + MathUtil.PI) * restLength * mult1;
        particle1.position.y = displaceY + (float) Math.sin(finalAng + MathUtil.PI) * restLength * mult1;
        particle2.position.x = displaceX + (float) Math.cos(finalAng) * restLength * mult2;
        particle2.position.y = displaceY + (float) Math.sin(finalAng) * restLength * mult2;
    }
