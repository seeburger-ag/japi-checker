/*
 * Copyright 2011 William Bernardet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.japi.checker;

/**
 * Defines the possible visibility scope of a JavaItem.
 *
 */
public enum Scope {
    PUBLIC(3), PROTECTED(2), NO_SCOPE(1), PRIVATE(0);
    int scope;
    
    Scope(int scope) {
        this.scope = scope;
    }
    
    public int getValue() {
        return scope;
    }
    
    /**
     * Check if this scope is more visible than the scope.
     * @param scope the scope to compare to
     * @return Returns true if the scope is more visible
     *         than the give scope, false otherwise.
     */
    public boolean isMoreVisibleThan(Scope scope) {
    	return this.getValue() > scope.getValue(); 
    }
}
