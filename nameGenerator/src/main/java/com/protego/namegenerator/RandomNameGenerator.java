package com.protego.namegenerator;

public class RandomNameGenerator {
    private int pos;

    public RandomNameGenerator(int seed) {
        this.pos = seed;
    }

    public RandomNameGenerator() {
        this((int) System.currentTimeMillis());
    }
    public synchronized String next() {
        Dictionary d = Dictionary.INSTANCE;
        pos = Math.abs(pos+d.getPrime()) % d.size();
        return d.word(pos);
    }
}