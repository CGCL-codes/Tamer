    public void vecProd(double[] ds, int i, double[] ds_11_, int i_12_, double[] ds_13_, int i_14_) {
        ds_13_[i_14_] = ds[i + 1] * ds_11_[i_12_ + 2] - ds[i + 2] * ds_11_[i_12_ + 1];
        ds_13_[i_14_ + 1] = ds[i + 2] * ds_11_[i_12_] - ds[i] * ds_11_[i_12_ + 2];
        ds_13_[i_14_ + 2] = ds[i] * ds_11_[i_12_ + 1] - ds[i + 1] * ds_11_[i_12_];
    }
