package algorithms.SieveOfEratosthenes;

public class Main {
    public static void main(String[] args) {
        int N = 100;
        System.out.println(findAllPrimes(N));
    }

    public static String findAllPrimes(int N) {
        String primes = "";
        int[] allNumbers = new int[N-1];
        int[] P = {0, 0};

        // Make array with all number from 2 to N
        int j = 0;
        for (int i = 2; i < N+1; i++) {
            allNumbers[j] = i;
            j++;
        }

        while (P[1] * P[1] < N){
            P = findPrime(allNumbers);
            allNumbers = crossNum(allNumbers, P);
            primes += P[1] + " ";
        }

        for (int x: allNumbers) {
            if (x != 0){
                primes += x + " ";
            }
        }
        return primes;
    }

    // Set the prime and all it's multiples to 0
    static int[] crossNum(int[] inputArray, int[] prime) {
        int arrayLen = inputArray.length;
        for (int i = prime[0]; i < arrayLen; i+= prime[1]) {
            if (inputArray[i] != 0) {
                inputArray[i] = 0;
            }
        }
        return inputArray;
    }

    // Find the prime and it's index in the array
    static int[] findPrime(int[] inputArray) {
        int P = 0;
        int i = 0;
        while (P == 0) {
            P = inputArray[i];
            i ++;
        }
        return new int[]{i-1, P};
    }
}