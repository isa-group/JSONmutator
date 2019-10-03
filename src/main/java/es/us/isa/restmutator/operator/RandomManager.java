package es.us.isa.restmutator.operator;

import java.util.Random;

/**
 * Class for randomness management. To be extended by mutators and operators.
 *
 * @author Alberto Martin-Lopez
 */
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
