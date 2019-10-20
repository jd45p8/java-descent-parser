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
        ArrayList<String> productions = head.getProductions();
        int i = 0;
        String simbol = head.getSimbol();
        while (i < productions.size()) {
            String production = productions.get(i);
            if (production.substring(0, simbol.length()).compareTo(simbol) == 0) {
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
     * @return Heads list as result of removing head left side recursion.
     */
    public static ArrayList<Head> removeLeftSideRecursion(Head head) {
        String simbol = head.getSimbol();
        Head A = new Head(simbol);
        Head Asec = new Head(simbol + "'");

        head.getProductions().forEach((production) -> {
            if (production.substring(0, simbol.length()).compareTo(simbol) == 0) {
                Asec.addProduction(production.substring(simbol.length()) + Asec.getSimbol());
            } else {
                A.addProduction(production + Asec.getSimbol());
            }
        });
        Asec.addProduction("&");

        ArrayList<Head> result = new ArrayList();
        result.add(A);
        result.add(Asec);

        return result;
    }

    public static void main(String args[]) {
        ArrayList<String> productions = new ArrayList();
        productions.add("E->E+T");
        productions.add("E->E-T");
        productions.add("E->T");
        productions.add("T->T*F");
        productions.add("T->T/F");
        productions.add("T->F");
        productions.add("F->(E)");
        productions.add("F->id");
        Grammar g = new Grammar(productions);
        g.heads.values().forEach((head) -> {
            if (hasLeftRecursion(head)) {
                removeLeftSideRecursion(head).forEach(tempHead -> {
                    System.out.println(tempHead.toString());
                });
            } else {
                System.out.println(head.toString());
            }
        });
    }
}
