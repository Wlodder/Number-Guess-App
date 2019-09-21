package com.test.itstesttime;

public class NeuralNetwork {

    private Layer inputLayer;
    private Layer outputLayer;
    public double[][] Theta1;
    public double[][] Theta2;
    private int prediction;

    // for the forward pass algorithm
    private double[][] z1, a1, z2, a2, z3, a3;


    // for the backward pass
    private int J = 0;

    public NeuralNetwork(int inputSize, int hiddenLayerSize, int outputSize)
    {
        inputLayer = new Layer(inputSize);
        outputLayer = new Layer(outputSize);
    }

    public void loadInput(double[][] newInput)
    {
        inputLayer.setLayerContents(newInput);
    }

    public int forwardPass(double[][] weights1, double[][] weights2)
    {
        z1 = np.multiply(np.T(weights1), inputLayer.getLayerContents());
        a1 = np.sigmoid(z1);
        z2 = np.multiply(a1, weights2);
        return predict(z2);
    }

    public int predict(double[][] outputVector)
    {
        double highestProb = 0.0;
        int currentGuess = -1;

        for(int i = 0; i < outputVector.length; i++)
        {
            for (int j = 0; j < outputVector[i].length; j++)
            {
                if(outputVector[i][j] > highestProb)
                    currentGuess = i + 1;
            }
        }
        return currentGuess;
    }

    public void computeCost()
    {

    }
}
