package com.test.itstesttime;

public class Layer {

    private double[][] layerContents;

    public Layer(int size)
    {
        layerContents = new double[size][1];
    }

    public void setLayerContents(double[][] newContents)
    {
        layerContents = newContents;
    }

    public double[][] getLayerContents()
    {
        return layerContents;
    }
}
