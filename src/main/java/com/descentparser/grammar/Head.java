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
    private final ArrayList<String> productions;

    /**
     * Head builder.
     *
     * @param simbol
     */
    public Head(String simbol) {
        this.simbol = simbol;
        this.productions = new ArrayList<>();
    }

    /**
     * Get head simbol.
     *
     * @return Head simbol.
     */
    public String getSimbol() {
        return simbol;
    }

    /**
     * Get productions linked to head simbol.
     *
     * @return Productions list without Head.
     */
    public ArrayList<String> getProductions() {
        return productions;
    }

    /**
     * Format a production and adds it to productions array.
     *
     * @param production Productions array.
     */
    public void addProduction(String production) {
        if (production.contains("->")) {
            String[] prodParts = production.split("->");
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
                    productions.add(prodParts[1]);
                }
            }
        } else if (!production.isEmpty()) {
            boolean founded = false;
            int i = 0;
            while (i < productions.size()) {
                if (productions.get(i).compareTo(production) == 0) {
                    founded = true;
                }
                i++;
            }
            if (!founded) {
                productions.add(production);
            }
        }
    }

    @Override
    public String toString() {
        String result = simbol + "->";
        int i = 0;
        while (i < productions.size()) {
            if (i > 0) {
                result += "|" + productions.get(i);
            } else {
                result += productions.get(i);
            }

            i++;
        }
        return result;
    }

}
