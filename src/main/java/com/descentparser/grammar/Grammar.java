/*
 * Copyright 2019 José Polo <Github https://github.com/jd45p8>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.descentparser.grammar;

import com.descentparser.tools.simbolTools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a grammar structure with its productions.
 *
 * @author José Polo <Github https://github.com/jd45p8>
 */
public class Grammar {

    public String[][] MTable;
    public final HashMap<String, Head> heads;
    public HashMap<String, Set<String>> PRIM;

    public final ArrayList<String> nonTerminalSymbols;
    public final ArrayList<String> terminalSymbols;

    /**
     * Grammar builder.
     *
     * @param productions
     */
    public Grammar(ArrayList<String> productions) {
        heads = new HashMap();

        nonTerminalSymbols = new ArrayList<>();
        terminalSymbols = new ArrayList<>();

        for (String production : productions) {
            String[] prodParts = production.split("->");

            if (!prodParts[0].isEmpty() && !simbolTools.isTerminal(prodParts[0])) {
                if (prodParts.length > 1) {
                    int i = 0;
                    Head head = heads.get(prodParts[0]);

                    if (head == null) {
                        head = new Head(prodParts[0]);
                        heads.put(prodParts[0], head);
                        nonTerminalSymbols.add(prodParts[0]);
                    }

                    head.addProduction(prodParts[1]);

                } else {
                    // Removes all elements from head list if a production is misshapen.
                    heads.clear();
                    break;
                }
            } else {
                // Removes all elements from head list if a production head simbol is lowercase.
                heads.clear();
                break;
            }
        }

        /**
         * Get all non terminal symbols.
         */
        this.nonTerminalSymbols.stream().forEachOrdered((String k) -> {
            Head h = this.heads.get(k);
            h.getProductions().stream().forEachOrdered((String p) -> {
                String[] chars = p.split("");
                for (String c : chars) {
                    if (c.compareTo("&") != 0 && simbolTools.isTerminal(c) && !terminalSymbols.contains(c)) {
                        terminalSymbols.add(c);
                    }
                }

            });
        });

        this.generatePRIMERO();
        this.generateMTable();
    }

    /**
     * Generate PRIMERO.
     */
    private void generatePRIMERO() {
        PRIM = new HashMap<>();

        /**
         * Loop over all heads and get the first symbol of every productions.
         */
        heads.values().forEach((Head h) -> {
            Set<String> p = new HashSet<>();

            h.getProductions().stream()
                    .forEachOrdered((production) -> {
                        String firstSymbol = production.charAt(0) + "";
                        p.add(firstSymbol);
                    });

            PRIM.put(h.getSimbol(), p);
        });

        HashMap<String, Boolean> marked = new HashMap<>();

        /**
         * Replace the non-terminal symbols in the TRIM hashmap.
         */
        PRIM.keySet().stream().forEachOrdered((String a) -> {
            marked.put(a, Boolean.TRUE);
            PRIM.values().stream()
                    .filter((bSet) -> (bSet.contains(a)))
                    .forEachOrdered((bSet) -> {
                        bSet.remove(a);
                        PRIM.get(a).stream()
                                .filter((String str) -> !marked.containsKey(str))
                                .forEach((String str) -> bSet.add(str));
                    });
        });
    }

    /**
     * Find the PRIMERO of a word.
     *
     * @param w
     * @return
     */
    public ArrayList<String> PRIMOfWord(String w) {
        String firstSymbol = w.charAt(0) + "";

        if (this.heads.containsKey(firstSymbol)) {
            return new ArrayList<>(this.PRIM.get(firstSymbol));
        }

        return new ArrayList<>(Arrays.asList(firstSymbol));
    }

    public void getNextOfAll(Head head, HashMap<String, ArrayList<String>> nextOfG) {

    }

    /**
     * Generate the MTable.
     */
    private void generateMTable() {
        this.MTable = new String[this.nonTerminalSymbols.size()][this.terminalSymbols.size() + 1];

        /**
         * Loop over all non terminal symbols.
         */
        this.nonTerminalSymbols.stream().forEachOrdered((String key) -> {

            /**
             * Loop over the productions.
             */
            this.heads.get(key).getProductions().stream().forEachOrdered((String prod) -> {

                if (prod.compareTo("&") == 0) {
                    // TODO   Usar SIGUIENTE
                } else {
                    String firstSymbol = prod.charAt(0) + "";
                    if (this.PRIM.containsKey(firstSymbol)) {

                        this.PRIM.get(firstSymbol).stream().forEachOrdered((String p) -> {
                            if (MTable[this.nonTerminalSymbols.indexOf(key)][this.terminalSymbols.indexOf(p)] == null) {
                                MTable[this.nonTerminalSymbols.indexOf(key)][this.terminalSymbols.indexOf(p)] = prod;
                            }
                        });
                    } else {
                        MTable[this.nonTerminalSymbols.indexOf(key)][this.terminalSymbols.indexOf(firstSymbol)] = prod;
                    }
                }

            });

        });
    }
}
