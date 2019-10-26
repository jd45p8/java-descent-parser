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

import com.descentparser.tools.NullableStatus;
import com.descentparser.tools.symbolTools;
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
    public Production[][] MTable;
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

        for (String alpha : productions) {
            String[] prodParts = alpha.split("->");

            if (prodParts[0].length() == 1 && symbolTools.isTerminal(prodParts[0])) {
                if (prodParts.length > 1) {
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
            h.getProductions().stream().forEachOrdered( (Production p) -> {
                String[] chars = p.alpha.split("");
                for (String c : chars) {
                    if (c.compareTo("&") != 0 && symbolTools.isTerminal(c) && !terminalSymbols.contains(c)) {
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
            Set<String> first = new HashSet<>();

            h.getProductions().stream()
                    .forEachOrdered((Production p) -> {
                        String firstSymbol = p.alpha.charAt(0) + "";
                        first.add(firstSymbol);
                    });

            PRIM.put(h.getSimbol(), first);
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

    /**
     * Generate the MTable.
     */
    private void generateMTable() {
        this.MTable = new Production[this.nonTerminalSymbols.size()][this.terminalSymbols.size() + 1];

        /**
         * Loop over all non terminal symbols.
         */
        this.nonTerminalSymbols.stream().forEachOrdered((String key) -> {

            /**
             * Loop over the productions.
             */
            this.heads.get(key).getProductions().stream().forEachOrdered((Production prod) -> {

                if (prod.compareTo("&") == 0) {
                    // TODO   Usar SIGUIENTE
                } else {
                    String firstSymbol = prod.alpha.charAt(0) + "";
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

    /**
     * Looks for and set the next of every non terminal of the grammar.
     */
    public void generateNext() {
        heads.values().forEach(head -> {
            head.getProductions().forEach((production) -> {
                heads.keySet().forEach((symbol) -> {
                    if (production.alpha.contains(symbol)) {
                        String[] prodSplit = production.alpha.split(symbol, 0);
                        /**
                         * If last part of vector is nullable means next of
                         * simbol contains next of head symbol.
                         */
                        if (prodSplit.length == 1 || nullable(prodSplit[prodSplit.length - 1])) {
                            ArrayList<String> nxtOfSimbol = heads.get(symbol).getNext();
                            if (!nxtOfSimbol.contains(head.getSimbol())) {
                                nxtOfSimbol.add(head.getSimbol());
                            }
                        }

                        /**
                         * The first terminal rigth of symbol different to
                         * epsilon is in next of symbol.
                         */
                        ArrayList<String> nxt = head.getNext();
                        for (int i = 1; i < prodSplit.length - 1; i++) {
                            String betha = prodSplit[i];
                            ArrayList<String> prinOfBetha = PRIMOfWord(betha);
                            prinOfBetha.forEach(item -> {
                                if (item.compareTo("&") != 0) {
                                    nxt.add(item);
                                }
                            });
                        }
                    }
                });
            });
        });

        boolean first = true;
        heads.values().forEach(head -> {
            ArrayList<String> nxt = head.getNext();
            if (first) {
                nxt.add("$");
            }
            boolean repeat;
            do {
                repeat = false;
                for (String item: nxt) {
                    if (!symbolTools.isTerminal(item)) {
                        nxt.addAll(heads.get(item).getNext());
                        repeat = true;
                    }
                }
            } while (repeat);
        });
    }

    /**
     * Denermines whether alpha generates epsilon.
     *
     * @param alpha production to check nullability.
     * @return true if alpha is nullable.
     * @throws NullPointerException if simbol doen't exist in heads.
     */
    public boolean nullable(String alpha) throws NullPointerException {
        int i = 0;
        while (i < alpha.length()) {
            if (!symbolTools.isTerminal(alpha.charAt(i))) {
                Head head = heads.get(alpha.substring(i, i + 1));
                if (head != null) {
                    if (!nullable(head)) {
                        return false;
                    }
                } else {
                    throw new NullPointerException("Simbol " + alpha.charAt(i) + " not found.");
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Denermines whether the simbol generates epsilon.
     *
     * @param head production to check nullability.
     * @return true if production is nullable.
     * @throws NullPointerException if simbol doen't exist in heads.
     */
    public boolean nullable(Head head) throws NullPointerException {
        ArrayList<Production> productions = head.getProductions();
        int i = 0;
        boolean isSomeOneCalculating = false;
        while (i < productions.size()) {
            Production p = productions.get(i);
            switch (p.nullableStatus) {
                case NotCalculated:
                    p.nullableStatus = NullableStatus.Calculating;
                    int j = 0;
                    while (j < p.alpha.length()) {
                        if (!symbolTools.isTerminal(p.alpha.charAt(i))) {
                            Head nextHead = heads.get(p.alpha.substring(i, i + 1));
                            if (nextHead != null) {
                                if (nullable(nextHead)) {
                                    p.nullableStatus = NullableStatus.Nullable;
                                } else {
                                    p.nullableStatus = NullableStatus.NotNullable;
                                    return false;
                                }
                            } else {
                                throw new NullPointerException("Simbol " + p.alpha.charAt(i) + " not found.");
                            }
                        } else if (p.alpha.charAt(i) != '&') {
                            p.nullableStatus = NullableStatus.NotNullable;
                            return false;
                        }
                    }
                    break;
                case Calculating:
                    isSomeOneCalculating = true;
                    break;
                default:
                    return p.nullableStatus == NullableStatus.Nullable;
            }
            i++;
        }

        return !isSomeOneCalculating;
    }
    
    
    
}
