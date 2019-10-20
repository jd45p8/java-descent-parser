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
import java.util.Set;

/**
 * Represents a grammar structure with its productions.
 *
 * @author José Polo <Github https://github.com/jd45p8>
 */
public class Grammar {

    public final ArrayList<Head> heads;
    public String[][] MTable;

    /**
     * Grammar builder.
     *
     * @param productions
     */
    public Grammar(ArrayList<String> productions) {
        heads = new ArrayList();
        for (String production : productions) {
            String[] prodParts = production.split("->");

            if (!prodParts[0].isEmpty() && !simbolTools.isTerminal(prodParts[0])) {
                if (prodParts.length > 1) {
                    int i = 0;
                    Head head = null;

                    while (i < heads.size() && head == null) {
                        if (heads.get(i).getSimbol().compareTo(prodParts[0]) == 0) {
                            head = heads.get(i);
                            break;
                        }
                        i++;
                    }

                    if (head == null) {
                        head = new Head(prodParts[0]);
                        heads.add(head);
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
        for (Head head : this.heads) {
            head.setPRIM(generatePRIMOfAlpha(head.getSimbol()));
        }
    }

    private ArrayList<String> generatePRIMOfAlpha(String alpha) {
        ArrayList<String> PRIM = new ArrayList<>();

        for (Head head : this.heads) {
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

    public void generateSIGUIENTE() {
    }

}
