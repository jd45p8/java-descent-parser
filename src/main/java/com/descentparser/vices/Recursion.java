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
package com.descentparser.vices;

import com.descentparser.grammar.Grammar;
import com.descentparser.grammar.Head;
import com.descentparser.grammar.Production;
import com.descentparser.tools.symbolTools;
import java.util.ArrayList;

/**
 * Contains tools for detecting and solving productions(in Head) left recursion.
 *
 * @author José Polo <Github https://github.com/jd45p8>
 */
public class Recursion {

    /**
     * Determines whether productions in head have left side recursion.
     *
     * @param head Head to be analyzed looking for left side recursion in its
     * productions.
     * @return true if head productions has left side recursion.
     */
    public static boolean hasLeftRecursion(Head head) {
        ArrayList<Production> productions = head.getProductions();
        int i = 0;
        String simbol = head.getSymbol();
        while (i < productions.size()) {
            String alpha = productions.get(i).alpha;
            if (alpha.substring(0, simbol.length()).compareTo(simbol) == 0) {
                return true;
            }
            i++;
        }
        return false;
    }

    /**
     * Removes left side recursion from head productions in a new array list.
     *
     * @param head Head whose productions has left side recursion.
     * @param nonTerminals non terminals set of the grammar to which the head
     * belongs.
     * @return Heads list as result of removing head left side recursion.
     */
    public static ArrayList<Head> removeLeftSideRecursion(Head head, ArrayList<String> nonTerminals) {
        String simbol = head.getSymbol();
        Head A = new Head(simbol);
        Head Asec = new Head(symbolTools.getUnusedUppercase(nonTerminals));

        head.getProductions().forEach((production) -> {
            if (production.alpha.substring(0, simbol.length()).compareTo(simbol) == 0) {
                Asec.addProduction(production.alpha.substring(simbol.length()) + Asec.getSymbol());
            } else {
                A.addProduction(production.alpha + Asec.getSymbol());
            }
        });

        ArrayList<Head> result = new ArrayList();
        if (Asec.getProductions().size() > 0) {
            Asec.addProduction("&");
            nonTerminals.add(nonTerminals.indexOf(head.getSymbol()) + 1, Asec.getSymbol());

            result.add(A);
            result.add(Asec);
        } else {
            result.add(head);
        }
        return result;
    }
}
