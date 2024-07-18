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

import com.jme3.app.SimpleApplication;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wil
 */
public class Test extends SimpleApplication {
    
    private static final String HOME_PATH;
    static {
        HOME_PATH = System.getProperty("user.home");
    }
    
    public static void main(String[] args) {
        Test app = new Test();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        getFlyByCamera().setEnabled(false);

        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.randomColor());
        geom.setMaterial(mat);

        CubeControl control = new CubeControl();
        control.setSpeed(1.5f);
        control.setDirection(Vector3f.UNIT_XYZ);

        geom.addControl(control);
        
        exporter(geom);
        
        geom.move(-2, 0, 0);
        rootNode.attachChild(geom);
        
        
        try {
            Geometry imGeom = importer(new FileInputStream(HOME_PATH + "/cube.jmo.json"));
            imGeom.move(2, 0, 0);
            
            rootNode.attachChild(imGeom);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void exporter(Savable savable) {
        try {
            File file = new File(HOME_PATH + "/cube.jmo.json");
            
            JsonExporter exporter = JsonExporter.getInstance();
            exporter.save(savable, file);
            
            System.out.println("[ OUT ] :Dir >> " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("[ ERR ] :Importer", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T> T importer(InputStream in) {
        try {
            JsonImporter importer = JsonImporter.getInstance();
            importer.setAssetManager(assetManager);
            return (T) importer.load(in);
        } catch (IOException e) {
            throw new RuntimeException("[ ERR ] :Exporter", e);
        }
    }

    
    public static class CubeControl extends AbstractControl {
        
        private static final Quaternion ROT = new Quaternion();
        
        private float speed = 2.5F;
        private float rot;
        private Vector3f direction = Vector3f.UNIT_X;

        public CubeControl() {
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public void setDirection(Vector3f direction) {
            this.direction = direction;
        }
        
        @Override
        protected void controlUpdate(float tpf) {
            spatial.rotate(ROT.fromAngleAxis(speed * tpf, direction));
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) { }

        @Override
        public void read(JmeImporter im) throws IOException {
            super.read(im);
            
            InputCapsule in = im.getCapsule(this);
            speed = in.readFloat("speed", 2.5F);
            direction = (Vector3f) in.readSavable("dir", Vector3f.UNIT_X);
        }

        @Override
        public void write(JmeExporter ex) throws IOException {
            super.write(ex);
            
            OutputCapsule out = ex.getCapsule(this);
            out.write(speed, "speed", 2.05f);
            out.write(direction, "dir", Vector3f.UNIT_X);
        }
    }
}
