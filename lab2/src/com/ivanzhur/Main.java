package com.ivanzhur;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Main {

    enum Temple {
        GUAN_IN, GUAN_IAN
    }

    private class Fight extends RecursiveTask<Integer> {

        private int round;
        private int middleIndex;

        public Fight(int round, int middleIndex) {
            this.round = round;
            this.middleIndex = middleIndex;
        }

        @Override
        protected Integer compute() {
            int delta = N_MONKS / (int) Math.pow(2, 2 + round);
            // Corresponds first round of fights, i.e. first pairs
            if (delta == 0) {
                if (tsiEnergy[middleIndex] > tsiEnergy[middleIndex + 1]) return middleIndex;
                if (tsiEnergy[middleIndex] < tsiEnergy[middleIndex + 1]) return middleIndex + 1;
                return middleIndex + new Random().nextInt(2);
            }

            // Subsequent fight pairs
            Fight fightA = new Fight(round + 1, middleIndex - delta);
            Fight fightB = new Fight(round + 1, middleIndex + delta);
            fightA.fork();
            fightB.fork();

            int resA = fightA.join();
            int resB = fightB.join();
            if (tsiEnergy[resA] > tsiEnergy[resB]) return resA;
            if (tsiEnergy[resA] < tsiEnergy[resB]) return resB;
            return new Random().nextBoolean() ? resA : resB;
        }
    }

    private static final int N_MONKS = 32;

    private Temple[] monkTemples;
    private Integer[] tsiEnergy;

    public Main() {
        monkTemples = new Temple[N_MONKS];
        tsiEnergy = new Integer[N_MONKS];
        Random random = new Random();
        for (int i = 0; i < N_MONKS; i++) {
            if (random.nextBoolean()) monkTemples[i] = Temple.GUAN_IN;
            else monkTemples[i] = Temple.GUAN_IAN;
            tsiEnergy[i] = random.nextInt(100);
        }

        for (int i = 0; i < N_MONKS; i++) {
            System.out.println("tsi " + i + ": " + tsiEnergy[i]);
        }
    }

    public void fight() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Fight tournament = new Fight(0, N_MONKS / 2 - 1);
        Integer winnerIndex = forkJoinPool.invoke(tournament);

        String winnerTemple = monkTemples[winnerIndex] == Temple.GUAN_IN ? "Guan-In" : "Guan-Ian";
        System.out.println("Winner is the monk #" + winnerIndex + " from " + winnerTemple);
    }

    public static void main(String[] args) {
        Main tournament = new Main();
        tournament.fight();
    }
}
