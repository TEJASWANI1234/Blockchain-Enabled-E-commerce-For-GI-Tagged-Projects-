import java.io.*;
import java.math.BigInteger;
import java.util.*;
import org.json.*;

public class ShamirSecretSharing {

    // Function to parse JSON and return a Map of the values (x, y)
    public static Map<Integer, BigInteger> parseJsonFile(String filePath) throws IOException, JSONException {
        // Read the JSON file
        StringBuilder jsonData = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            jsonData.append(line);
        }
        reader.close();
        
        // Parse JSON data
        JSONObject jsonObject = new JSONObject(jsonData.toString());
        JSONObject keys = jsonObject.getJSONObject("keys");
        
        int n = keys.getInt("n");
        int k = keys.getInt("k");
        
        Map<Integer, BigInteger> points = new HashMap<>();
        
        for (String key : jsonObject.keySet()) {
            if (!key.equals("keys")) {
                int x = Integer.parseInt(key); // The key acts as the x-value
                
                JSONObject valueObject = jsonObject.getJSONObject(key);
                String baseString = valueObject.getString("base");
                String valueString = valueObject.getString("value");
                
                int base = Integer.parseInt(baseString); // Get the base of the y-value
                BigInteger y = new BigInteger(valueString, base); // Decode the y-value into base 10
                points.put(x, y);
            }
        }
        
        return points;
    }

    // Function to perform Lagrange interpolation to find the constant 'c'
    public static BigInteger lagrangeInterpolation(Map<Integer, BigInteger> points, int k, BigInteger modulus) {
        BigInteger result = BigInteger.ZERO;
        
        // Iterate over each point (x_i, y_i)
        for (Map.Entry<Integer, BigInteger> entry1 : points.entrySet()) {
            int x_i = entry1.getKey();
            BigInteger y_i = entry1.getValue();

            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            // Lagrange basis polynomial construction
            for (Map.Entry<Integer, BigInteger> entry2 : points.entrySet()) {
                int x_j = entry2.getKey();
                if (x_i != x_j) {
                    numerator = numerator.multiply(BigInteger.valueOf(-x_j)).mod(modulus);
                    denominator = denominator.multiply(BigInteger.valueOf(x_i - x_j)).mod(modulus);
                }
            }

            // Calculate the term for this point and add it to the result
            BigInteger term = y_i.multiply(numerator).mod(modulus).multiply(denominator.modInverse(modulus)).mod(modulus);
            result = result.add(term).mod(modulus);
        }
        
        return result;
    }

    public static void main(String[] args) throws IOException, JSONException {
        // Define the file paths for test cases
        String testFilePath1 = "testcase1.json"; // replace with actual path
        String testFilePath2 = "testcase2.json"; // replace with actual path

        // Parse the JSON files to extract points (x, y)
        Map<Integer, BigInteger> points1 = parseJsonFile(testFilePath1);
        Map<Integer, BigInteger> points2 = parseJsonFile(testFilePath2);

        // Use a large prime modulus (for simplicity, you can assume no modulus)
        BigInteger modulus = BigInteger.valueOf(Long.MAX_VALUE);

        // Number of required points (k from the JSON)
        int k1 = 3; // For the first test case, k = 3
        int k2 = 6; // For the second test case, k = 6

        // Find the constant term 'c' using Lagrange interpolation for both test cases
        BigInteger secret1 = lagrangeInterpolation(points1, k1, modulus);
        BigInteger secret2 = lagrangeInterpolation(points2, k2, modulus);

        // Output the results
        System.out.println("Secret for Test Case 1: " + secret1);
        System.out.println("Secret for Test Case 2: " + secret2);
    }
}
