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

import java.util.HashMap;

/**
 * Mtable hashMap based;
 *
 * @author José Polo <Github https://github.com/jd45p8>
 */
public class MTable extends HashMap<String, HashMap<String, Production>> {

    /**
     * Get the production linked to non terminal A and terminal a.
     *
     * @param A non terminal identifier.
     * @param b terminal identifier.
     * @return producction if found.
     */
    public Production getProduction(String A, String b) {
        HashMap<String, Production> hashA = this.get(A);
        if (hashA != null) {
            Production p = hashA.get(b);
            if (p != null) {
                return p;
            }
        }
        return null;
    }

    /**
     * Get the formated string value linked to production linked to non terminal
     * A and terminal a.
     *
     * @param A non terminal identifier.
     * @param b terminal identifier.
     * @return string formated if production is found.
     */
    public String getProductionString(String A, String b) {
        Production p = getProduction(A, b);
        if (p != null) {
            return A + "->" + getProduction(A, b).alpha;
        }
        return null;
    }

    /**
     * Link the production with terminal A and non terminal p.
     *
     * @param A non terminal identifier.
     * @param b temrinal identifier.
     * @param p production to link.
     */
    public void setProduction(String A, String b, Production p) {
        HashMap<String, Production> hashA = this.get(A);
        if (hashA == null) {
            hashA = new HashMap();
            this.put(A, hashA);
        }
        hashA.put(b, p);
    }

}
