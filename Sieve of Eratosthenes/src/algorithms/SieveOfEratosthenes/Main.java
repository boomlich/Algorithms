package algorithms.SieveOfEratosthenes;

import java.util.ArrayList;

public class Main {
    public static int N = 100;
    public static ArrayList<Integer> primeNumbers = new ArrayList<>();

    public static void main(String[] args) {

        // Add all numbers to a list
        for (int i = 2; i < N+1; i++) {
            primeNumbers.add(i);
        }

        int i = 0;
        while (primeNumbers.get(i) < Math.sqrt(N)) {
            int P = primeNumbers.get(i);
            int multipleP = P;

            int j = 2;
            while (multipleP < N-1) {
                multipleP = P * j;
                primeNumbers.remove(Integer.valueOf(multipleP));
                j ++;
            }
            i ++;
        }

        System.out.println("All prime numbers up to " + N +":");
        System.out.println(primeNumbers);
    }
}

