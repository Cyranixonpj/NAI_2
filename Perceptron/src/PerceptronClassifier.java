import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
                // Replace commas with dots for numerical consistency
                attributes.add(Double.parseDouble(parts[i].replace(",", ".")));
            }
            data.add(new IrisExample(attributes, parts[parts.length - 1]));
        }
        reader.close();
        return data;
    }

    private static int perceptron(List<IrisExample> trainingData, List<IrisExample> testData) {
        double threshold = 0.0;
        double learningRate = 0.1;
        List<Double> weights = new ArrayList<>();
        for (int i = 0; i < trainingData.get(0).getAttributes().size(); i++) {
            weights.add(0.0); // Initialize weights to 0
        }

        // Train perceptron
        for (IrisExample example : trainingData) {
            List<Double> attributes = example.getAttributes();
            double activation = 0.0;
            for (int i = 0; i < attributes.size(); i++) {
                activation += attributes.get(i) * weights.get(i);
            }
            int target;
            if (example.getDecisionAttribute().equals("Iris-setosa")) {
                target = 1;
            } else {
                target = -1;
            }

            double error = target - activation;
            if (error != 0) {
                for (int i = 0; i < weights.size(); i++) {
                    double delta = learningRate * error * attributes.get(i);
                    weights.set(i, weights.get(i) + delta);
                }
                threshold += learningRate * error; // Update threshold
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
            activation += threshold;

            int predictedClass;
            if (activation >= 0) {
                predictedClass = 1;
            } else {
                predictedClass = -1;
            }

            int target;
            if (example.getDecisionAttribute().equals("Iris-setosa")) {
                target = 1;
            } else {
                target = -1;
            }

            if (predictedClass == target) {
                correctlyClassified++;
            }
        }

        return correctlyClassified;
    }

    public static void main(String[] args) {
        try {
            List<IrisExample> trainingData = readData("iris_training.txt");
            List<IrisExample> testData = readData("iris_test.txt");

            int correctlyClassified = perceptron(trainingData, testData);
            double accuracy = (double) correctlyClassified / testData.size() * 100;

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