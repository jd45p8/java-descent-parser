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

import java.util.ArrayList;

/**
 * Represents the head of a production and its productions.
 *
 * @author José Polo <Github https://github.com/jd45p8>
 */
public class Head {

    private final String simbol;
    private final ArrayList<Production> productions;
    private ArrayList<String> PRIM;
    private final ArrayList<String> next;

    /**
     * Head builder.
     *
     * @param symbol
     */
    public Head(String symbol) {
        this.simbol = symbol;
        this.productions = new ArrayList();
        this.next = new ArrayList();
    }

    /**
     * Get head symbol.
     *
     * @return Head symbol.
     */
    public String getSymbol() {
        return simbol;
    }

    /**
     * Get productions linked to head simbol.
     *
     * @return Productions list without Head.
     */
    public ArrayList<Production> getProductions() {
        return productions;
    }

    public ArrayList<String> getPRIM() {
        return PRIM;
    }

    public void setPRIM(ArrayList<String> PRIM) {
        this.PRIM = PRIM;
    }

    public ArrayList<String> getNext() {
        return next;
    } 

    /**
     * Format a production and adds it to productions list.
     *
     * @param alpha production to add.
     */
    public void addProduction(String alpha) {
        if (alpha.contains("->")) {
            String[] prodParts = alpha.split("->");
            if (prodParts[0].compareTo(prodParts[0]) == 0 && !prodParts[1].isEmpty()) {
                boolean founded = false;
                int i = 0;
                while (i < productions.size()) {
                    if (productions.get(i).compareTo(prodParts[1]) == 0) {
                        founded = true;
                    }
                    i++;
                }
                if (!founded) {
                    productions.add(new Production(prodParts[1]));
                }
            }
        } else {
            boolean founded = false;
            int i = 0;
            while (i < productions.size()) {
                if (productions.get(i).compareTo(alpha) == 0) {
                    founded = true;
                }
                i++;
            }
            if (!founded) {
                productions.add(new Production(alpha));
            }
        }
    }

    /**
     * Insert a production in productions list, you must ensure it is a valid
     * production, without "->".
     *
     * @param production Production to add.
     */
    public void addProduction(Production production) {
        productions.add(production);
    }

    @Override
    public String toString() {
        String result = simbol + "->";
        int i = 0;
        while (i < productions.size()) {
            if (i > 0) {
                result += simbol + "->" + productions.get(i).alpha + "\n";
            } else {
                result += productions.get(i).alpha + "\n";
            }

            i++;
        }
        return result;
    }

}
