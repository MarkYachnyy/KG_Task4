package ru.vsu.cs.team4.task4.objio.exceptions;

public class FaceWordTypeException extends ObjReaderException {
    public FaceWordTypeException(int lineIndex) {
        super("Several argument types in one polygon.", lineIndex);
    }
}
