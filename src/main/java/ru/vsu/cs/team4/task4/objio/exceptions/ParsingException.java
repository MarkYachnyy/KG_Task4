package ru.vsu.cs.team4.task4.objio.exceptions;

public class ParsingException extends ObjReaderException {
    public ParsingException(String type, int lineIndex) {
        super("Failed to parse " + type + " value.", lineIndex);
    }
}
