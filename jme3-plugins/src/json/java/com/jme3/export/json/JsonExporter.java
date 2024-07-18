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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import com.jme3.export.FormatVersion;
import com.jme3.export.JmeExporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.export.SavableClassUtil;
import static com.jme3.export.json.JsonUtilities.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wil
 */
public class JsonExporter implements JmeExporter {
    
    private final Map<Savable, JsonObject> writtenSavables = new HashMap<>();
    private final Map<Savable, JsonOutputCapsule> capsuleTable = new HashMap<>();

    public JsonExporter() {
    }
    
    @Override
    public void save(Savable object, OutputStream out) throws IOException {
        JsonObject scene    = new JsonObject();        
        JsonObject metadata = new JsonObject();
        metadata.addProperty(JME_SIGNATURE, FormatVersion.SIGNATURE);
        metadata.addProperty(JME_FORMAT_VERSION, FormatVersion.VERSION);
        
        scene.add(JME_JSON_META_DATA, metadata);
        
        JsonObject root = writeSavableFromCurrentNode(object);
        scene.add(JME_JSON_BINARIES, root);
        
        try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(out))) {
            writer.setIndent("  ");
            
            Gson gson = new Gson();
            gson.toJson(scene, writer);
        }
    }

    @Override
    public void save(Savable object, File file, boolean mkdirs) throws IOException {
        File parentDirectory = file.getParentFile();
        if (parentDirectory != null && !parentDirectory.exists() && mkdirs) {
            parentDirectory.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            save(object, bos);
        }
    }

    @Override
    public OutputCapsule getCapsule(Savable object) {
        return this.capsuleTable.get(object);
    }
    
    final JsonObject writeSavableFromCurrentNode(Savable obj) throws IOException {
        JsonObject object = writtenSavables.get(obj);
        if (object == null) {
            JsonObject o = new JsonObject();
            
            // jME3 NEW: Append version number(s)
            int[] versions = SavableClassUtil.getSavableVersions(obj.getClass());
            JsonArray array = new JsonArray();
            for (final int v : versions) {
                array.add(v);
            }
            
            o.addProperty(JME_CLASS, obj.getClass().getName());
            o.addProperty(JME_ID, newInstanceID(obj));
            o.add(JME_VERSIONS, array);
            
            writtenSavables.put(obj, o);
            capsuleTable.put(obj, new JsonOutputCapsule(o, this));
            
            obj.write(this);
            
            capsuleTable.remove(obj);
            return o;
        } else {
            JsonObject o = new JsonObject();
            JsonPrimitive jp = object.getAsJsonPrimitive(JME_ID);
            o.addProperty(JME_REFERENCE, jp.getAsString());
            return o;
        }
    }
    
    public static JsonExporter getInstance() {
        return new JsonExporter();
    }
}
