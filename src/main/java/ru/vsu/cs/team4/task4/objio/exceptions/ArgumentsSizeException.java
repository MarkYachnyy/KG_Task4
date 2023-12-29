package ru.vsu.cs.team4.task4.objio.exceptions;

public class ArgumentsSizeException extends ObjReaderException {
    public ArgumentsSizeException(ArgumentsErrorType errorType, int lineIndex) {
        super("Too " + errorType.getTextValue() + " arguments.", lineIndex);
    }
}
