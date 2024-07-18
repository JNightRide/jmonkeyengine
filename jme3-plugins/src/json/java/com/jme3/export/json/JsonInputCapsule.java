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
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jme3.export.InputCapsule;
import com.jme3.export.Savable;
import com.jme3.export.SavableClassUtil;
import com.jme3.util.IntMap;
import com.jme3.util.BufferUtils;
import static com.jme3.export.json.JsonUtilities.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wil
 */
public class JsonInputCapsule implements InputCapsule {
    
    private JsonObject object;
    private JsonImporter importer;
    private Savable savable;
    
    private int[] classHierarchyVersions;

    public JsonInputCapsule(JsonObject object, Savable savable, JsonImporter importer, int[] classHierarchyVersions) {
        this.classHierarchyVersions = classHierarchyVersions;
        this.savable  = savable;
        this.object   = object;
        this.importer = importer;
    }
    
    @Override
    public int getSavableVersion(Class<? extends Savable> type) {
        return SavableClassUtil.getSavedSavableVersion(savable, type, 
                                            classHierarchyVersions, importer.getFormatVersion());
    }

    @Override
    public byte readByte(String string, byte b) throws IOException {
        if (! checkName(string)) {
            return b;
        }
        JsonPrimitive primitive = getJsonPrimitive(object, string);
        if (! checkElement(primitive)) {
            return b;
        }
        return primitive.getAsByte();
    }

    @Override
    public byte[] readByteArray(String string, byte[] bytes) throws IOException {
        if (! checkName(string)) {
            return bytes;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return bytes;
        }
        
        int length  = array.size();
        byte[] bits = new byte[length];
        
        for (int i = 0; i < length; i++) {
            JsonPrimitive primitive = getJsonPrimitive(array, i);
            if (! checkElement(primitive)) {
                throw new IOException("Invalid element, could not convert object at position [" + i +"] to a primitive object (byte)");
            }
            
            bits[i] = primitive.getAsByte();
        }
        return bits;
    }

    @Override
    public byte[][] readByteArray2D(String string, byte[][] bytes) throws IOException {
        if (! checkName(string)) {
            return bytes;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return bytes;
        }
        
        int length = array.size();        
        List<byte[]> bbits = new ArrayList<>();
        
        for (int i = 0; i < length; i++) {
            JsonArray element = getJsonArray(array, i);
            if (checkElement(element)) {
                int len = element.size();
                byte[] bits = new byte[len];

                for (int j = 0; j < len; j++) {
                    JsonPrimitive primitive = getJsonPrimitive(element, j);                
                    if (! checkElement(primitive)) {
                        throw new IOException("Invalid element, could not convert object at position [" + i + "][" + j +"] to a primitive object (byte)");
                    }

                    bits[j] = primitive.getAsByte();
                }            
                bbits.add(bits);
            } else {
                bbits.add(null);
            }
        }
        
        return bbits.toArray(new byte[0][]);
    }

    @Override
    public int readInt(String string, int i) throws IOException {
        if (! checkName(string)) {
            return i;
        }
        JsonPrimitive primitive = getJsonPrimitive(object, string);
        if (! checkElement(primitive)) {
            return i;
        }
        return primitive.getAsInt();
    }

    @Override
    public int[] readIntArray(String string, int[] ints) throws IOException {
        if (! checkName(string)) {
            return ints;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return ints;
        }
        
        int length = array.size();
        int[] nums = new int[length];
        
        for (int i = 0; i < length; i++) {
            JsonPrimitive primitive = getJsonPrimitive(array, i);
            if (! checkElement(primitive)) {
                throw new IOException("Invalid element, could not convert object at position [" + i +"] to a primitive object (int)");
            }
            
            nums[i] = primitive.getAsInt();
        }
        return nums;
    }

    @Override
    public int[][] readIntArray2D(String string, int[][] ints) throws IOException {
        if (! checkName(string)) {
            return ints;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return ints;
        }
        
        int length = array.size();        
        List<int[]> bnums = new ArrayList<>();
        
        for (int i = 0; i < length; i++) {
            JsonArray element = getJsonArray(array, i);
            if (checkElement(element)) {
                int len = element.size();
                int[] nums = new int[len];

                for (int j = 0; j < len; j++) {
                    JsonPrimitive primitive = getJsonPrimitive(element, j);                
                    if (! checkElement(primitive)) {
                        throw new IOException("Invalid element, could not convert object at position [" + i + "][" + j +"] to a primitive object (int)");
                    }

                    nums[j] = primitive.getAsInt();
                }            
                bnums.add(nums);
            } else {
                bnums.add(null);
            }
        }
        
        return bnums.toArray(new int[0][]);
    }

    @Override
    public float readFloat(String string, float f) throws IOException {
        if (! checkName(string)) {
            return f;
        }
        JsonPrimitive primitive = getJsonPrimitive(object, string);
        if (! checkElement(primitive)) {
            return f;
        }
        return primitive.getAsFloat();
    }

    @Override
    public float[] readFloatArray(String string, float[] floats) throws IOException {
        if (! checkName(string)) {
            return floats;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return floats;
        }
        
        int length = array.size();
        float[] nums = new float[length];
        
        for (int i = 0; i < length; i++) {
            JsonPrimitive primitive = getJsonPrimitive(array, i);
            if (! checkElement(primitive)) {
                throw new IOException("Invalid element, could not convert object at position [" + i +"] to a primitive object (float)");
            }
            
            nums[i] = primitive.getAsFloat();
        }
        return nums;
    }

    @Override
    public float[][] readFloatArray2D(String string, float[][] floats) throws IOException {
        if (! checkName(string)) {
            return floats;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return floats;
        }
        
        int length = array.size();        
        List<float[]> bnums = new ArrayList<>();
        
        for (int i = 0; i < length; i++) {
            JsonArray element = getJsonArray(array, i);
            if (checkElement(element)) {
                int len = element.size();
                float[] nums = new float[len];

                for (int j = 0; j < len; j++) {
                    JsonPrimitive primitive = getJsonPrimitive(element, j);                
                    if (! checkElement(primitive)) {
                        throw new IOException("Invalid element, could not convert object at position [" + i + "][" + j +"] to a primitive object (float)");
                    }

                    nums[j] = primitive.getAsFloat();
                }            
                bnums.add(nums);
            } else {
                bnums.add(null);
            }
        }
        
        return bnums.toArray(new float[0][]);
    }

    @Override
    public double readDouble(String string, double d) throws IOException {
        if (! checkName(string)) {
            return d;
        }
        JsonPrimitive primitive = getJsonPrimitive(object, string);
        if (! checkElement(primitive)) {
            return d;
        }
        return primitive.getAsDouble();
    }

    @Override
    public double[] readDoubleArray(String string, double[] doubles) throws IOException {
        if (! checkName(string)) {
            return doubles;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return doubles;
        }
        
        int length = array.size();
        double[] nums = new double[length];
        
        for (int i = 0; i < length; i++) {
            JsonPrimitive primitive = getJsonPrimitive(array, i);
            if (! checkElement(primitive)) {
                throw new IOException("Invalid element, could not convert object at position [" + i +"] to a primitive object (double)");
            }
            
            nums[i] = primitive.getAsDouble();
        }
        return nums;
    }

    @Override
    public double[][] readDoubleArray2D(String string, double[][] doubles) throws IOException {
        if (! checkName(string)) {
            return doubles;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return doubles;
        }
        
        int length = array.size();        
        List<double[]> bnums = new ArrayList<>();
        
        for (int i = 0; i < length; i++) {
            JsonArray element = getJsonArray(array, i);
            if (checkElement(element)) {
                int len = element.size();
                double[] nums = new double[len];

                for (int j = 0; j < len; j++) {
                    JsonPrimitive primitive = getJsonPrimitive(element, j);                
                    if (! checkElement(primitive)) {
                        throw new IOException("Invalid element, could not convert object at position [" + i + "][" + j +"] to a primitive object (double)");
                    }

                    nums[j] = primitive.getAsDouble();
                }            
                bnums.add(nums);
            } else {
                bnums.add(null);
            }
        }
        
        return bnums.toArray(new double[0][]);
    }

    @Override
    public long readLong(String string, long l) throws IOException {
        if (! checkName(string)) {
            return l;
        }
        JsonPrimitive primitive = getJsonPrimitive(object, string);
        if (! checkElement(primitive)) {
            return l;
        }
        return primitive.getAsLong();
    }

    @Override
    public long[] readLongArray(String string, long[] longs) throws IOException {
        if (! checkName(string)) {
            return longs;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return longs;
        }
        
        int length = array.size();
        long[] nums = new long[length];
        
        for (int i = 0; i < length; i++) {
            JsonPrimitive primitive = getJsonPrimitive(array, i);
            if (! checkElement(primitive)) {
                throw new IOException("Invalid element, could not convert object at position [" + i +"] to a primitive object (long)");
            }
            
            nums[i] = primitive.getAsLong();
        }
        return nums;
    }

    @Override
    public long[][] readLongArray2D(String string, long[][] longs) throws IOException {
        if (! checkName(string)) {
            return longs;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return longs;
        }
        
        int length = array.size();        
        List<long[]> bnums = new ArrayList<>();
        
        for (int i = 0; i < length; i++) {
            JsonArray element = getJsonArray(array, i);
            if (checkElement(element)) {
                int len = element.size();
                long[] nums = new long[len];

                for (int j = 0; j < len; j++) {
                    JsonPrimitive primitive = getJsonPrimitive(element, j);                
                    if (! checkElement(primitive)) {
                        throw new IOException("Invalid element, could not convert object at position [" + i + "][" + j +"] to a primitive object (long)");
                    }

                    nums[j] = primitive.getAsLong();
                }            
                bnums.add(nums);
            } else {
                bnums.add(null);
            }
        }
        
        return bnums.toArray(new long[0][]);
    }

    @Override
    public short readShort(String string, short s) throws IOException {
        if (! checkName(string)) {
            return s;
        }
        JsonPrimitive primitive = getJsonPrimitive(object, string);
        if (! checkElement(primitive)) {
            return s;
        }
        return primitive.getAsShort();
    }

    @Override
    public short[] readShortArray(String string, short[] shorts) throws IOException {
        if (! checkName(string)) {
            return shorts;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return shorts;
        }
        
        int length = array.size();
        short[] nums = new short[length];
        
        for (int i = 0; i < length; i++) {
            JsonPrimitive primitive = getJsonPrimitive(array, i);
            if (! checkElement(primitive)) {
                throw new IOException("Invalid element, could not convert object at position [" + i +"] to a primitive object (short)");
            }
            
            nums[i] = primitive.getAsShort();
        }
        return nums;
    }

    @Override
    public short[][] readShortArray2D(String string, short[][] shorts) throws IOException {
        if (! checkName(string)) {
            return shorts;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return shorts;
        }
        
        int length = array.size();        
        List<short[]> bnums = new ArrayList<>();
        
        for (int i = 0; i < length; i++) {
            JsonArray element = getJsonArray(array, i);
            if (checkElement(element)) {
                int len = element.size();
                short[] nums = new short[len];

                for (int j = 0; j < len; j++) {
                    JsonPrimitive primitive = getJsonPrimitive(element, j);                
                    if (! checkElement(primitive)) {
                        throw new IOException("Invalid element, could not convert object at position [" + i + "][" + j +"] to a primitive object (short)");
                    }

                    nums[j] = primitive.getAsShort();
                }            
                bnums.add(nums);
            } else {
                bnums.add(null);
            }
        }
        
        return bnums.toArray(new short[0][]);
    }

    @Override
    public boolean readBoolean(String string, boolean bln) throws IOException {
        if (! checkName(string)) {
            return bln;
        }
        JsonPrimitive primitive = getJsonPrimitive(object, string);
        if (! checkElement(primitive)) {
            return bln;
        }
        return primitive.getAsBoolean();
    }

    @Override
    public boolean[] readBooleanArray(String string, boolean[] blns) throws IOException {
        if (! checkName(string)) {
            return blns;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return blns;
        }
        
        int length = array.size();
        boolean[] bools = new boolean[length];
        
        for (int i = 0; i < length; i++) {
            JsonPrimitive primitive = getJsonPrimitive(array, i);
            if (! checkElement(primitive)) {
                throw new IOException("Invalid element, could not convert object at position [" + i +"] to a primitive object (boolean)");
            }
            
            bools[i] = primitive.getAsBoolean();
        }
        return bools;
    }

    @Override
    public boolean[][] readBooleanArray2D(String string, boolean[][] blns) throws IOException {
        if (! checkName(string)) {
            return blns;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return blns;
        }
        
        int length = array.size();        
        List<boolean[]> bbools = new ArrayList<>();
        
        for (int i = 0; i < length; i++) {
            JsonArray element = getJsonArray(array, i);
            if (checkElement(element)) {
                int len = element.size();
                boolean[] nboo = new boolean[len];

                for (int j = 0; j < len; j++) {
                    JsonPrimitive primitive = getJsonPrimitive(element, j);                
                    if (! checkElement(primitive)) {
                        throw new IOException("Invalid element, could not convert object at position [" + i + "][" + j +"] to a primitive object (boolean)");
                    }

                    nboo[j] = primitive.getAsBoolean();
                }            
                bbools.add(nboo);
            } else {
                bbools.add(null);
            }
        }
        
        return bbools.toArray(new boolean[0][]);
    }

    @Override
    public String readString(String string, String defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        JsonPrimitive primitive = getJsonPrimitive(object, string);
        if (! checkElement(primitive)) {
            return defVal;
        }
        return primitive.getAsString();
    }

    @Override
    public String[] readStringArray(String string, String[] defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        int length = array.size();
        String[] str = new String[length];
        
        for (int i = 0; i < length; i++) {
            JsonPrimitive primitive = getJsonPrimitive(array, i);
            if (!checkElement(primitive)) {
                str[i] = null;
            } else {
                str[i] = primitive.getAsString();
            }
        }
        return str;
    }

    @Override
    public String[][] readStringArray2D(String string, String[][] defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        int length = array.size();        
        List<String[]> bbuff = new ArrayList<>();
        
        for (int i = 0; i < length; i++) {
            JsonArray element = getJsonArray(array, i);
            if (checkElement(element)) {
                int len = element.size();
                String[] str = new String[len];

                for (int j = 0; j < len; j++) {
                    JsonPrimitive primitive = getJsonPrimitive(element, j);                
                    if (! checkElement(primitive)) {
                        str[j] = null;
                    } else {
                        str[j] = primitive.getAsString();
                    }
                }            
                bbuff.add(str);
            } else {
                bbuff.add(null);
            }
        }
        
        return bbuff.toArray(new String[0][]);
    }

    @Override
    public BitSet readBitSet(String string, BitSet defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        int length = array.size();
        BitSet bitSet = new BitSet();
        
        for (int i = 0; i < length; i++) {
            JsonPrimitive primitive = getJsonPrimitive(array, i);
            if (! checkElement(primitive)) {
                throw new IOException("Invalid element, could not convert object at position [" + i +"] to a primitive object (int - BitSet)");
            }
            
            int isSet = primitive.getAsInt();
            if (isSet == 1) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    @Override
    public Savable readSavable(String string, Savable  defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonObject obj = getJsonObject(object, string);
        if (! checkElement(obj)) {
            return defVal;
        }
        
        try {
            return importer.readSavableFromCurrentNode(obj, defVal);
        } catch (IOException | IllegalStateException  e) {
            throw new IOException(e);
        }
    }
    
    @Override
    public Savable[] readSavableArray(String string, Savable[] defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        int length = array.size();
        Savable[] objs = new Savable[length];
        
        for (int i = 0; i < length; i++) {
            JsonObject obj = getJsonObject(array, i);
            if (checkElement(obj)) {
                objs[i] = importer.readSavableFromCurrentNode(obj, null);
            } else {
                objs[i] = null;
            }
        }
        return objs;
    }

    @Override
    public Savable[][] readSavableArray2D(String string, Savable[][] defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        int length = array.size();        
        List<Savable[]> bbuff = new ArrayList<>();
        
        for (int i = 0; i < length; i++) {
            JsonArray element = getJsonArray(array, i);
            if (checkElement(element)) {
                int len = element.size();
                Savable[] objs = new Savable[len];

                for (int j = 0; j < len; j++) {
                    JsonObject entry = getJsonObject(element, j);
                    if (! checkElement(entry)) {
                        objs[j] = null;
                    } else {
                        objs[j] = importer.readSavableFromCurrentNode(entry, null);
                    }
                }            
                bbuff.add(objs);
            } else {
                bbuff.add(null);
            }
        }
        
        return bbuff.toArray(new Savable[0][]);
    }

    @Override
    public ArrayList readSavableArrayList(String string, ArrayList defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        int length = array.size();
        ArrayList<Savable> objs = new ArrayList<>();
        
        for (int i = 0; i < length; i++) {
            JsonObject obj = getJsonObject(array, i);
            if (checkElement(obj)) {
                objs.add(importer.readSavableFromCurrentNode(obj, null));
            } else {
                objs.add(null);
            }
        }
        return objs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList[] readSavableArrayListArray(String string, ArrayList[] defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        int length = array.size();
        ArrayList<Savable>[] bbuff = new ArrayList[length];
        
        for (int i = 0; i < length; i++) {
            JsonArray element = getJsonArray(array, i);
            if (checkElement(element)) {
                int len = element.size();
                ArrayList<Savable> objs = new ArrayList<>();

                for (int j = 0; j < len; j++) {
                    JsonObject entry = getJsonObject(element, j);
                    if (! checkElement(entry)) {
                        objs.add(null);
                    } else {
                        objs.add(importer.readSavableFromCurrentNode(entry, null));
                    }
                }            
                bbuff[i] = objs;
            } else {
                bbuff[i] = null;
            }
        }
        
        return bbuff;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList[][] readSavableArrayListArray2D(String string, ArrayList[][] defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        int length = array.size();        
        List<ArrayList[]> bbuff = new ArrayList<>();
        
        for (int i = 0; i < length; i++) {
            JsonArray element = getJsonArray(array, i);
            if (checkElement(element)) {
                int len = element.size();
                ArrayList<Savable>[] objs = new ArrayList[len];

                for (int j = 0; j < len; j++) {
                    JsonArray entryElement = getJsonArray(element, j);
                    if (checkElement(entryElement)) {
                        int count = entryElement.size();
                        ArrayList<Savable> buffer = new ArrayList<>();
                        
                        for (int l = 0; l < count; l++) {
                            JsonObject entry = getJsonObject(entryElement, l);
                            if (checkElement(entry)) {
                                buffer.add(importer.readSavableFromCurrentNode(entry, null));
                            } else {
                                bbuff.add(null);
                            }
                        }
                        
                        objs[j] = buffer;
                    } else {
                        objs[j] = null;
                    }
                }            
                bbuff.add(objs);
            } else {
                bbuff.add(null);
            }
        }
        
        return bbuff.toArray(new ArrayList[0][]);
    }

    @Override
    public ArrayList<FloatBuffer> readFloatBufferArrayList(String string, ArrayList<FloatBuffer> defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        ArrayList<FloatBuffer> buff = new ArrayList<>();        
        for (int i = 0; i < array.size(); i++) {
            JsonArray element = getJsonArray(array, i);
            if (checkElement(element)) {
                int length = element.size();
                FloatBuffer buffer = BufferUtils.createFloatBuffer(length);
                
                for (int j = 0; j < length; j++) {
                    JsonPrimitive primitive = getJsonPrimitive(element, j);
                    if (! checkElement(primitive)) {
                        throw new IOException("Invalid element, could not convert object at position [" + i +"] to a primitive object (float - FloatBuffer)");
                    }                    
                    buffer.put(primitive.getAsFloat());
                }
                
                buffer.flip();
                buff.add(buffer);
            } else {
                buff.add(null);
            }
        }
        return buff;
    }

    @Override
    public ArrayList<ByteBuffer> readByteBufferArrayList(String string, ArrayList<ByteBuffer> defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        ArrayList<ByteBuffer> buff = new ArrayList<>();        
        for (int i = 0; i < array.size(); i++) {
            JsonArray element = getJsonArray(array, i);
            if (checkElement(element)) {
                int length = element.size();
                ByteBuffer buffer = BufferUtils.createByteBuffer(length);
                
                for (int j = 0; j < length; j++) {
                    JsonPrimitive primitive = getJsonPrimitive(array, j);
                    if (! checkElement(primitive)) {
                        throw new IOException("Invalid element, could not convert object at position [" + i +"] to a primitive object (byte - ByteBuffer)");
                    }                    
                    buffer.put(primitive.getAsByte());
                }
                
                buffer.flip();
                buff.add(buffer);
            } else {
                buff.add(null);
            }
        }
        return buff;
    }

    @Override
    public Map<? extends Savable, ? extends Savable> readSavableMap(String string, Map<? extends Savable, ? extends Savable> defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        try {
            Map<Savable, Savable> map = new HashMap<>();            
            int length = array.size();
            
            for (int i = 0; i < length; i++) {
                JsonObject entry = getJsonObject(array, i);
                if (! checkElement(entry)) {
                     throw new IOException("Invalid element, could not convert object at position [" + i + "] to a map object (Entry<?,?>)");
                }
                
                JsonObject mapKey = getNativeJsonObject(entry, JME_MAP_KEY);
                JsonObject mapVal = getNativeJsonObject(entry, JME_MAP_VALUE);
                
                if (! checkElement(mapKey)) {
                    throw new IOException("invalid key [" + i + "] (Map<?,?>)");
                }
                
                Savable key = importer.readSavableFromCurrentNode(mapKey, null);                
                if (key == null) {
                    throw new IOException("Invalid key [" + i + "] (Map.Entry<?,?> - Read)");
                }
                
                if (checkElement(mapVal)) {
                    map.put(key, importer.readSavableFromCurrentNode(mapVal, null));
                } else {
                    map.put(key, null);
                }
            }
            return map;
        } catch (IOException | IllegalStateException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Map<String, ? extends Savable> readStringSavableMap(String string, Map<String, ? extends Savable> defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonObject obj = getJsonObject(object, string);
        if (! checkElement(obj)) {
            return defVal;
        }
        
        try {
            Map<String, Savable> map = new HashMap<>();
            for (final String entry : obj.keySet()) {
                JsonObject value = getNativeJsonObject(obj, entry);
                if (checkElement(value)) {
                    map.put(entry, importer.readSavableFromCurrentNode(value, null));
                } else {
                    map.put(entry, null);
                }
            }            
            return map;
        } catch (IOException | IllegalStateException e) {
            throw new IOException(e);
        }
    }

    @Override
    public IntMap<? extends Savable> readIntSavableMap(String string, IntMap<? extends Savable> defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonObject obj = getJsonObject(object, string);
        if (! checkElement(obj)) {
            return defVal;
        }
        
        try {
            IntMap<Savable> map = new IntMap<>();
            for (final String entry : obj.keySet()) {
                JsonObject value = getNativeJsonObject(obj, entry);
                if (checkElement(value)) {
                    map.put(Integer.parseInt(entry), importer.readSavableFromCurrentNode(value, null));
                } else {
                    map.put(Integer.parseInt(entry), null);
                }
            }            
            return map;
        } catch (IOException | IllegalStateException | NumberFormatException e) {
            throw new IOException(e);
        }
    }

    @Override
    public FloatBuffer readFloatBuffer(String string, FloatBuffer defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        int length = array.size();
        FloatBuffer buffer = BufferUtils.createFloatBuffer(length);

        for (int i = 0; i < length; i++) {
            JsonPrimitive primitive = getJsonPrimitive(array, i);
            if (!checkElement(primitive)) {
                throw new IOException("Invalid element, could not convert object at position [" + i + "] to a primitive object (float - FloatBuffer)");
            }
            buffer.put(primitive.getAsFloat());
        }
        buffer.flip();
        return buffer;
    }

    @Override
    public IntBuffer readIntBuffer(String string, IntBuffer defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        int length = array.size();
        IntBuffer buffer = BufferUtils.createIntBuffer(length);

        for (int i = 0; i < length; i++) {
            JsonPrimitive primitive = getJsonPrimitive(array, i);
            if (!checkElement(primitive)) {
                throw new IOException("Invalid element, could not convert object at position [" + i + "] to a primitive object (int - IntBuffer)");
            }
            buffer.put(primitive.getAsInt());
        }
        buffer.flip();
        return buffer;
    }

    @Override
    public ByteBuffer readByteBuffer(String string, ByteBuffer defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        int length = array.size();
        ByteBuffer buffer = BufferUtils.createByteBuffer(length);

        for (int i = 0; i < length; i++) {
            JsonPrimitive primitive = getJsonPrimitive(array, i);
            if (!checkElement(primitive)) {
                throw new IOException("Invalid element, could not convert object at position [" + i + "] to a primitive object (byte - ByteBuffer)");
            }
            buffer.put(primitive.getAsByte());
        }
        buffer.flip();
        return buffer;
    }

    @Override
    public ShortBuffer readShortBuffer(String string, ShortBuffer defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        
        JsonArray array = getJsonArray(object, string);
        if (! checkElement(array)) {
            return defVal;
        }
        
        int length = array.size();
        ShortBuffer buffer = BufferUtils.createShortBuffer(length);

        for (int i = 0; i < length; i++) {
            JsonPrimitive primitive = getJsonPrimitive(array, i);
            if (!checkElement(primitive)) {
                throw new IOException("Invalid element, could not convert object at position [" + i + "] to a primitive object (short - ShortBuffer)");
            }
            buffer.put(primitive.getAsShort());
        }
        buffer.flip();
        return buffer;
    }

    @Override
    public <T extends Enum<T>> T readEnum(String string, Class<T> type, T defVal) throws IOException {
        if (! checkName(string)) {
            return defVal;
        }
        JsonPrimitive primitive = getJsonPrimitive(object, string);
        if (! checkElement(primitive)) {
            return defVal;
        }
        return Enum.valueOf(type, primitive.getAsString());
    }
}
