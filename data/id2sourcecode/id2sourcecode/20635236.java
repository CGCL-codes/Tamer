    private FuncionPorTramosVectorial creaFuncionColoresBandas(int minColores, int maxColores, Vector3d[] colores, Vector3d colorExtremos, Random random) {
        FuncionPorTramosVectorial funcionColores = new FuncionPorTramosVectorial();
        int numColores = minColores + random.nextInt(maxColores - minColores);
        if (numColores > 0) {
            if (colorExtremos != null) {
                funcionColores.nuevoPunto(0.0d, colorExtremos);
            } else {
                funcionColores.nuevoPunto(0.0d, colorBanda(colores, random));
            }
            if (numColores > 1) {
                funcionColores.nuevoPunto(1.0d, colorBanda(colores, random));
                if (numColores > 2) {
                    double lat = 0.0d;
                    double dLat = 1.0d / (numColores - 2);
                    for (int i = 2; i < numColores - 1; i++) {
                        lat += dLat + random.nextDouble() * 4.0d;
                        funcionColores.nuevoPunto(lat, colorBanda(colores, random));
                    }
                    lat += dLat + random.nextDouble() * 4.0d;
                    if (colorExtremos != null) {
                        funcionColores.nuevoPunto(lat, colorExtremos);
                    } else {
                        funcionColores.nuevoPunto(lat, colorBanda(colores, random));
                    }
                }
            }
            funcionColores.normalizarX();
        }
        return funcionColores;
    }
