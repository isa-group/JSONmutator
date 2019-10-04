package es.us.isa.restmutator.mutator;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * Class for randomness management. To be extended by mutators and operators.
 *
 * @author Alberto Martin-Lopez
 */
public abstract class RandomManager {

    protected long seed=-1;
    protected RandomDataGenerator rand1;
    protected RandomGenerator rand2;

    public RandomManager() {
        rand1 = new RandomDataGenerator();
        seed = rand1.getRandomGenerator().nextLong();
        rand1.reSeed(seed);
        rand2 = rand1.getRandomGenerator();
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
        rand1.reSeed(seed);
        rand2 = rand1.getRandomGenerator();
    }
}
