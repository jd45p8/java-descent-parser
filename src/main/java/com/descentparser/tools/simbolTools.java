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

/**
 * A tool set for managing simbols.
 * @author José Polo <Github https://github.com/jd45p8>
 */
public class simbolTools {
    
    /**
     * Determines whether the given simbol is terminal.
     * @param simbol simbol to check out.
     * @return true if given simbol is terminal.
     */
    public static boolean isTerminal(String simbol){
        if (simbol.toLowerCase().compareTo(simbol) == 0){
            return !(simbol.compareTo("->") == 0);
        }
        return false;
    }
}
