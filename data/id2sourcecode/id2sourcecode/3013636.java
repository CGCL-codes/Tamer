    public void recalcLightAndTemperature(final ModelParameters params, final int time, final int tileX, final int tileY) {
        final float poleCoeff = 2.0f * Math.abs((float) tileY / (map.height - 1) - 0.5f);
        final float equatorCoeff = 1f - poleCoeff;
        final float dayCoeff = (float) Math.sin((2.0 * time / params.getTicksPerDay() + (double) (2 * tileX) / map.width) * Math.PI) * equatorCoeff;
        final float yearCoeff = (float) -Math.cos((2.0 * time / params.getTicksPerYear() + (double) tileY / map.height) * Math.PI) * (1.0f - 2.0f * Math.abs(poleCoeff - 0.5f));
        final float dayLight = (dayCoeff + 1) / 2;
        light = 0.25f * dayLight + 0.75f * equatorCoeff * dayLight + 0.5f * poleCoeff * yearCoeff;
        final float levelTerm = Math.max(map.seaLevel, elevation) * TEMPERATURE_LEVEL_SCALER;
        final float dayTerm = dayCoeff * TEMPERATURE_DAY_SCALER;
        final float yearTerm = yearCoeff * TEMPERATURE_YEAR_SCALER;
        final float equatorTerm = (float) Math.sqrt(equatorCoeff) * TEMPERATURE_EQUATOR_SCALER;
        final float target = Math.max(TEMPERATURE_CONSTANT + levelTerm + dayTerm + yearTerm + equatorTerm, -TEMPERATURE_KELVIN_OFFSET);
        final float dampening = XMath.clamp((map.seaLevel - elevation) * TEMPERATURE_WATER_DAMPENING_SCALER, 0.2f, LIMIT_TEMPERATURE_DAMPENING);
        temperature = temperature * dampening + target * (1.0f - dampening);
    }
