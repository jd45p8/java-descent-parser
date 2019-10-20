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
import java.util.HashMap;
import java.util.Set;

/**
 * Represents a grammar structure with its productions.
 *
 * @author José Polo <Github https://github.com/jd45p8>
 */
public class Grammar {

    public String[][] MTable;
    public final HashMap<String, Head> heads;

    /**
     * Grammar builder.
     *
     * @param productions
     */
    public Grammar(ArrayList<String> productions) {
        heads = new HashMap();
        for (String production : productions) {
            String[] prodParts = production.split("->");

            if (!prodParts[0].isEmpty() && !simbolTools.isTerminal(prodParts[0])) {
                if (prodParts.length > 1) {
                    int i = 0;
                    Head head = heads.get(prodParts[0]);

                    if (head == null) {
                        head = new Head(prodParts[0]);
                        heads.put(prodParts[0], head);
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
     * Generate PRIMERO.
     */
    public void generatePRIMERO() {
        for (Head head : this.heads.values()) {
            head.setPRIM(generatePRIMOfAlpha(head.getSimbol()));
        }
    }

    private ArrayList<String> generatePRIMOfAlpha(String alpha) {
        ArrayList<String> PRIM = new ArrayList<>();

        for (Head head : this.heads.values()) {
            if (head.getSimbol().compareTo(alpha) == 0) {

                for (String production : head.getProductions()) {
                    String firstSymbol = production.charAt(0) + "";

                    if (simbolTools.isTerminal(firstSymbol)) {
                        PRIM.add(firstSymbol);
                    } else {

                        if (firstSymbol.compareTo(head.getSimbol()) == 0) {
                            continue;
                        }

                        PRIM.addAll(generatePRIMOfAlpha(firstSymbol));
                    }
                }

                break;
            }
        }

        return PRIM;
    }

    public void getNextOfAll(Head head, HashMap<String, ArrayList<String>> nextOfG) {

    }
}
