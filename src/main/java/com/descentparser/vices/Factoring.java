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
import com.descentparser.tools.simbolTools;
import java.util.ArrayList;

/**
 *
 * @author José Polo <Github https://github.com/jd45p8>
 */
public class Factoring {

    public static boolean hasLeftFactorig(Head head) {
        ArrayList<String> productions = head.getProductions();
        productions.sort((prod1, prod2) -> {
            return prod1.compareTo(prod2);
        });

        int i = 1;
        while (i < productions.size()) {
            if (productions.get(i).charAt(0) == productions.get(i - 1).charAt(0)) {
                if (!simbolTools.isTerminal(productions.get(i).substring(0, 1))) {
                    return true;
                }
            }
            i++;
        }
        return false;
    }

    public static void main(String args[]) {
        ArrayList<String> productions = new ArrayList();
        productions.add("E->T");
        productions.add("E->E+T");
        productions.add("E->E-T");        
        productions.add("T->T*F");
        productions.add("T->T/F");
        productions.add("T->F");
        productions.add("F->(E)");
        productions.add("F->id");
        Grammar g = new Grammar(productions);
        g.heads.forEach((head) -> {
            boolean leftFactoring = hasLeftFactorig(head);
            System.out.println(head.toString() + " - leftfactoring: " + leftFactoring);
        });
    }
}
