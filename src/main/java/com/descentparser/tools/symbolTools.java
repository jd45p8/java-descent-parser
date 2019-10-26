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
package com.descentparser.tools;

import com.descentparser.grammar.Grammar;

/**
 * A tool set for managing simbols.
 *
 * @author José Polo <Github https://github.com/jd45p8>
 */
public class symbolTools {
    
    /**
     * Determines whether the given simbol is terminal.
     *
     * @param symbol simbol to check out.
     * @return true if given simbol is terminal.
     */
    public static boolean isTerminal(char symbol) {
        return !Character.isUpperCase(symbol);
    }
    
    /**
     * Determines whether the given simbol is terminal.
     *
     * @param symbol simbol to check out.
     * @return true if given simbol is terminal.
     */
    public static boolean isTerminal(String symbol) {
        return symbol.length() == 1 && isTerminal(symbol.charAt(0));
    }

    /**
     * Determines whether the given simbol is in the grammar.
     *
     * @param symbol simbol to check out.
     * @param g grammar where is going to be ckecked the symbol.
     * @return true if given simbol is terminal.
     */
    public static boolean isSymbolOf(String symbol, Grammar g) {
        return g.heads.containsKey(symbol);
    }
    
    /**
     * Looks for an unused in the grammar unicode encoded uppercase symbol.
     * @param g grammar where a free symbol is needed.
     * @return if found returns the free symbol in a string.
     */
    public static String getUnusedUppercase(Grammar g){
        int id = 0;
        int limit = (int) Math.pow(16,5);
        String uL;
        while (id < limit){
            uL = new String(Character.toChars(id));
            if (Character.isUpperCase(id) && !g.heads.containsKey(uL)){
                return uL;
            }
            id++;
        }
        return null;
    }
}
