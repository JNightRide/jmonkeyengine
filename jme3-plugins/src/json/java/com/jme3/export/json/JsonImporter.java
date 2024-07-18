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
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetManager;
import com.jme3.export.FormatVersion;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.export.SavableClassUtil;
import static com.jme3.export.json.JsonUtilities.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wil
 */
public class JsonImporter implements JmeImporter {

    private final Map<String, Savable> references = new HashMap<>();
    private final Map<Savable, JsonInputCapsule> capsuleTable = new HashMap<>();

    private AssetManager assetManager;
    private int formatVersion;
    
    public JsonImporter() {
    }
    
    @Override
    public InputCapsule getCapsule(Savable obj) {
        return this.capsuleTable.get(obj);
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    @Override
    public AssetManager getAssetManager() {
        return this.assetManager;
    }

    @Override
    public int getFormatVersion() {
        return this.formatVersion;
    }

    @Override
    public Object load(AssetInfo ai) throws IOException {
        assetManager = ai.getManager();        
        try (InputStream in = ai.openStream()) {
            return load(in);
        }
    }
    
    public Savable load(File file) throws IOException {
        return load(new FileInputStream(file));
    }
    
    public Savable load(InputStream in) throws IOException {
        JsonElement rootElement = JsonParser.parseReader(new InputStreamReader(in));
        if (! checkElement(rootElement) || !rootElement.isJsonObject()) {
            return null;
        }
        
        JsonObject metadata = getNativeJsonObject(rootElement.getAsJsonObject(), JME_JSON_META_DATA);
        if (! checkElement(metadata)) {
            throw new IOException("File is corrupt, node '" + JME_JSON_META_DATA + "' not located");
        }
        
        // Try to read signature
        int maybeSignature = metadata.getAsJsonPrimitive(JME_SIGNATURE).getAsInt();
        if (maybeSignature == FormatVersion.SIGNATURE){
            // this is a new version J3O file
            formatVersion = metadata.getAsJsonPrimitive(JME_FORMAT_VERSION).getAsInt();
            
            // check if this binary is from the future
            if (formatVersion > FormatVersion.VERSION){
                throw new IOException("The binary file is of newer version than expected! " + 
                                      formatVersion + " > " + FormatVersion.VERSION);
            }
        } else {            
            // 0 indicates version before we started adding
            // version numbers
            formatVersion = 0; 
        }
        
        JsonObject scene = getNativeJsonObject(rootElement.getAsJsonObject(), JME_JSON_BINARIES);
        if (! checkElement(scene)) {
            throw new IOException("File is corrupt, node '" + JME_JSON_BINARIES + "' not located");
        }
        
        return readSavableFromCurrentNode(scene, null);
    }
    
    final Savable readSavableFromCurrentNode(JsonObject object, Savable defVal) throws IllegalStateException, IOException {
        if (object.has(JME_REFERENCE)) {
            JsonPrimitive primitive = getNativeJsonPrimitive(object, JME_REFERENCE);
            if (! checkElement(primitive)) {
                return defVal;
            }
            
            return references.get(primitive.getAsString());
        } else {
            try {
                JsonPrimitive clazz = getNativeJsonPrimitive(object, JME_CLASS);
                if (! checkElement(clazz)) {
                    throw new IllegalStateException("No reference to the object class was found.");
                }
            
                Savable obj = SavableClassUtil.fromName(clazz.getAsString());
                
                int[] initClassHierarchyVersions = null;
                JsonArray versions = getNativeJsonArray(object, JME_VERSIONS);

                if (checkElement(versions)) {
                    int length = versions.size();
                    initClassHierarchyVersions = new int[length];

                    for (int i = 0; i < length; i++) {
                        JsonPrimitive primitive = getJsonPrimitive(versions, i);
                        if (! checkElement(primitive)) {
                            throw new IOException("Error getting versions of object " + clazz.getAsString());
                        }

                        initClassHierarchyVersions[i] = primitive.getAsInt();
                    }
                }

                JsonPrimitive ref = getNativeJsonPrimitive(object, JME_ID);
                if (! checkElement(ref)) {
                    throw new IllegalStateException("Cannot find the reference ID for this gurdable object: " + obj.getClass().getName());
                }

                references.put(ref.getAsString(), obj);
                capsuleTable.put(obj, new JsonInputCapsule(object, obj,this, initClassHierarchyVersions));

                obj.read(this);

                capsuleTable.remove(obj);
                return obj;
            } catch (ClassNotFoundException | IllegalAccessException |
                     InstantiationException | InvocationTargetException e) {
                throw new IOException("Cannot load object ", e);
            }
        }
    }
    
    public static JsonImporter getInstance() {
        return new JsonImporter();
    }
}
