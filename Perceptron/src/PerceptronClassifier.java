import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PerceptronClassifier {
    private static List<IrisExample> readData(String filename) throws IOException {
        List<IrisExample> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            List<Double> attributes = new ArrayList<>();
            for (int i = 0; i < parts.length - 1; i++) {
                attributes.add(Double.parseDouble(parts[i].replace(",", ".")));
            }
            data.add(new IrisExample(attributes, parts[parts.length - 1]));
        }
        reader.close();
        return data;
    }

    private static int perceptron(List<IrisExample> trainingData, List<IrisExample> testData) {
        double learningRate = 0.1;
        List<Double> weights = new ArrayList<>();
        for (int i = 0; i < trainingData.get(0).getAttributes().size(); i++) {
            weights.add(0.0); // Initialize weights to 0
        }
        double threshold = 0.0; // Initialize threshold

        int maxIterations = 10000; // Increased number of iterations
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            for (IrisExample example : trainingData) {
                List<Double> attributes = example.getAttributes();
                double activation = 0.0;
                for (int i = 0; i < attributes.size(); i++) {
                    activation += attributes.get(i) * weights.get(i);
                }
                activation += threshold; // Add threshold to activation

                int target;
                if (example.getDecisionAttribute().equals("Iris-setosa")) {
                    target = 1;
                } else {
                    target = 0;
                }

                int prediction;
                if (activation >= 0) {
                    prediction = 1;
                } else {
                    prediction = 0;
                }

                if (prediction != target) {
                    for (int i = 0; i < weights.size(); i++) {
                        double delta = learningRate * (target - prediction) * attributes.get(i);
                        weights.set(i, weights.get(i) + delta);
                    }
                    threshold += learningRate * (target - prediction); // Update threshold
                }
            }
        }

        // Test perceptron
        int correctlyClassified = 0;
        for (IrisExample example : testData) {
            List<Double> attributes = example.getAttributes();
            double activation = 0.0;
            for (int i = 0; i < attributes.size(); i++) {
                activation += attributes.get(i) * weights.get(i);
            }
            activation += threshold; // Add threshold to activation

            int target;
            if (example.getDecisionAttribute().equals("Iris-setosa")) {
                target = 1;
            } else {
                target = 0;
            }

            int prediction;
            if (activation >= 0) {
                prediction = 1;
            } else {
                prediction = 0;
            }

            if (prediction == target) {
                correctlyClassified++;
            }
        }

        return correctlyClassified;
    }

    public static void main(String[] args) {
        try {
            List<IrisExample> trainingData = readData("iris_training.txt");
            List<IrisExample> testData = readData("iris_test.txt");

            int correctlyClassified = perceptron(trainingData, testData); // Test on test data
            double accuracy = (double) correctlyClassified / testData.size() * 100; // Calculate accuracy based on test data

            System.out.println("Liczba prawidłowo zaklasyfikowanych przykładów: " + correctlyClassified);
            System.out.println("Dokładność eksperymentu: " + accuracy + "%");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Wprowadź wektor atrybutów do klasyfikacji (oddzielone spacjami), lub wprowadź 'exit' aby zakończyć:");
            String input = scanner.nextLine();
            while (!input.equals("exit")) {
                List<Double> attributes = new ArrayList<>();
                String[] inputArray = input.split("\\s+");
                for (String str : inputArray) {
                    if (!str.isEmpty()) {
                        attributes.add(Double.parseDouble(str.replace(",", ".")));
                    }
                }
                if (!attributes.isEmpty()) {
                    int result = perceptron(trainingData, List.of(new IrisExample(attributes, "")));
                    if (result == 1) {
                        System.out.println("Klasyfikacja wektora: Iris-setosa");
                    } else {
                        System.out.println("Klasyfikacja wektora: Iris-nie-setosa");
                    }
                }
                System.out.println("Wprowadź kolejny wektor atrybutów lub wprowadź 'exit' aby zakończyć:");
                input = scanner.nextLine();
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}