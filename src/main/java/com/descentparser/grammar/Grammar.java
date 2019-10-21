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
        PRIM = new HashMap<>();

        for (Head h : this.heads.values()) {
            Set<String> p = new HashSet<>();

            h.getProductions()
                    .stream()
                    .map((production) -> production.charAt(0) + "")
                    .forEachOrdered((firstSymbol) -> {
                        p.add(firstSymbol);
                    });

            PRIM.put(h.getSimbol(), p);
        }

        HashMap<String, Boolean> marked = new HashMap<>();
                
        for (String a : PRIM.keySet()) {
            marked.put(a, Boolean.TRUE);
            
            for (Set<String> bSet : PRIM.values()) {
                if (bSet.contains(a)) {
                    bSet.remove(a);
                    
                    
                    for (String temp : PRIM.get(a)) {
                        if (!marked.containsKey(temp)) {
                            bSet.add(temp);
                        }
                    }
                    //bSet.addAll(PRIM.get(a));
                }
            }
        }
    }

    public void getNextOfAll(Head head, HashMap<String, ArrayList<String>> nextOfG) {

    }
}
