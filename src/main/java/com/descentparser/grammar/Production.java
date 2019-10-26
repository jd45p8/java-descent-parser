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

import com.descentparser.tools.NullableStatus;

/**
 *
 * @author José Polo <Github https://github.com/jd45p8>
 */
public class Production {
    public NullableStatus nullableStatus;
    public final String alpha;

    public Production(String alpha) {        
        this.alpha = alpha;
        this.nullableStatus = NullableStatus.NotCalculated;
    }
    
    /**
     * Compares alpha and the provided String p and determines whether both produces the same content.
     * @param alpha String to compare with alpha.
     * @return How much both strings are different.
     */
    public int compareTo(String alpha){
        return this.alpha.compareTo(alpha);
    }
    
    /**
     * Compares alpha and the provided String p and determines whether both produces the same content.
     * @param production Production to compare with alpha.
     * @return How much both strings are different.
     */
    public int compareTo(Production production){
        return alpha.compareTo(production.alpha);
    }
    
    /**
     * Returns alpha length.
     * @return alpha length.
     */
    public int length() {
        return alpha.length();
    }
}
