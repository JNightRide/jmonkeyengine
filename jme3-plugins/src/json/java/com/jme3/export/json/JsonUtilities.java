/*
 * Copyright (c) 2009-2024 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.export.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jme3.export.Savable;

/**
 *
 * @author wil
 */
final class JsonUtilities {
    
    public static final String JME_JSON_PREFIX    = "jme3.";
    public static final String JME_JSON_META_DATA = "metadata";
    public static final String JME_JSON_BINARIES  = "root";
    
    public static final String JME_REFERENCE = "+/jMe3.Reference";
    public static final String JME_VERSIONS  = "+/jMe3.Versions";
    public static final String JME_CLASS     = "+/jMe3.Class";
    public static final String JME_ID        = "+/jMe3.Id";
    
    public static final String JME_SIGNATURE      = "+/jMe3.Signature";
    public static final String JME_FORMAT_VERSION = "+/jMe3.Version";
        
    public static final String JME_MAP_KEY    = "+/jMe3.Map.Key";
    public static final String JME_MAP_VALUE  = "+/jMe3.Map.Value";
    
    public static boolean checkName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        return true;
    }
    
    public static boolean checkSavable(Object object) {
        if ((object != null) && (object instanceof Savable)) {
            return true;
        }
        return false;
    }
    
    public static boolean checkElement(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return false;
        }
        return true;
    }
    
    public static String mkName(String name) {
        return JME_JSON_PREFIX + name;
    }
    
    public static String newInstanceID(Savable obj) {
        if (obj == null) {
            throw new NullPointerException("The object is invalid.");
        }
        return obj.getClass().getName() + '@' + obj.hashCode();
    }
    
    public static JsonObject getJsonObject(JsonArray parent, int index) {
        if (parent.isEmpty()) {
            return null;
        }
        
        JsonElement element = parent.get(index);
        if (element == null || !element.isJsonObject() || element.isJsonNull()) {
            return null;
        }        
        return element.getAsJsonObject();
    }
    
    
    public static JsonArray getJsonArray(JsonArray parent, int index) {
        if (parent.isEmpty()) {
            return null;
        }
        
        JsonElement element = parent.get(index);
        if (element == null || !element.isJsonArray() || element.isJsonNull()) {
            return null;
        }        
        return element.getAsJsonArray();
    }
    
    public static JsonPrimitive getJsonPrimitive(JsonArray parent, int index) {
        if (parent.isEmpty()) {
            return null;
        }
        
        JsonElement element = parent.get(index);
        if (element == null || !element.isJsonPrimitive() || element.isJsonNull()) {
            return null;
        }
        return element.getAsJsonPrimitive();
    }
    
    public static JsonPrimitive getJsonPrimitive(JsonObject parent, String name) {
        return getNativeJsonPrimitive(parent, JME_JSON_PREFIX + name);
    }
    
    public static JsonArray getJsonArray(JsonObject parent, String name) {        
        return getNativeJsonArray(parent, JME_JSON_PREFIX + name);
    }
    
    public static JsonObject getJsonObject(JsonObject parent, String name) {        
        return getNativeJsonObject(parent, JME_JSON_PREFIX + name);
    }
    
    public static JsonPrimitive getNativeJsonPrimitive(JsonObject parent, String name) {
        if (! parent.has(name)) {
            return null;
        }
        
        JsonElement element = parent.get(name);
        if (element == null || !element.isJsonPrimitive() || element.isJsonNull()) {
            return null;
        }
        return element.getAsJsonPrimitive();
    }
    
    public static JsonArray getNativeJsonArray(JsonObject parent, String name) {        
        JsonElement element = parent.get(name);
        if (element == null || !element.isJsonArray() || element.isJsonNull()) {
            return null;
        }
        return element.getAsJsonArray();
    }
    
    public static JsonObject getNativeJsonObject(JsonObject parent, String name) {        
        JsonElement element = parent.get(name);
        if (element == null || !element.isJsonObject() || element.isJsonNull()) {
            return null;
        }
        return element.getAsJsonObject();
    }
}
