package ru.vsu.cs.team4.task4.render_engine;

import ru.vsu.cs.team4.task4.model.Model;
import ru.vsu.cs.team4.task4.objio.ObjReader;

public class PreloadedModels {
    public static Model sceneCamera(){
        Model model = ObjReader.read("""
                v 1 1 0
                v -1 1 0
                v -1 -1 0
                v 1 -1 0

                v 0.5 0.5 -1
                v -0.5 0.5 -1
                v -0.5 -0.5 -1
                v 0.5 -0.5 -1

                v 1 1 -1
                v -1 1 -1
                v -1 -1 -1
                v 1 -1 -1

                v 1 1 -4
                v -1 1 -4
                v -1 -1 -4
                v 1 -1 -4

                vt 0 0

                f 1/1 2/1 3/1 4/1
                f 2/1 1/1 5/1 6/1
                f 2/1 6/1 7/1 3/1
                f 4/1 3/1 7/1 8/1
                f 1/1 4/1 8/1 5/1
                f 9/1 10/1 11/1 12/1
                f 10/1 9/1 13/1 14/1
                f 11/1 10/1 14/1 15/1
                f 12/1 11/1 15/1 16/1
                f 9/1 12/1 16/1 13/1
                f 16/1 15/1 14/1 13/1
                """);
        return model;
    }
}
