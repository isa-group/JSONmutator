package es.us.isa.restmutator.operator;

import java.util.Random;

public abstract class RandomManager {

    protected long seed=-1;
    protected Random rand;

    public RandomManager() {
        rand = new Random();
        seed = rand.nextLong();
        rand.setSeed(seed);
    }

    public long getSeed() {
        return this.seed;
    }
}
