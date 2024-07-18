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
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.util.IntMap;
import static com.jme3.export.json.JsonUtilities.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author wil
 */
public class JsonOutputCapsule implements OutputCapsule {
    
    private JsonObject object;
    private JsonExporter exporter;

    public JsonOutputCapsule(JsonObject object, JsonExporter exporter) {
        this.object   = object;
        this.exporter = exporter;
    }
    
    @Override
    public void write(byte value, String string, byte defVal) throws IOException {
        if (!checkName(string) || value == defVal) {
            return;
        }
        object.addProperty(mkName(string), value);
    }

    @Override
    public void write(byte[] value, String string, byte[] defVal) throws IOException {
        if (! checkName(string) || Arrays.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (int i = 0; i < value.length; i++) {
            array.add(value[i]);
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(byte[][] value, String string, byte[][] defVal) throws IOException {
        if (! checkName(string) || Arrays.deepEquals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (byte[] element : value) {
            if (element == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                JsonArray jsonElement = new JsonArray();
                for (int j = 0; j < element.length; j++) {
                    jsonElement.add(element[j]);
                }
                array.add(jsonElement);
            }
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(int value, String string, int defVal) throws IOException {
        if (!checkName(string) || value == defVal) {
            return;
        }
        object.addProperty(mkName(string), value);
    }

    @Override
    public void write(int[] value, String string, int[] defVal) throws IOException {
        if (! checkName(string) || Arrays.equals(value, defVal) || value == null) {
            return;
        }
                
        JsonArray array = new JsonArray();
        for (int i = 0; i < value.length; i++) {
            array.add(value[i]);
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(int[][] value, String string, int[][] defVal) throws IOException {
        if (! checkName(string) || Arrays.deepEquals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (int[] element : value) {
            if (element == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                JsonArray jsonElement = new JsonArray();
                for (int j = 0; j < element.length; j++) {
                    jsonElement.add(element[j]);
                }
                array.add(jsonElement);
            }
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(float value, String string, float defVal) throws IOException {
        if (!checkName(string) || value == defVal) {
            return;
        }
        object.addProperty(mkName(string), value);
    }

    @Override
    public void write(float[] value, String string, float[] defVal) throws IOException {
        if (! checkName(string) || Arrays.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (int i = 0; i < value.length; i++) {
            array.add(value[i]);
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(float[][] value, String string, float[][] defVal) throws IOException {
        if (! checkName(string) || Arrays.deepEquals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (float[] element : value) {
            if (element == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                JsonArray jsonElement = new JsonArray();
                for (int j = 0; j < element.length; j++) {
                    jsonElement.add(element[j]);
                }
                array.add(jsonElement);
            }
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(double value, String string, double defVal) throws IOException {
        if (!checkName(string) || value == defVal) {
            return;
        }
        object.addProperty(mkName(string), value);
    }

    @Override
    public void write(double[] value, String string, double[] defVal) throws IOException {
        if (! checkName(string) || Arrays.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (int i = 0; i < value.length; i++) {
            array.add(value[i]);
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(double[][] value, String string, double[][] defVal) throws IOException {
        if (! checkName(string) || Arrays.deepEquals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (double[] element : value) {
            if (element == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                JsonArray jsonElement = new JsonArray();
                for (int j = 0; j < element.length; j++) {
                    jsonElement.add(element[j]);
                }
                array.add(jsonElement);
            }
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(long value, String string, long defVal) throws IOException {
        if (!checkName(string) || value == defVal) {
            return;
        }
        object.addProperty(mkName(string), value);
    }

    @Override
    public void write(long[] value, String string, long[] defVal) throws IOException {
        if (! checkName(string) || Arrays.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (int i = 0; i < value.length; i++) {
            array.add(value[i]);
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(long[][] value, String string, long[][] defVal) throws IOException {
        if (! checkName(string) || Arrays.deepEquals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (long[] element : value) {
            if (element == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                JsonArray jsonElement = new JsonArray();
                for (int j = 0; j < element.length; j++) {
                    jsonElement.add(element[j]);
                }
                array.add(jsonElement);
            }
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(short value, String string, short defVal) throws IOException {
        if (!checkName(string) || value == defVal) {
            return;
        }
        object.addProperty(mkName(string), value);
    }

    @Override
    public void write(short[] value, String string, short[] defVal) throws IOException {
        if (! checkName(string) || Arrays.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (int i = 0; i < value.length; i++) {
            array.add(value[i]);
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(short[][] value, String string, short[][] defVal) throws IOException {
        if (! checkName(string) || Arrays.deepEquals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (short[] element : value) {
            if (element == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                JsonArray jsonElement = new JsonArray();
                for (int j = 0; j < element.length; j++) {
                    jsonElement.add(element[j]);
                }
                array.add(jsonElement);
            }
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(boolean value, String string, boolean defVal) throws IOException {
        if (!checkName(string) || value == defVal) {
            return;
        }
        object.addProperty(mkName(string), value);
    }

    @Override
    public void write(boolean[] value, String string, boolean[] defVal) throws IOException {
        if (! checkName(string) || Arrays.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (int i = 0; i < value.length; i++) {
            array.add(value[i]);
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(boolean[][] value, String string, boolean[][] defVal) throws IOException {
        if (! checkName(string) || Arrays.deepEquals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (boolean[] element : value) {
            if (element == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                JsonArray jsonElement = new JsonArray();
                for (int j = 0; j < element.length; j++) {
                    jsonElement.add(element[j]);
                }
                array.add(jsonElement);
            }
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(String value, String string, String defVal) throws IOException {
        if (!checkName(string) || Objects.equals(value, defVal)) {
            return;
        }
        object.addProperty(mkName(string), value);
    }

    @Override
    public void write(String[] value, String string, String[] defVal) throws IOException {
        if (! checkName(string) || Arrays.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (String val : value) {
            array.add(val);
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(String[][] value, String string, String[][] defVal) throws IOException {
        if (! checkName(string) || Arrays.deepEquals(value, defVal)) {
            return;
        }        
        if (value == null) {
            value = defVal;
        }
        
        JsonArray array = new JsonArray();
        for (String[] element : value) {
            if (element == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                JsonArray jsonElement = new JsonArray();
                for (String str : element) {
                    if (str == null) {
                        jsonElement.add(JsonNull.INSTANCE);
                    } else {
                        jsonElement.add(str);
                    }
                }
                array.add(jsonElement);
            }
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(BitSet value, String string, BitSet defVal) throws IOException {
        if (! checkName(string) || Objects.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (int i = value.nextSetBit(0); i >= 0; i = value.nextSetBit(i + 1)) {
            array.add(i);
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(Savable value, String string, Savable defVal) throws IOException {
        if (!checkName(string) || Objects.equals(value, defVal)) {
            return;
        }
        object.add(mkName(string), exporter.writeSavableFromCurrentNode(value));
    }

    @Override
    public void write(Savable[] value, String string, Savable[] defVal) throws IOException {
        if (! checkName(string) || Arrays.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (Savable savable : value) {
            if (savable == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                array.add(exporter.writeSavableFromCurrentNode(savable));
            }
        }
        object.add(mkName(string), array);
    }

    @Override
    public void write(Savable[][] value, String string, Savable[][] defVal) throws IOException {
        if (! checkName(string) || Arrays.deepEquals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (Savable[] element : value) {
            if (element == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                JsonArray jsonElement = new JsonArray();
                for (Savable savable : element) {
                    if (savable == null) {
                        jsonElement.add(JsonNull.INSTANCE);
                    } else {
                        jsonElement.add(exporter.writeSavableFromCurrentNode(savable));
                    }
                }
                array.add(jsonElement);
            }
        }
        object.add(mkName(string), array);
    }

    @Override
    public void writeSavableArrayList(ArrayList value, String string, ArrayList defVal) throws IOException {
        if (! checkName(string) || Objects.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (int i = 0; i < value.size(); i++) {
            Object entryList = value.get(i);
            if (! checkSavable(entryList)) {
                throw new IOException("The object at position [" + i + "] cannot be saved.");
            }
            
            Savable savable = (Savable) entryList;
            if (savable == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                array.add(exporter.writeSavableFromCurrentNode(savable));
            }
        }
        object.add(mkName(string), array);
    }

    @Override
    public void writeSavableArrayListArray(ArrayList[] value, String string, ArrayList[] defVal) throws IOException {
        if (! checkName(string) || Arrays.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (int i = 0; i < value.length; i++) {
            ArrayList element = value[i];
            
            if (element == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                JsonArray jsonElement = new JsonArray();
                for (int j = 0; j < element.size(); j++) {
                    Object entryList = element.get(j);
                    if (! checkSavable(entryList)) {
                        throw new IOException("The object at position [" + i + "] cannot be saved.");
                    }
            
                    Savable savable = (Savable) entryList;
                    if (savable == null) {
                        jsonElement.add(JsonNull.INSTANCE);
                    } else {
                        jsonElement.add(exporter.writeSavableFromCurrentNode(savable));
                    }
                }
                array.add(jsonElement);
            }
        }
        object.add(mkName(string), array);
    }

    @Override
    public void writeSavableArrayListArray2D(ArrayList[][] value, String string, ArrayList[][] defVal) throws IOException {
        if (! checkName(string) || Arrays.deepEquals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (int i = 0; i < value.length; i++) {
            ArrayList[] element = value[i];
            if (element == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                JsonArray elementArray = new JsonArray();
                for (int j = 0; j < array.size(); j++) {
                    ArrayList entry = element[j];
                    
                    if (entry == null) {
                        elementArray.add(JsonNull.INSTANCE);
                    } else {
                        JsonArray listArray = new JsonArray();
                        for (int l = 0; l < entry.size(); l++) {
                            Object entryList = entry.get(l);
                            if (! checkSavable(entryList)) {
                                throw new IOException("The object at position [" + i + "] cannot be saved.");
                            }
                            
                            Savable savable = (Savable) entryList;
                            if (savable == null) {
                                listArray.add(JsonNull.INSTANCE);
                            } else {
                                listArray.add(exporter.writeSavableFromCurrentNode(savable));
                            }
                        }
                        
                        elementArray.add(listArray);
                    }
                }
                
                array.add(elementArray);
            }
        }
        
        object.add(mkName(string), array);
    }

    @Override
    public void writeFloatBufferArrayList(ArrayList<FloatBuffer> value, String string, ArrayList<FloatBuffer> defVal) throws IOException {
        if (! checkName(string) || Objects.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (int i = 0; i < value.size(); i++) {
            FloatBuffer buffer = value.get(i);
            if (buffer == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                int pos = buffer.position();
                buffer.rewind();
                
                int ctr = 0;
                while (buffer.hasRemaining()) {
                    ctr++;
                    array.add(buffer.get());
                }
                if (ctr != buffer.limit()) {
                    throw new IOException("'" + string + "' buffer contention resulted in write data consistency.  " 
                                              +   ctr  + " values written when should have written " + buffer.limit());
                }
                buffer.position(pos);
            }
        }
        object.add(mkName(string), array);
    }

    @Override
    public void writeByteBufferArrayList(ArrayList<ByteBuffer> value, String string, ArrayList<ByteBuffer> defVal) throws IOException {
        if (! checkName(string) || Objects.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (int i = 0; i < value.size(); i++) {
            ByteBuffer buffer = value.get(i);
            if (buffer == null) {
                array.add(JsonNull.INSTANCE);
            } else {
                int pos = buffer.position();
                buffer.rewind();
                
                int ctr = 0;
                while (buffer.hasRemaining()) {
                    ctr++;
                    array.add(buffer.get());
                }
                if (ctr != buffer.limit()) {
                    throw new IOException("'" + string + "' buffer contention resulted in write data consistency.  " 
                                              +   ctr  + " values written when should have written " + buffer.limit());
                }
                buffer.position(pos);
            }
        }
        object.add(mkName(string), array);
    }

    @Override
    public void writeSavableMap(Map<? extends Savable, ? extends Savable> value, String string, Map<? extends Savable, ? extends Savable> defVal) throws IOException {
        if (! checkName(string) || Objects.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        for (final Map.Entry<? extends Savable, ? extends Savable> entry : value.entrySet()) {
            JsonObject jsonEntry = new JsonObject();
            
            Savable key = entry.getKey();
            if (key == null) {
                throw new IOException("Key (saveable) is invalid: " + key);
            }
            jsonEntry.add(JME_MAP_KEY, exporter.writeSavableFromCurrentNode(key));
            
            Savable val = entry.getValue();
            if (val == null) {
                jsonEntry.add(JME_MAP_VALUE, JsonNull.INSTANCE);
            } else {
                jsonEntry.add(JME_MAP_VALUE, exporter.writeSavableFromCurrentNode(val));
            }
            
            array.add(jsonEntry);
            
        }
        object.add(mkName(string), array);
    }

    @Override
    public void writeStringSavableMap(Map<String, ? extends Savable> value, String string, Map<String, ? extends Savable> defVal) throws IOException {
        if (! checkName(string) || Objects.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonObject obj = new JsonObject();        
        for (final Map.Entry<String, ? extends Savable> entry : value.entrySet()) {
            String key = entry.getKey();
            if (key == null) {
                throw new IOException("Key (saveable) is invalid: " + key);
            }
            
            Savable val = entry.getValue();
            if (val == null) {
                obj.add(key, JsonNull.INSTANCE);
            } else {
                obj.add(key, exporter.writeSavableFromCurrentNode(val));
            }
        }
        
        object.add(mkName(string), obj);
    }

    @Override
    public void writeIntSavableMap(IntMap<? extends Savable> value, String string, IntMap<? extends Savable> defVal) throws IOException {
        if (! checkName(string) || Objects.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonObject obj = new JsonObject();        
        for (IntMap.Entry<? extends Savable> entry : value){
            Savable val = entry.getValue();
            if (val == null) {
                obj.add(Integer.toString(entry.getKey()), JsonNull.INSTANCE);
            } else {
                obj.add(Integer.toString(entry.getKey()), exporter.writeSavableFromCurrentNode(val));
            }
        }
        
        object.add(mkName(string), obj);
    }

    @Override
    public void write(FloatBuffer value, String string, FloatBuffer defVal) throws IOException {
        if (! checkName(string) || Objects.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        int pos = value.position();
        value.rewind();

        int ctr = 0;
        while (value.hasRemaining()) {
            ctr++;
            array.add(value.get());
        }
        if (ctr != value.limit()) {
            throw new IOException("'" + string + "' buffer contention resulted in write data consistency.  "
                                      +   ctr  + " values written when should have written " + value.limit());
        }
        value.position(pos);
        object.add(mkName(string), array);
    }

    @Override
    public void write(IntBuffer value, String string, IntBuffer defVal) throws IOException {
        if (! checkName(string) || Objects.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        int pos = value.position();
        value.rewind();

        int ctr = 0;
        while (value.hasRemaining()) {
            ctr++;
            array.add(value.get());
        }
        if (ctr != value.limit()) {
            throw new IOException("'" + string + "' buffer contention resulted in write data consistency.  "
                                      +   ctr  + " values written when should have written " + value.limit());
        }
        value.position(pos);
        object.add(mkName(string), array);
    }

    @Override
    public void write(ByteBuffer value, String string, ByteBuffer defVal) throws IOException {
        if (! checkName(string) || Objects.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        int pos = value.position();
        value.rewind();

        int ctr = 0;
        while (value.hasRemaining()) {
            ctr++;
            array.add(value.get());
        }
        if (ctr != value.limit()) {
            throw new IOException("'" + string + "' buffer contention resulted in write data consistency.  "
                                      +   ctr  + " values written when should have written " + value.limit());
        }
        value.position(pos);
        object.add(mkName(string), array);
    }

    @Override
    public void write(ShortBuffer value, String string, ShortBuffer defVal) throws IOException {
        if (! checkName(string) || Objects.equals(value, defVal) || value == null) {
            return;
        }
        
        JsonArray array = new JsonArray();
        int pos = value.position();
        value.rewind();

        int ctr = 0;
        while (value.hasRemaining()) {
            ctr++;
            array.add(value.get());
        }
        if (ctr != value.limit()) {
            throw new IOException("'" + string + "' buffer contention resulted in write data consistency.  "
                                      +   ctr  + " values written when should have written " + value.limit());
        }
        value.position(pos);
        object.add(mkName(string), array);
    }

    @Override
    public void write(Enum value, String string, Enum defVal) throws IOException {
        if (! checkName(string) || Objects.equals(value, defVal) || value == null) {
            return;
        }
        object.addProperty(mkName(string), String.valueOf(value));
    }
    
}
