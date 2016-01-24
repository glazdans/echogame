package com.glazdans.echo.bullet;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;

public class ModelLoader {
    Model model;
    G3dModelLoader modelLoader;

    Model soldierModel;

    public ModelLoader(){
        UBJsonReader jsonReader = new UBJsonReader();

        JsonReader jsonReader1 = new JsonReader();
        modelLoader = new G3dModelLoader(jsonReader1);
    }

    public Model loadModel(){
        model = modelLoader.loadModel(Gdx.files.getFileHandle("model/testScene.g3dj", Files.FileType.Internal));
        return model;
    }

    public Model loadSoldier(){
        soldierModel = modelLoader.loadModel(Gdx.files.getFileHandle("model/soldier.g3dj", Files.FileType.Internal));
        return soldierModel;
    }

}
