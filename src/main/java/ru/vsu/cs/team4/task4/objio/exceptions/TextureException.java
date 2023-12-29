package ru.vsu.cs.team4.task4.objio.exceptions;

public class TextureException extends ObjReaderException {
    public TextureException(int lineIndex) {
        super("Texture presence mismatch.", lineIndex);
    }
}
