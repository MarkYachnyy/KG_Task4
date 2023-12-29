package ru.vsu.cs.team4.task4.objio.exceptions;

public class TokenException extends ObjReaderException {
    public TokenException(int lineIndex) {
        super("Invalid line beginning.", lineIndex);
    }
}
