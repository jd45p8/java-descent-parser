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

import com.descentparser.grammar.Head;
import com.descentparser.grammar.Production;
import com.descentparser.tools.symbolTools;
import java.util.ArrayList;

/**
 * Contains tools for detectiong and solving productions(in Head) left
 * factoring.
 *
 * @author José Polo <Github https://github.com/jd45p8>
 */
public class Factoring {

    /**
     * Determines whether productions in head have left side factoring.
     *
     * @param head Head whose productions are going to be analyzed looking for
     * left side factoring.
     * @return true if head productions have left side factoring.
     */
    public static boolean hasLeftFactoring(Head head) {
        ArrayList<Production> productions = head.getProductions();
        productions.sort((prod1, prod2) -> {
            return prod1.compareTo(prod2);
        });

        String simbol = head.getSymbol();
        int i = 1;
        while (i < productions.size()) {
            if (productions.get(i).alpha.charAt(0) == productions.get(i - 1).alpha.charAt(0)) {
                if (productions.get(i).alpha.substring(0, simbol.length()).compareTo(simbol) != 0) {
                    return true;
                }
            }
            i++;
        }
        return false;
    }

    /**
     * Removes left side factoring vice from head productions.
     *
     * @param head Head whose productions have left side factoring vice.
     * @param nonTerminals non terinals already used in grammar to whick head
     * belongs.
     * @return Heads list as result of removing head left side factoring.
     */
    public static ArrayList<Head> removeLeftSideFactoring(Head head, ArrayList<String> nonTerminals) {
        ArrayList<Production> productions = head.getProductions();
        productions.sort((prod1, prod2) -> {
            return prod1.compareTo(prod2);
        });
        int matchStrIndex = 1;
        int firstMatchProd = 0;
        int matchProdCount = 1;
        int i = 1;
        while (i < productions.size()) {
            if (matchStrIndex <= productions.get(i).length()
                    && matchStrIndex <= productions.get(i - 1).length()) {
                String subS1 = productions.get(i - 1).alpha.substring(0, matchStrIndex);
                String subS2 = productions.get(i).alpha.substring(0, matchStrIndex);

                /**
                 * If the first simbol of both subtrings is the same but is the
                 * same head simbol it isn't left factoring vice.
                 */
                if (subS1.compareTo(subS2) == 0 && subS1.substring(0, 1).compareTo(head.getSymbol()) != 0) {
                    if (matchStrIndex == 1) {
                        matchProdCount++;
                    }
                    if (i == productions.size() - 1) {
                        matchStrIndex++;
                        i = firstMatchProd;
                    }
                } else if (matchProdCount == 1) {
                    firstMatchProd++;
                } else if (matchStrIndex == 1) {
                    matchStrIndex++;
                    i = firstMatchProd;
                } else {
                    matchStrIndex--;
                    break;
                }
            } else if (matchProdCount == 1) {
                firstMatchProd++;
            } else {
                matchStrIndex--;
                break;
            }
            i++;
        }

        ArrayList<Head> result = new ArrayList();

        if (matchProdCount > 1) {
            Head A = new Head(head.getSymbol());
            Head Asec = new Head(symbolTools.getUnusedUppercase(nonTerminals));
            nonTerminals.add(nonTerminals.indexOf(head.getSymbol())+1,Asec.getSymbol());

            i = 0;
            //Alpha is the common substring.
            A.addProduction(productions.get(firstMatchProd).alpha.substring(0, matchStrIndex) + Asec.getSymbol());
            while (i < productions.size()) {
                //Is gamma if doesn't match with the commmon string.
                if (i < firstMatchProd || i >= firstMatchProd + matchProdCount) {
                    A.addProduction(productions.get(i).alpha);
                } else {
                    String subs = productions.get(i).alpha.substring(matchStrIndex);
                    Asec.addProduction(subs.isEmpty() ? "&" : subs);
                }
                i++;
            }

            result.add(A);
            result.add(Asec);
        }else{
            result.add(head);
        }
        return result;
    }
}
