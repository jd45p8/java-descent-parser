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
import com.descentparser.vices.Factoring;
import com.descentparser.vices.Recursion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Represents a grammar structure with its productions.
 *
 * @author José Polo <Github https://github.com/jd45p8>
 */
public class Grammar {

    public Production[][] MTable;
    public final HashMap<String, Head> heads;
    public final ArrayList<String> nonTerminals;
    public final ArrayList<String> terminalSymbols;

    /**
     * Grammar builder.
     *
     * @param productions
     */
    public Grammar(ArrayList<String> productions) {
        heads = new HashMap();
        nonTerminals = new ArrayList();
        terminalSymbols = new ArrayList();

        for (String alpha : productions) {
            String[] prodParts = alpha.split("->");

            if (prodParts[0].length() == 1 && !symbolTools.isTerminal(prodParts[0])) {
                if (prodParts.length > 1) {
                    Head head = heads.get(prodParts[0]);

                    if (head == null) {
                        head = new Head(prodParts[0]);
                        heads.put(prodParts[0], head);
                        nonTerminals.add(prodParts[0]);
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
    }

    /**
     * Execute all processes needed to let the grammar ready to recongnize
     * strings.
     */
    public void processGrammar() {
        ArrayList<Head> vicesFreeHeads = new ArrayList();

        heads.keySet().forEach((key) -> {
            Head head = heads.get(key);
            ArrayList<Head> temp;
            if (Factoring.hasLeftFactoring(head)) {
                temp = Factoring.removeLeftSideFactoring(head, nonTerminals);
                vicesFreeHeads.addAll(temp);
            } else if (Recursion.hasLeftRecursion(head)) {
                temp = Recursion.removeLeftSideRecursion(head, nonTerminals);
                vicesFreeHeads.addAll(temp);
            } else {
                vicesFreeHeads.add(head);
            }
        });

        heads.clear();
        vicesFreeHeads.forEach((head) -> {
            heads.put(head.getSymbol(), head);
        });

        generatePRIMERO();
        generateNext();
    }

    /**
     * Generate PRIMERO.
     */
    private void generatePRIMERO() {
        /**
         * Loop over all heads and get the first symbol of every productions.
         */
        heads.values().forEach((Head h) -> {
            ArrayList<String> first = h.getFirst();

            h.getProductions().stream()
                    .forEachOrdered((Production p) -> {
                        int i = 0;
                        String symbol;
                        while (i < p.length()) {
                            symbol = p.alpha.substring(i, i + 1);
                            if (symbol.compareTo("&") != 0) {
                                first.add(symbol);
                                break;
                            }
                            i++;
                        }
                        if (i == p.length()) {
                            first.add("&");
                        }
                    });
        });

        heads.values().forEach(head -> {
            ArrayList<String> first = head.getFirst();
            int i = 0;
            while (i < first.size()) {
                String item = first.get(i);
                if (!symbolTools.isTerminal(item)) {
                    ArrayList<String> firstOfItem = heads.get(item).getFirst();

                    firstOfItem.forEach(X -> {
                        if (first.contains(X) == false) {
                            first.add(X);
                        }
                    });

                    first.remove(i);
                } else {
                    i++;
                }
            }
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
            return heads.get(firstSymbol).getFirst();
        }

        return new ArrayList<>(Arrays.asList(firstSymbol));
    }

    /**
     * Generate the MTable.
     */
    private void generateMTable() {
        this.MTable = new Production[this.nonTerminals.size()][this.terminalSymbols.size() + 1];

        /**
         * Loop over all non terminal symbols.
         */
        this.nonTerminals.stream().forEachOrdered((String key) -> {

            /**
             * Loop over the productions.
             */
            this.heads.get(key).getProductions().stream().forEachOrdered((Production prod) -> {

                if (prod.compareTo("&") == 0) {
                    // TODO   Usar SIGUIENTE
                } else {
                    String firstSymbol = prod.alpha.substring(0, 1);
                    if (this.PRIM.containsKey(firstSymbol)) {

                        this.PRIM.get(firstSymbol).stream().forEachOrdered((String p) -> {
                            if (MTable[this.nonTerminals.indexOf(key)][this.terminalSymbols.indexOf(p)] == null) {
                                MTable[this.nonTerminals.indexOf(key)][this.terminalSymbols.indexOf(p)] = prod;
                            }
                        });
                    } else {
                        MTable[this.nonTerminals.indexOf(key)][this.terminalSymbols.indexOf(firstSymbol)] = prod;
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
                heads.keySet().forEach(symbol -> {
                    if (production.alpha.contains(symbol)) {
                        String[] prodSplit = production.alpha.split(symbol, 0);
                        /**
                         * If last part of vector is nullable means next of
                         * simbol contains next of head symbol.
                         */
                        ArrayList<String> nxtOfSimbol = heads.get(symbol).getNext();
                        if (prodSplit.length <= 1 || nullable(prodSplit[prodSplit.length - 1])) {
                            if (!nxtOfSimbol.contains(head.getSymbol()) && symbol.compareTo(head.getSymbol()) != 0) {
                                nxtOfSimbol.add(head.getSymbol());
                            }
                        }

                        /**
                         * The first terminal rigth of symbol different to
                         * epsilon is in next of symbol.
                         */
                        if (prodSplit.length > 1) {
                            for (int i = 1; i < prodSplit.length; i++) {
                                String betha = prodSplit[i];
                                ArrayList<String> prinOfBetha = PRIMOfWord(betha);
                                prinOfBetha.forEach(item -> {
                                    if (item.compareTo("&") != 0 && !nxtOfSimbol.contains(item)) {
                                        nxtOfSimbol.add(item);
                                    }
                                });
                            }
                        } else if (prodSplit.length == 1 && symbol.compareTo(production.alpha.substring(0, 1)) == 0) {
                            ArrayList<String> prinOfBetha = PRIMOfWord(prodSplit[0]);
                            prinOfBetha.forEach(item -> {
                                if (item.compareTo("&") != 0 && !nxtOfSimbol.contains(item)) {
                                    nxtOfSimbol.add(item);
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
            int i = 0;
            while (i < nxt.size()) {
                String item = nxt.get(i);
                if (symbolTools.isSymbolOf(item, this)) {
                    ArrayList<String> nxtOfItem = heads.get(item).getNext();
                    int cont = 0;
                    for (String X : nxtOfItem) {
                        if (nxt.contains(X) == false) {
                            cont++;
                            nxt.add(i + cont, X);                            
                        }
                    }

                    nxt.remove(i);
                } else {
                    i++;
                }
            }
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
                        j++;
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
