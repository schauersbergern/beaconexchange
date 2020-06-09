package com.protego.namegenerator;

import android.content.Context;

public class RandomNameGenerator {
    private int pos;
    private Context ctx;

    public RandomNameGenerator(int seed) {
        this.pos = seed;
    }

    public RandomNameGenerator(Context ctx) {
        this((int) System.currentTimeMillis());
        this.ctx = ctx;
    }
    public synchronized String next() {
        Dictionary d = new Dictionary(ctx);
        pos = Math.abs(pos+d.getPrime()) % d.size();
        return d.word(pos);
    }
}