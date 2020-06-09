package com.protego.namegenerator;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    private List<String> nouns = new ArrayList<>();
    private List<String> adjectives = new ArrayList<>();

    private final int prime;

    public Dictionary(Context ctx) {
        try {
            load(ctx, "a.txt", adjectives);
            load(ctx,"n.txt", nouns);
        } catch (IOException e) {
            throw new Error(e);
        }

        int combo = size();

        int primeCombo = 2;
        while (primeCombo<=combo) {
            int nextPrime = primeCombo+1;
            primeCombo *= nextPrime;
        }
        prime = primeCombo+1;
    }

    /**
     * Total size of the combined words.
     */
    public int size() {
        return nouns.size()*adjectives.size();
    }

    /**
     * Sufficiently big prime that's bigger than {@link #size()}
     */
    public int getPrime() {
        return prime;
    }

    public String word(int i) {
        int a = i%adjectives.size();
        int n = i/adjectives.size();

        return adjectives.get(a)+" "+nouns.get(n);
    }

    private void load(Context ctx, String name, List<String> col) throws IOException {
       InputStream in = ctx.getAssets().open(name);
        BufferedReader r = new BufferedReader(new InputStreamReader(in,"US-ASCII"));
        try {
            String line;
            while ((line=r.readLine())!=null)
                col.add(line);
        } finally {
            r.close();
        }
    }

}