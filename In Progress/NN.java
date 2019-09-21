import java.util.Arrays;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;

public class NN {

    private static BufferedReader csvReader;
    private static BufferedReader csvTestReader;

    public static void main(String[] args)
    {
        try{
            File weights = new File("C:\\Users\\wolod\\Documents\\Programming\\Java\\Neural network\\weights.csv");
            final String pathToTrainCsv = "C:\\Users\\wolod\\Documents\\Programming\\Java\\Neural network\\mnist_test.csv";
            final String pathToTestCsv = "C:\\Users\\wolod\\Documents\\Programming\\Java\\Neural network\\mnist_train.csv";
            final String pathToWeightsCsv = "C:\\Users\\wolod\\Documents\\Programming\\Java\\Neural network\\weights.csv";
            //csvReader = new BufferedReader(new FileReader(pathToTrainCsv));
            double[][] X = new double[784][1]; // insert the pixels of the photo;
            double[][] Y = new double[10][1]; // labels
            double alpha = 0.000025;
            int m = 1000;
            int hiddenLayerNodes = 50;
            int outputLayerNodes = 10;
            int inputLayerSize = 784;
            double[][] Theta1 = np.random(hiddenLayerNodes, 784);
            double[][] b1 = new double[hiddenLayerNodes][1];
            double lambda= 3;
            double[][] Theta2 = np.random(hiddenLayerNodes, hiddenLayerNodes);
            double[][] b2 = new double[hiddenLayerNodes][1];
            double[][] Theta3 = np.random(outputLayerNodes, hiddenLayerNodes);
            double[][] b3 = new double[outputLayerNodes][1];
            double error = 0;
            double[][] dTheta2;
            double[][] dTheta1;
            double[][] dTheta3;
            double[][] Delta1 = fillMatrixZeros(new double[Theta1.length][Theta1[0].length]);
            double[][] Delta2 = fillMatrixZeros(new double[Theta2.length][Theta2[0].length]);
            double[][] Delta3 = fillMatrixZeros(new double[Theta3.length][Theta3[0].length]);
            double[][] A1, A2, A3, Z1, Z2, Z3, Z4;
            double[][] A4 = new double[10][1];
            double[][] dZ4;
            double[][] db3 = new double[Delta3.length][Delta3[0].length];
            double[][] db2 = new double[Delta2.length][Delta2[0].length];
            double[][] db1 = new double[Delta1.length][Delta1[0].length];
            String[] data;
            double[][] theta3;
            String row;
            double cost;
            int epochs = 30;
            if(!weights.exists())
            {
                theta3 = Theta3;
                for(int j = 0; j < epochs; j++)
                {
                    theta3 = Theta3;
                    csvReader = new BufferedReader(new FileReader(pathToTrainCsv));
                    for (int i = 0; i < 100 && ((csvReader.ready())); i++) {
                        // Foward Prop
                        // LAYER 1
                        cost = 0;
                        for(int k = 0; k < m && ((row = csvReader.readLine()) != null); k++)
                        {

                            data = row.split(",");
                            X = stringArraytoNPArray(data, 784, 1);
                            Y = valtoNPArray(Integer.parseInt(data[0]));
                            A1 = X;
                            Z2 = np.add(np.dot(Theta1, X), b1);
                            A2 = np.sigmoid(Z2);

                                //LAYER 2
                            Z3 = np.add(np.dot(Theta2, A2), b2);
                            A3 = np.sigmoid(Z3);
                            // LAYER 3
                            Z4 = np.add(np.dot(Theta3, A3), b3);
                            A4 = np.sigmoid(Z4);
                            cost += np.cross_entropy(m, Y, A4) + lambda * (regularizeMatrix(Theta3) +  regularizeMatrix(Theta1) + regularizeMatrix(Theta2)) / 2 * m;

                            //costs.getData().add(new XYChart.Data(i, cost));

                            //Back Prop
                            //LAYER 2

                            //double[][] dZ3 = np.subtract(A3, Y);
                            //dTheta2 = np.multiply(np.dot(np.T(Theta2),dZ3), sigoidGradient(Z2));
                            dZ4 = np.subtract(A4, Y);
                            dTheta3 = np.multiply(np.dot(np.T(Theta3),dZ4), sigoidGradient(Z3));
                            Delta3 = np.add(Delta3,np.dot(dZ4,np.T(A3)));
                            //double[][] dTheta2 = np.divide(np.dot(dZ2, np.T(A1)), m);
                            db3 = np.divide(Delta3, m);

                            ///////////////////////////////////
                            dTheta2 = np.multiply(np.dot(np.T(Theta2),dTheta3), sigoidGradient(Z2));
                            Delta2 = np.add(Delta2,np.dot(dTheta3, np.T(A2)));
                            db2 = np.divide(Delta2, m);
                            //////////////////////////////////
                            //dTheta1 = np.multiply(np.dot(np.T(Theta1), dTheta2), sigoidGradient(Z1));
                            Delta1 = np.add(Delta1, np.dot(dTheta2, np.T(A1)));

                            //LAYER 1
                            //dTheta1 = np.multiply(np.dot(np.T(Theta2), dTheta2), np.subtract(1.0, np.power(A2, 2)));
                            //dTheta1 = np.multiply(np.dot(np.T(Theta1),dTheta2), sigoidGradient(Z1));

                            Delta1 = np.add(Delta1, np.dot(dTheta2, np.T(A1)));
                            db1 = np.divide(Delta1, m);
                            Delta1 = np.add(Delta1, np.multiply(lambda,Theta1));
                            Delta2 = np.add(Delta2, np.multiply(lambda,Theta2));
                        }
                        System.out.println(cost);
                        // G.D
                        Theta1 = np.subtract(Theta1, np.multiply(alpha, Delta1));
                        b1 = np.subtract(b1, np.multiply(alpha, db1));
                        Theta2 = np.subtract(Theta2, np.multiply(alpha, Delta2));
                        b2 = np.subtract(b2, np.multiply(alpha, db2));
                        Theta3 = np.subtract(Theta3, np.multiply(alpha, Delta3));
                        b3 = np.subtract(b3, np.multiply(alpha, db3));

                        if (i < 10) {
                            System.out.println("==============");
                            System.out.println("Cost = " + cost);
                            //System.out.println("Predictions = " + Arrays.deepToString(A3));
                            System.out.print(findPrediction(Y));
                            System.out.println(findPrediction(A4));
                        }
                        Delta1 = fillMatrixZeros(Delta1);
                        Delta2 = fillMatrixZeros(Delta2);
                      }
                  }
                }
              else
              {
                csvReader = new BufferedReader(new FileReader(pathToWeightsCsv));
                // load the matrices with the weight extracted from the csv files
                Theta1 = np.T(Theta1);
                //Theta3 = np.T(Theta3);
                int counter = 1;
                for(int i = 0; i < 784; i++)
                {
                  row = csvReader.readLine();
                  //System.out.println(Theta1.length);
                  Theta1[i] = readWeights(row,50);
                  counter ++;
                }
                System.out.println(Theta2.length);
                for(int i = 0; i < 50; i++)
                {
                  row = csvReader.readLine();
                  Theta2[i] = readWeights(row,50);
                  counter ++;
                }
                csvReader.readLine();
                counter++;
                System.out.println(counter);
                for(int i = 0; i < 10; i++)
                {
                  row = csvReader.readLine();
                  Theta3[i] = readWeights(row,50);
                }
                Theta1 = np.T(Theta1);
                System.out.println(np.shape(Theta3));
                theta3 = np.T(Theta3);
              }

            csvTestReader = new BufferedReader(new FileReader(pathToTestCsv));
            for (int i = 0; i < 10000 && ((row = csvTestReader.readLine()) != null); i++) {
                // Foward Prop
                cost = 0;

                data = row.split(",");
                X = stringArraytoNPArray(data, 784, 1);
                Y = valtoNPArray(Integer.parseInt(data[0]));
                A1 = X;
                Z2 = np.add(np.dot(Theta1, X), b1);
                A2 = relu(Z2);

                    //LAYER 2
                Z3 = np.add(np.dot(Theta2, A2), b2);
                A3 = relu(Z3);
                // LAYER 3
                Z4 = np.add(np.dot(Theta3, A3), b3);
                A4 = np.softmax(Z4);
                cost = np.cross_entropy(m, Y, A4);
                if(i < 100)
                {
                  System.out.println("" + findPrediction(A4) + findPrediction(Y));
                  for(int x = 0; x < A4.length; x++)
                    System.out.print(A4[x][0] + " ");
                  System.out.println();
                }
                if(findPrediction(Y) == findPrediction(A4))
                  error++;
              }
            System.out.println("accuracy = " + error/100);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

    }

    public static double[][] stringArraytoNPArray(String[] line, int a, int b)
    {
        double[][] returnArray = new double[a][b];
        for(int a2 = 0; a2 < a; a2++)
          for(int b2 = 0; b2 < b; b2++)
            returnArray[a2][b2] = Integer.parseInt(line[1 + a2 + b2]);
        return normalize(returnArray);
    }

    public static double[][] valtoNPArray(int value)
    {
      double[][] rArray = new double[10][1];
      for(int c = 0; c < 10; c++)
      {
          if(c == value)
            rArray[c][0] = 1;
          else
            rArray[c][0] = 0;
      }
      return rArray;
    }

    public static int findPrediction(double[][] matrix)
    {
      // finding a prediction from the vector
      int currentPrediction = -1;
      double highestProb = 0;
      for(int a = 0; a < matrix.length; a++)
        for(int b = 0; b < matrix[a].length; b++)
          if(matrix[a][b] > highestProb)
          {
            currentPrediction = a % 10;
            highestProb = matrix[a][b];
          }
      return currentPrediction;
    }

    public static double[][] sigoidGradient(double[][] matrix)
    {
        double[][] sigMatrix = np.sigmoid(matrix);
        double[][] augSigMatrix = np.subtract(1,sigMatrix);
        double[][] returnMatrix = new double[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++)
          for(int j = 0; j < matrix[i].length; j++)
            returnMatrix[i][j] = sigMatrix[i][j] * augSigMatrix[i][j];

        return returnMatrix;
    }

    public static double[][] elemMatMuliply(double[][] a, double[][] b)
    {
        double[][] returnMatrix = new double[a.length][a[0].length];
        for(int i = 0; i < a.length; i++)
          for(int j = 0; j < a[i].length; j++)
            returnMatrix[i][j] = a[i][j] * b[i][j];

        return returnMatrix;
    }

    public static double[][] fillMatrixZeros(double[][] a)
    {
        double[][] zeros = new double[a.length][a[0].length];
        for(int i = 0; i < a.length; i++)
          for(int j = 0; j < a[0].length; j++)
            zeros[i][j] = 0;
        return zeros;
    }

    public static double regularizeMatrix(double[][] a)
    {
        double r = 0;
        for(int i = 0; i < a.length; i++)
          for(int j = 0; j < a[0].length; j++)
            r += a[i][j] * a[i][j];
        return r;
    }

    public static double[][] normalize(double[][] a)
    {
        double mean = 0;
        for(int i = 0; i < a.length; i++)
          for(int j = 0; j < a[0].length; j++)
            mean += a[i][j];
        mean /= 784;

        for(int i = 0; i < a.length; i++)
          for(int j = 0; j < a[0].length; j++)
            a[i][j] = (a[i][j] - mean) / 255;

        return a;
    }

    public static double[] readWeights(String s, int size)
    {
        double[] r = new double[size];
        String[] arr = s.split(",");
        //System.out.println(arr.length);
        for(int x = 0; x < arr.length; x++)
        {
          //System.out.println(arr[x]);
          r[x] = Double.parseDouble(arr[x]);
        }
        return r;
    }

    public static double[][] relu(double[][] a)
    {
        double[][] r = new double[a.length][a[0].length];
        for(int i = 0; i < a.length; i++)
          for(int j = 0; j < a[0].length; j++)
            r[i][j] = Math.max(0, a[i][j]);

        return r;
    }

}
